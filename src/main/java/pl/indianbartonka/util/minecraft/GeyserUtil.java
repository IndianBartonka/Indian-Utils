package pl.indianbartonka.util.minecraft;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import org.jetbrains.annotations.VisibleForTesting;
import pl.indianbartonka.util.GsonUtil;
import pl.indianbartonka.util.http.connection.Connection;
import pl.indianbartonka.util.http.connection.request.Request;
import pl.indianbartonka.util.http.connection.request.RequestBuilder;

@VisibleForTesting
public final class GeyserUtil {

    private static final Gson GSON = GsonUtil.getGson();

    private GeyserUtil() {

    }

    public static void main(final String[] args) throws IOException {
        final long xuid = getXUID("JndjanBartonka");
        final String playerName = getPlayerName(xuid);

        System.out.println(xuid);
        System.out.println(playerName);

        System.out.println();
        System.out.println(getBedrockSkinHead(xuid));
        System.out.println(getBedrockSkinBody(xuid));
    }

    public static String getPlayerName(final long xuid) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl("https://api.geysermc.org/v2/xbox/gamertag/" + xuid)
                .addHeader("Accept", "application/json")
                .GET()
                .build();


        try (final Connection connection = new Connection(request)) {
            if (connection.isSuccessful()) {
                final String bodyAsString = connection.getBodyAsString();

                if (bodyAsString == null) return "";

                final String replaced = bodyAsString
                        .replaceAll("\\{\"gamertag\":\"", "")
                        .replaceAll("\"}", "");

                return replaced.trim();
            }
        }

        return "";
    }

    public static long getXUID(final String playerName) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl("https://api.geysermc.org/v2/xbox/xuid/" + playerName)
                .addHeader("Accept", "application/json")
                .GET()
                .build();


        try (final Connection connection = new Connection(request)) {
            if (connection.isSuccessful()) {
                final String bodyAsString = connection.getBodyAsString();

                if (bodyAsString == null) return -1;

                final String replaced = bodyAsString
                        .replaceAll("\\{\"xuid\":", "")
                        .replaceAll("}", "");

                return Long.parseLong(replaced.trim());
            }
        }

        return -1;
    }

    public static String getBedrockSkinBody(final long xuid) throws IOException {
        final SkinResponse skinResponse = sendGeyserSkinRequest(xuid);

        if (skinResponse.textureID() == null) return null;

        return "https://mc-heads.net/body/" + skinResponse.textureID;
    }

    public static String getBedrockSkinHead(final long xuid) throws IOException {
        final SkinResponse skinResponse = sendGeyserSkinRequest(xuid);

        if (skinResponse.textureID() == null) return null;

        return "https://mc-heads.net/head/" + skinResponse.textureID;
    }

    private static SkinResponse sendGeyserSkinRequest(final long xuid) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl("https://api.geysermc.org/v2/skin/" + xuid)
                .GET()
                .build();

        try (final Connection connection = new Connection(request)) {
            if (connection.isSuccessful()) {
                final String bodyAsString = connection.getBodyAsString();

                if (bodyAsString == null) return new SkinResponse(null);

                return GSON.fromJson(bodyAsString, SkinResponse.class);
            } else {
                return new SkinResponse(null);
            }
        }
    }

    record SkinResponse(@SerializedName("texture_id") String textureID) {
    }
}
