package me.indian.util.discord;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import me.indian.util.DateUtil;
import me.indian.util.MessageUtil;
import me.indian.util.ThreadUtil;
import me.indian.util.discord.embed.component.Field;
import me.indian.util.discord.embed.component.Footer;
import me.indian.util.logger.Logger;
import org.jetbrains.annotations.Nullable;

public class WebHookSender {

    private final Logger logger;
    private final ExecutorService service;
    private final ReentrantLock lock;
    private final Gson gson;
    private int requests;
    private boolean block;

    public WebHookSender(final Logger logger, final Gson gson) {
        this.logger = logger.prefixed("Webhook");
        this.service = Executors.newScheduledThreadPool(2, new ThreadUtil("Discord-WebHook"));
        this.lock = new ReentrantLock();
        this.gson = gson;
        this.requests = 0;
        this.block = false;

        this.resetRequestsOnMinute();
    }

    private void rateLimit() {
        this.logger.debug("Aktualna liczba wysłanych requestów do discord: " + (this.requests + 1));
        if (this.requests == 37) {
            this.block = true;
            this.logger.debug("Czekanie ");
            ThreadUtil.sleep(60);
            this.requests = 0;
            this.block = false;
            this.logger.debug("Przeczekano ");
        }
    }

    private void resetRequestsOnMinute() {
        //Requesty nie muszą być zawsze wysyłane co sekunde
        // Więc po 3min resetuje je bo gdy wysyłamy jeden co np 10s to limit nie powinien zostać przekroczony

        final TimerTask task = new TimerTask() {
            public void run() {
                WebHookSender.this.requests = 0;
            }
        };
        new Timer("Webhook request cleaner", true).scheduleAtFixedRate(task, 0, DateUtil.minutesTo(3, TimeUnit.MILLISECONDS));
    }

    public void sendMessage(final String webhookURL, final String userName, final String message, @Nullable final String avatarUrl) {
        this.service.execute(() -> {
            try {
                this.lock.lock();
                this.rateLimit();

                final HttpURLConnection connection = (HttpURLConnection) new URL(webhookURL).openConnection();
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
                this.lock.unlock();
            }
        });
    }

    public void sendMessage(final String webhookURL, final String userName, final String message, @Nullable final String avatarUrl, final Throwable throwable) {
        this.sendMessage(webhookURL, userName,
                message + (throwable == null ? "" : "\n```" + MessageUtil.getStackTraceAsString(throwable) + "```"), avatarUrl);
    }

    public void sendEmbedMessage(final String webhookURL, final String userName, @Nullable final String avatarUrl, final String title, final String message, final List<Field> fields, final Footer footer) {
        this.service.execute(() -> {
            try {
                this.lock.lock();
                this.rateLimit();

                final HttpURLConnection connection = (HttpURLConnection) new URL(webhookURL).openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                final JsonObject jsonPayload = new JsonObject();
                jsonPayload.addProperty("username", userName);
                jsonPayload.addProperty("avatar_url", avatarUrl);
                jsonPayload.addProperty("tts", false);

                final JsonObject embed = new JsonObject();
                embed.addProperty("title", title);
                embed.addProperty("description", message);

                if (fields != null && !fields.isEmpty()) {
                    final JsonArray fieldsArray = new JsonArray();
                    for (final Field field : fields) {
                        final JsonObject fieldObject = new JsonObject();
                        fieldObject.addProperty("name", field.name());
                        fieldObject.addProperty("value", field.value());
                        fieldObject.addProperty("inline", field.inline());
                        fieldsArray.add(fieldObject);
                    }

                    embed.add("fields", fieldsArray);
                }

                final JsonObject footerObject = new JsonObject();
                footerObject.addProperty("text", footer.text());
                footerObject.addProperty("icon_url", footer.imageURL());

                embed.add("footer", footerObject);
                embed.addProperty("color", 3838);

                final JsonArray embeds = new JsonArray();
                embeds.add(embed);
                jsonPayload.add("embeds", embeds);

                try (final OutputStream os = connection.getOutputStream()) {
                    os.write(this.gson.toJson(jsonPayload).getBytes());
                    os.flush();
                }

                this.handleHttpCode(connection.getResponseCode());
            } catch (final Exception exception) {
                this.logger.critical("Nie można wysłać wiadomości do discord ", exception);
            } finally {
                this.lock.unlock();
            }
        });
    }

    public void sendEmbedMessage(final String webhookURL, final String userName, @Nullable final String avatarUrl, final String title, final String message, final List<Field> fields, final Throwable throwable, final Footer footer) {
        this.sendEmbedMessage(webhookURL, userName, avatarUrl, title, message +
                (throwable == null ? "" : "\n```" + MessageUtil.getStackTraceAsString(throwable) + "```"), fields, footer);
    }

    public void sendEmbedMessage(final String webhookURL, final String userName, @Nullable final String avatarUrl, final String title, final String message, final Footer footer) {
        this.sendEmbedMessage(webhookURL, userName, avatarUrl, title, message, (List<Field>) null, footer);
    }

    public void sendEmbedMessage(final String webhookURL, final String userName, @Nullable final String avatarUrl, final String title, final String message, final Throwable throwable, final Footer footer) {
        this.sendEmbedMessage(webhookURL, userName, avatarUrl, title, message +
                (throwable == null ? "" : "\n```" + MessageUtil.getStackTraceAsString(throwable) + "```"), footer);
    }

    public void shutdown() {
        try {
            while (this.block) {
                this.logger.alert("Czekanie na możliwość wysłania requestów do discord, następna próba za&a 10&b sekund");
                ThreadUtil.sleep(10);
            }

            this.logger.info("Zamykanie wątków Webhooku");
            this.service.shutdown();
            if (!this.service.awaitTermination(20, TimeUnit.SECONDS)) {
                this.logger.error("Wątki nie zostały zamknięte na czas! Wymuszanie zamknięcia");
                this.service.shutdownNow();
            }
            this.logger.info("Zamknięto wątki Webhooku");
        } catch (final Exception exception) {
            this.logger.critical("Wstąpił błąd przy próbie zamknięcia webhooku ", exception);
        }
    }

    private void handleHttpCode(final int code) {
        switch (code) {
            case HttpURLConnection.HTTP_NO_CONTENT -> this.requests++;
            case HttpURLConnection.HTTP_BAD_REQUEST ->
                    this.logger.error("Kod odpowiedzi: " + code + " Oznacza to że dane twojego webhook są błędne");
            default -> this.logger.debug("Kod odpowiedzi: " + code);
        }
    }
}