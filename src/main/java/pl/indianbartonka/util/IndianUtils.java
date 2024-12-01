package pl.indianbartonka.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class IndianUtils {

    //Popraw fo bo powinno być lower casse
    public static String version = "UNKNOWN";
    //TODO: dodaj Debug który będzie wywalał jeśli włączony te exception 

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

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private IndianUtils() {

    }
}
