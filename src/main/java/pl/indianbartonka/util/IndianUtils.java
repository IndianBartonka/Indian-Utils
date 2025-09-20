package pl.indianbartonka.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.VisibleForTesting;
import pl.indianbartonka.util.annotation.UtilityClass;

@UtilityClass
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


                final List<String> flags = ManagementFactory.getRuntimeMXBean().getInputArguments();

                if (flags.contains("-UtilsDebug")) {
                    debug = true;
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

    @VisibleForTesting
    public static boolean wineCheck() {
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
            if (IndianUtils.debug) exception.printStackTrace();
        }
        return false;
    }

    @VisibleForTesting
    public static boolean box64Check() {
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
            if (IndianUtils.debug) exception.printStackTrace();
        }
        return false;
    }

    @VisibleForTesting
    public static boolean box86Check() {
        try {
            final Process process = Runtime.getRuntime().exec("box86 --version");
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Box86")) return true;
                }
            }
            if (!process.waitFor(30, TimeUnit.MILLISECONDS)) process.destroy();
        } catch (final Exception exception) {
            if (IndianUtils.debug) exception.printStackTrace();
        }
        return false;
    }
}
