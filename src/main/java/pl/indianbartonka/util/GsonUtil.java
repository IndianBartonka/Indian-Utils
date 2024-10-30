package pl.indianbartonka.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;

public final class GsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .setStrictness(Strictness.LENIENT)
            .create();

    private GsonUtil() {
    }

    public static Gson getGson() {
        return GSON;
    }
}