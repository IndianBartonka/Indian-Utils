package pl.indianbartonka.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.CheckReturnValue;

public final class EnvironmentReader {

    private final Map<String, String> keys = new LinkedHashMap<>();
    private final File environmentFile;

    public EnvironmentReader(final File environmentFile) {
        this.environmentFile = environmentFile;
    }

    public void read() throws IOException {
        this.keys.clear();
        try (final BufferedReader reader = new BufferedReader(new FileReader(this.environmentFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    final String value = parts[1].trim().replace("\\n", "\n").replace("\\t", "\t");
                    this.keys.put(parts[0].trim(), value);
                }
            }
        }
    }

    @CheckReturnValue
    public String getEnvironment(final String key) {
        return this.keys.get(key);
    }
}