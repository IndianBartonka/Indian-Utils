package me.indian.util.language.storage.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import me.indian.util.language.storage.StorageStrategy;

public class PropertiesStorageStrategy implements StorageStrategy {

    private final Properties properties = new Properties();

    @Override
    public String getStrategyName() {
        return "Properties Strategy";
    }

    @Override
    public boolean containsKey(final String key) {
        return this.properties.containsKey(key);
    }

    @Override
    public boolean saveIsSupported() {
        return true;
    }

    @Override
    public void setMessage(final String key, final String message) {
        this.properties.setProperty(key, message);
    }

    @Override
    public String getMessage(final String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public void load(final File langFile) throws IOException {
        if (langFile.exists()) {
            try (final FileInputStream in = new FileInputStream(langFile)) {
                this.properties.load(in);
            }
        }
    }

    @Override
    public void save(final File langFile) throws IOException {
        try (final FileOutputStream out = new FileOutputStream(langFile)) {
            this.properties.store(out, null);
        }
    }

    @Override
    public Map<String, String> getMessages() {
        final Map<String, String> messagesMap = new HashMap<>();

        for (final String key : this.properties.stringPropertyNames()) {
            messagesMap.put(key, this.getMessage(key));
        }

        return messagesMap;
    }

    @Override
    public void clear() {
        this.properties.clear();
    }


    @Override
    public String toString() {
        return this.getStrategyName() + "(" +
                "saveIsSupported='" + this.saveIsSupported() + '\'' +
                ", messages=" + this.properties +
                ')';
    }
}
