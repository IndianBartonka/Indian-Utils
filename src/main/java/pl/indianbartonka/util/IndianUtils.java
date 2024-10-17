package pl.indianbartonka.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class IndianUtils {

    public static String VERSION = "UNKNOWN";

    static {
        final InputStream inputStream = IndianUtils.class.getResourceAsStream("/IndianUtils.version");

        if (inputStream != null) {
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    VERSION = line;
                }
            } catch (final IOException ignored) {

            }
        }
    }

    private IndianUtils() {

    }
}