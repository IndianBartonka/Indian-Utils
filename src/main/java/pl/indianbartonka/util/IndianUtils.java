package pl.indianbartonka.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public final class IndianUtils {

    public static String version = "UNKNOWN";
    public static boolean debug;

    static {
        final InputStream inputStream = IndianUtils.class.getResourceAsStream("/IndianUtils.version");

        if (inputStream != null) {
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    version = line;
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

    public static void setDebug(final boolean debug) {
        IndianUtils.debug = debug;
    }

    private static boolean wineCheck() {
        try {
            final Process process = Runtime.getRuntime().exec("wine --version");
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("wine-")) return true;
                }
            }
            if (!process.waitFor(30, TimeUnit.MILLISECONDS)) process.destroy();
        } catch (final Exception exception) {

        }
        return false;
    }

    private static boolean box64() {
        try {
            final Process process = Runtime.getRuntime().exec("box64 --version");
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Box64")) return true;
                }
            }
            if (!process.waitFor(30, TimeUnit.MILLISECONDS)) process.destroy();
        } catch (final Exception exception) {

        }
        return false;
    }
}
