package pl.indianbartonka.util.discord;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.jetbrains.annotations.Nullable;
import pl.indianbartonka.util.GsonUtil;
import pl.indianbartonka.util.ThreadUtil;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.discord.embed.Embed;
import pl.indianbartonka.util.http.ContentType;
import pl.indianbartonka.util.http.HttpStatusCode;
import pl.indianbartonka.util.http.UserAgent;
import pl.indianbartonka.util.http.connection.Connection;
import pl.indianbartonka.util.http.connection.request.Request;
import pl.indianbartonka.util.http.connection.request.RequestBody;
import pl.indianbartonka.util.http.connection.request.RequestBuilder;
import pl.indianbartonka.util.logger.Logger;

public class WebHookClient {

    private final Logger logger;
    private final ScheduledExecutorService service;
    private final ReentrantLock lock;
    private final Gson gson;
    private String webhookURL;
    private String userName;
    private @Nullable String avatarUrl;

    public WebHookClient(final Logger logger, final Gson gson, final boolean disableShutdownHook, final String webhookURL, final String userName, @Nullable final String avatarUrl) {
        this.logger = logger;
        this.webhookURL = webhookURL;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.service = Executors.newScheduledThreadPool(4, new ThreadUtil("Discord-WebHook-" + userName));
        this.lock = new ReentrantLock();
        this.gson = gson;

        if (!disableShutdownHook) this.addShutdownHook();
    }

    public WebHookClient(final Logger logger, final boolean disableShutdownHook, final String webhookURL, final String userName, @Nullable final String avatarUrl) {
        this(logger, GsonUtil.getGson(), disableShutdownHook, webhookURL, userName, avatarUrl);
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

    public void sendMessage(final String message) {
        this.service.execute(() -> {

            try {
                this.lock.lock();

                final JsonObject jsonPayload = new JsonObject();
                jsonPayload.addProperty("content", message);
                jsonPayload.addProperty("username", this.userName);
                jsonPayload.addProperty("avatar_url", this.avatarUrl);
                jsonPayload.addProperty("tts", false);

                if (message.isEmpty()) {
                    this.logger.error("Nie można wysłać pustej wiadomości webhookiem!");
                    return;
                }

                final Request request = new RequestBuilder()
                        .setUrl(this.webhookURL)
                        .setContentType(ContentType.APPLICATION_JSON)
                        .setUserAgent(UserAgent.randomUserAgent())
                        .POST(new RequestBody(this.gson.toJson(jsonPayload).getBytes()))
                        .build();

                try (final Connection connection = new Connection(request)) {
                    this.handleHttpCode(connection.getHttpStatusCode(), message, null);
                }

            } catch (final Exception exception) {
                this.logger.critical("Nie można wysłać wiadomości do discord ", exception);
            } finally {
                this.lock.unlock();
            }
        });
    }

    public void sendEmbedMessage(final Embed embed) {
        this.service.execute(() -> {

            try {
                this.lock.lock();

                final JsonObject jsonPayload = new JsonObject();
                jsonPayload.addProperty("username", this.userName);
                jsonPayload.addProperty("avatar_url", this.avatarUrl);
                jsonPayload.addProperty("tts", embed.isTts());

                final JsonArray embedsArray = new JsonArray();
                embedsArray.add(embed.getEmbedJson());
                jsonPayload.add("embeds", embedsArray);

                final Request request = new RequestBuilder()
                        .setUrl(this.webhookURL)
                        .setContentType(ContentType.APPLICATION_JSON)
                        .setUserAgent(UserAgent.randomUserAgent())
                        .POST(new RequestBody(this.gson.toJson(jsonPayload).getBytes()))
                        .build();

                try (final Connection connection = new Connection(request)) {
                    this.handleHttpCode(connection.getHttpStatusCode(), null, embed);
                }

            } catch (final Exception exception) {
                this.logger.critical("Nie można wysłać wiadomości do discord ", exception);
            } finally {
                this.lock.unlock();
            }
        });
    }

    public void shutdown() {
        try {
            while (this.lock.isLocked()) {
                this.logger.alert("Czekanie na możliwość wysłania requestów do discord, następna próba za&a 10&b sekund");
                ThreadUtil.sleep(10);
            }

            if (!this.service.isShutdown()) {
                this.logger.info("Zamykanie wątków Webhooku, może to potrwać do&a 3&b minut");
                this.service.shutdown();
                if (!this.service.awaitTermination(3, TimeUnit.MINUTES)) {
                    this.logger.error("Wątki nie zostały zamknięte na czas! Wymuszanie zamknięcia");
                    this.service.shutdownNow();
                }
                this.logger.info("Zamknięto wątki Webhooku");
            }
        } catch (final Exception exception) {
            this.logger.critical("Wstąpił błąd przy próbie zamknięcia webhooku ", exception);

            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void handleHttpCode(final HttpStatusCode statusCode, final String message, final Embed embed) {
        if (statusCode == HttpStatusCode.UNKNOWN) {
            this.logger.error("Nieznany kod odpowiedzi!");
            return;
        }

        if (statusCode == HttpStatusCode.BAD_REQUEST) {
            this.logger.error("Kod odpowiedzi: " + statusCode + " Oznacza to że dane twojego webhook są błędne");
        } else if (statusCode != HttpStatusCode.NO_CONTENT) {
            if (statusCode == HttpStatusCode.TOO_MANY_REQUESTS) {
                this.logger.warning("&cWysyłasz zbyt dużo&b requestów&c!");
                this.logger.alert("Odczekamy teraz &a30&b sekund&r zanim wyślemy następne!");

                if (message != null) this.sendMessage(message);
                if (embed != null) this.sendEmbedMessage(embed);

                ThreadUtil.sleep(30);

            } else {
                this.logger.error("Kod odpowiedzi: " + statusCode.getCode());
            }
        }
    }

    @Since("0.0.9.5")
    public String getWebhookURL() {
        return this.webhookURL;
    }

    @Since("0.0.9.5")
    public void setWebhookURL(final String webhookURL) {
        this.webhookURL = webhookURL;
    }

    @Since("0.0.9.5")
    public String getUserName() {
        return this.userName;
    }

    @Since("0.0.9.5")
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    @Since("0.0.9.5")
    public @Nullable String getAvatarUrl() {
        return this.avatarUrl;
    }

    @Since("0.0.9.5")
    public void setAvatarUrl(@Nullable final String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
