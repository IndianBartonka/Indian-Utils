package pl.indianbartonka.util.language;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import pl.indianbartonka.util.language.storage.StorageStrategy;

public abstract class Language {

    protected final File langFile;
    private final String languageCodeName;
    protected StorageStrategy storageStrategy;

    protected Language(final String languageCodeName, final LanguageManager languageManager, final StorageStrategy storageStrategy) {
        this.languageCodeName = languageCodeName;
        this.storageStrategy = storageStrategy;
        this.langFile = new File(languageManager.getLanguagesDir() + File.separator + (this.languageCodeName + ".lang"));
    }

    public void addMessage(final String key, final String message) {
        if (!this.storageStrategy.containsKey(key)) {
            this.storageStrategy.setMessage(key, message);
        }
    }

    public String getMessage(final String key) {
        return this.storageStrategy.getMessage(key);
    }

    public void saveToFile() throws IOException {
        if (!this.storageStrategy.saveIsSupported()) return;
        this.storageStrategy.save(this.langFile);
    }

    public void loadFromFile() throws IOException {
        if (!this.langFile.exists() || !this.storageStrategy.saveIsSupported()) return;

        this.storageStrategy.clear();
        this.storageStrategy.load(this.langFile);
    }

    public final String getLanguageCodeName() {
        return this.languageCodeName;
    }

    public final File getLangFile() {
        return this.langFile;
    }

    public Map<String, String> getMessages() {
        return this.storageStrategy.getMessages();
    }

    public final StorageStrategy getStorageStrategy() {
        return this.storageStrategy;
    }

    @Override
    public String toString() {
        return "Language(" +
                "languageCodeName='" + this.languageCodeName + '\'' +
                ", langFile=" + this.langFile +
                ", storageStrategy=" + this.storageStrategy +
                ')';
    }
}
