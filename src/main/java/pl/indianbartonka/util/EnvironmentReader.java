package pl.indianbartonka.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class EnvironmentReader {

    private static File environmentFile = new File(".env");

    private static final Map<String, String> keys = new LinkedHashMap<>();

    public static void read() {
        try (final BufferedReader reader = new BufferedReader(new FileReader(environmentFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    final String value = parts[1].trim().replace("\\n", "\n").replace("\\t", "\t");

                    keys.put(parts[0].trim(), value);
                }
            }
        } catch (final IOException ioException) {
            if (IndianUtils.debug) ioException.printStackTrace();
        }
    }

    public static String getEnvironment(final String key) {
        if (keys.isEmpty()) read();

        return keys.get(key);
    }

    public static void setEnvironmentFile(final File environmentFile) {
        EnvironmentReader.environmentFile = environmentFile;
    }
}
