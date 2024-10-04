package pl.indianbartonka.util.language.storage.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import pl.indianbartonka.util.language.storage.StorageStrategy;

public class JsonStorageStrategy implements StorageStrategy {

    private final Gson gson;
    private final Map<String, String> messages;

    public JsonStorageStrategy(final Gson gson) {
        this.gson = gson;
        this.messages = new LinkedHashMap<>();
    }

    @Override
    public String getStrategyName() {
        return "Gson Strategy";
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
        try (final FileReader reader = new FileReader(langFile)) {
            final TypeToken<Map<String, String>> type = new TypeToken<>() {
            };

            final Map<String, String> loadedMessages = this.gson.fromJson(reader, type);

            if (loadedMessages != null) {
                this.messages.putAll(loadedMessages);
            }
        }
    }

    @Override
    public void save(final File langFile) throws IOException {
        try (final FileWriter writer = new FileWriter(langFile)) {
            writer.write(this.gson.toJson(this.messages));
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
