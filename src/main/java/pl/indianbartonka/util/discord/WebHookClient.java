package pl.indianbartonka.util.discord;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import javax.net.ssl.HttpsURLConnection;
import pl.indianbartonka.util.ThreadUtil;
import pl.indianbartonka.util.discord.embed.Embed;
import pl.indianbartonka.util.logger.Logger;
import org.jetbrains.annotations.Nullable;

public class WebHookClient {

    private final Logger logger;
    private final ScheduledExecutorService service;
    private final ReentrantLock lock;
    private final Gson gson;
    private int requests;
    private boolean block;

    public WebHookClient(final Logger logger, final Gson gson, final boolean disableShutdownHook) {
        this.logger = logger.prefixed("Webhook");
        this.service = Executors.newScheduledThreadPool(4, new ThreadUtil("Discord-WebHook"));
        this.lock = new ReentrantLock();
        this.gson = gson;
        this.requests = 0;
        this.block = false;

        this.resetRequestsOnMinute();

        if (!disableShutdownHook) this.addShutdownHook();
    }

    private void addShutdownHook() {
        final ThreadUtil shutdownThread = new ThreadUtil("Shutdown");

        Runtime.getRuntime().addShutdownHook(shutdownThread.newThread(() -> {
            try {
                this.shutdown();
            } catch (final Exception exception) {
                this.logger.critical("Wystąpił błąd podczas próby uruchomienia shutdown hooku ", exception);
            }
        }));
    }

    private void rateLimit() {
        this.logger.debug("Aktualna liczba wysłanych requestów do discord: " + (this.requests + 1));
        if (this.requests == 37) {
            this.rateLimitNow();
        }
    }

    private void rateLimitNow() {
        try {
            this.lock.lock();
            this.block = true;
            this.logger.debug("Czekanie ");
            ThreadUtil.sleep(60);
            this.requests = 0;
            this.block = false;
            this.logger.debug("Przeczekano ");
        } finally {
            this.lock.unlock();
        }
    }

    private void resetRequestsOnMinute() {
        //Requesty nie muszą być zawsze wysyłane co sekunde
        // Więc po 3min resetuje je bo gdy wysyłamy jeden co np 10s to limit nie powinien zostać przekroczony

        this.service.scheduleAtFixedRate(() -> this.requests = 0, 0, 3, TimeUnit.MINUTES);
    }

    public void sendMessage(final String webhookURL, final String userName, @Nullable final String avatarUrl, final String message) {
        this.service.execute(() -> {
            HttpsURLConnection connection = null;

            try {
                this.lock.lock();
                this.rateLimit();

                connection = (HttpsURLConnection) new URL(webhookURL).openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                final JsonObject jsonPayload = new JsonObject();
                jsonPayload.addProperty("content", message);
                jsonPayload.addProperty("username", userName);
                jsonPayload.addProperty("avatar_url", avatarUrl);
                jsonPayload.addProperty("tts", false);

                if (message.isEmpty()) {
                    this.logger.error("Nie można wysłać pustej wiadomości webhookiem!");
                    return;
                }

                try (final OutputStream os = connection.getOutputStream()) {
                    os.write(this.gson.toJson(jsonPayload).getBytes());
                    os.flush();
                }

                this.handleHttpCode(connection.getResponseCode());
            } catch (final Exception exception) {
                this.logger.critical("Nie można wysłać wiadomości do discord ", exception);
            } finally {
                if (connection != null) connection.disconnect();
                this.lock.unlock();
            }
        });
    }

    public void sendEmbedMessage(final String webhookURL, final String userName, @Nullable final String avatarUrl, final Embed embed) {
        this.service.execute(() -> {
            HttpsURLConnection connection = null;

            try {
                this.lock.lock();
                this.rateLimit();

                connection = (HttpsURLConnection) new URL(webhookURL).openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                final JsonObject jsonPayload = new JsonObject();
                jsonPayload.addProperty("username", userName);
                jsonPayload.addProperty("avatar_url", avatarUrl);
                jsonPayload.addProperty("tts", embed.isTts());

                final JsonArray embedsArray = new JsonArray();
                embedsArray.add(embed.getEmbedJson());
                jsonPayload.add("embeds", embedsArray);

                try (final OutputStream os = connection.getOutputStream()) {
                    os.write(this.gson.toJson(jsonPayload).getBytes());
                    os.flush();
                }

                this.handleHttpCode(connection.getResponseCode());
            } catch (final Exception exception) {
                this.logger.critical("Nie można wysłać wiadomości do discord ", exception);
            } finally {
                if (connection != null) connection.disconnect();
                this.lock.unlock();
            }
        });
    }

    public void shutdown() {
        try {
            while (this.block) {
                this.logger.alert("Czekanie na możliwość wysłania requestów do discord, następna próba za&a 10&b sekund");
                ThreadUtil.sleep(10);
            }

            this.logger.info("Zamykanie wątków Webhooku, może to potrwać do&a 3&b minut");
            this.service.shutdown();
            if (!this.service.awaitTermination(3, TimeUnit.MINUTES)) {
                this.logger.error("Wątki nie zostały zamknięte na czas! Wymuszanie zamknięcia");
                this.service.shutdownNow();
            }
            this.logger.info("Zamknięto wątki Webhooku");
        } catch (final Exception exception) {
            this.logger.critical("Wstąpił błąd przy próbie zamknięcia webhooku ", exception);

            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void handleHttpCode(final int code) {
        this.requests++;

        if (code == HttpsURLConnection.HTTP_BAD_REQUEST) {
            this.logger.error("Kod odpowiedzi: " + code + " Oznacza to że dane twojego webhook są błędne");
        } else if (code != HttpsURLConnection.HTTP_NO_CONTENT) {
            if (code == 429) {
                this.logger.warning("&cWysyłasz zbyt dużo&b requestów&c!");
                this.logger.alert("Odczekamy teraz &eminute&r zanim wyślemy następne!");
                this.rateLimitNow();
            } else {
                this.logger.debug("Kod odpowiedzi: " + code);
            }
        }
    }
}