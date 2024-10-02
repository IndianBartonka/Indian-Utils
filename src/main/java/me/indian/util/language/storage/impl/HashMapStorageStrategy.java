package me.indian.util.language.storage.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import me.indian.util.language.storage.StorageStrategy;

public class HashMapStorageStrategy implements StorageStrategy {

    private final Map<String, String> messages = new LinkedHashMap<>();

    @Override
    public String getStrategyName() {
        return "HashMap Strategy";
    }

    @Override
    public boolean containsKey(final String key) {
        return this.messages.containsKey(key);
    }

    @Override
    public boolean saveIsSupported() {
        return true;
    }

    @Override
    public void setMessage(final String key, final String message) {
        this.messages.put(key, message);
    }

    @Override
    public String getMessage(final String key) {
        return this.messages.get(key);
    }

    @Override
    public void load(final File langFile) throws IOException {
        try (final BufferedReader reader = new BufferedReader(new FileReader(langFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    final String value = parts[1].trim().replace("\\n", "\n").replace("\\t", "\t");

                    this.messages.put(parts[0].trim(), value);
                }
            }
        }
    }

    @Override
    public void save(final File langFile) throws IOException {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(langFile))) {
            for (final Map.Entry<String, String> entry : this.messages.entrySet()) {
                final String value = entry.getValue().replace("\n", "\\n").replace("\t", "\\t");

                writer.write(entry.getKey() + "=" + value);
                writer.newLine();
            }
        }
    }

    @Override
    public Map<String, String> getMessages() {
        return this.messages;
    }

    @Override
    public void clear() {
        this.messages.clear();
    }

    @Override
    public String toString() {
        return this.getStrategyName() + "(" +
                "saveIsSupported='" + this.saveIsSupported() + '\'' +
                ", messages=" + this.messages +
                ')';
    }
}