package me.indian.util.language;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public abstract class Language {

    private final String languageCodeName;
    protected final File langFile;
    protected Properties messages;

    protected Language(final String languageCodeName, final LanguageManager languageManager) {
        this.languageCodeName = languageCodeName;
        this.messages = new Properties();
        this.langFile = new File(languageManager.getLanguagesDir() + File.separator + (this.languageCodeName + ".lang"));
    }

    public void addMessage(final String key, final String message) {
        if (!this.messages.containsKey(key)) {
            this.messages.setProperty(key, message);
        }
    }

    public String getMessage(final String key) {
        return this.messages.getProperty(key);
    }

    public void saveToFile() throws IOException {
        try (final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(this.langFile), StandardCharsets.UTF_8)) {
            this.messages.store(writer, null);
        }
    }

    public void loadFromFile() throws IOException {
        if (!this.langFile.exists()) return;

        this.messages.clear();
        try (final InputStreamReader input = new InputStreamReader(new FileInputStream(this.langFile), StandardCharsets.UTF_8)) {
            this.messages.load(input);
        }
    }

    public final String getLanguageCodeName() {
        return this.languageCodeName;
    }

    public final File getLangFile() {
        return this.langFile;
    }

    public final Properties getMessages() {
        return this.messages;
    }

    @Override
    public String toString() {
        return "Language(" +
                "languageCodeName='" + this.languageCodeName + '\'' +
                ", langFile=" + this.langFile +
                ", messages=" + this.messages +
                ')';
    }
}
