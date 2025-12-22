package pl.indianbartonka.util.language;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import pl.indianbartonka.util.MessageUtil;
import pl.indianbartonka.util.language.storage.StorageStrategy;
import pl.indianbartonka.util.logger.Logger;

/**
 * The LanguageManager class is responsible for managing multiple language translations.
 * It allows adding languages, retrieving messages based on keys, and loading or saving languages to files.
 * <p>
 * This class provides functionality to work with language files and manage translations,
 * ensuring that messages can be easily retrieved and modified.
 * </p>
 * <p>
 * Documents written by ChatGPT
 * </p>
 */
public class LanguageManager {

    private final Logger logger;
    private final String languagesDir;
    private final Map<String, Language> languages;
    private Language defaultLanguage;

    /**
     * Constructs a LanguageManager with the specified logger and directory for languages.
     *
     * <p>This constructor initializes a LanguageManager instance that manages
     * multiple language translations. It creates the specified directory if it does not
     * already exist.</p>
     *
     * @param logger       the logger used for logging messages
     * @param languagesDir the directory where language files are stored
     * @throws IOException if an I/O error occurs while creating the directory
     */
    public LanguageManager(final Logger logger, final String languagesDir) throws IOException {
        this.logger = logger;
        this.languagesDir = languagesDir;
        this.languages = new ConcurrentHashMap<>();

        Files.createDirectories(Path.of(languagesDir));
    }

    /**
     * Retrieves the message corresponding to the specified key in the default language.
     *
     * @param key the key for the message
     * @return the message in the default language,
     * or the key itself if no translation is found
     */
    public String getMessage(final String key) {
        if (this.defaultLanguage != null) {
            final String message = this.defaultLanguage.getMessage(key);
            if (message != null) return message;
        }

        return this.findMessage(key);
    }

    /**
     * Retrieves the formatted message corresponding to the specified key in the default language,
     * substituting the format arguments into the message.
     *
     * @param key        the key for the message
     * @param formatArgs the arguments to be formatted into the message
     * @return the formatted message, or the key itself if no translation is found
     */
    public String getMessage(final String key, final Object... formatArgs) {
        return MessageUtil.formatMessage(this.getMessage(key), formatArgs);
    }

    /**
     * Retrieves the formatted message corresponding to the specified key in the specified language,
     * substituting the format arguments into the message.
     *
     * @param key        the key for the message
     * @param language   the Language object from which to retrieve the message
     * @param formatArgs the arguments to be formatted into the message
     * @return the formatted message, or the key itself if no translation is found
     */
    public String getMessage(final String key, final Language language, final Object... formatArgs) {
        return MessageUtil.formatMessage(this.getMessage(key, language), formatArgs);
    }

    /**
     * Retrieves the message corresponding to the specified key in the specified language.
     *
     * @param key      the key for the message
     * @param language the Language object from which to retrieve the message
     * @return the message in the specified language, or the key itself if no translation is found
     */
    public String getMessage(final String key, final Language language) {
        if (language == null) return this.getMessage(key);

        final String message = language.getMessage(key);

        return (message == null ? key : message);
    }

    /**
     * Searches for the message corresponding to the specified key across all loaded languages.
     *
     * @param key the key for the message
     * @return the message if found, or the key itself if no translation is found
     */
    private String findMessage(final String key) {
        for (final Map.Entry<String, Language> entry : this.languages.entrySet()) {
            final String message = entry.getValue().getMessage(key);

            if (message != null) return message;
        }

        return key;
    }

    /**
     * Adds a new language to the manager.
     *
     * @param language the Language object to be added
     */
    public void addLanguage(final Language language) {
        this.languages.put(language.getLanguageCodeName(), language);
    }

    /**
     * Retrieves the Language object corresponding to the specified language code.
     *
     * @param code the language code
     * @return the Language object if found, or null if not found
     */
    @Nullable
    public Language getLanguage(final String code) {
        final Set<Map.Entry<String, Language>> entrySet = this.languages.entrySet();

        // First check for exact matches
        for (final Map.Entry<String, Language> entry : entrySet) {
            if (entry.getKey().equals(code)) return entry.getValue();
        }

        // Then check for approximate matches
        for (final Map.Entry<String, Language> entry : entrySet) {
            if (entry.getKey().contains(code)) return entry.getValue();
        }

        return null;
    }

    /**
     * Saves all loaded languages to their respective files.
     *
     * @param info if true, logs the saving of each language
     */
    public void saveLanguages(final boolean info) {
        for (final Map.Entry<String, Language> entry : this.languages.entrySet()) {
            try {
                final Language language = entry.getValue();
                final StorageStrategy storageStrategy = language.getStorageStrategy();
                if (!storageStrategy.saveIsSupported()) {
                    if (info) {
                        this.logger.error(this.getMessage("language.saving.unsupported", storageStrategy.getStrategyName()));
                    }
                    return;
                }

                language.addMessage("storageStrategyName", language.getStorageStrategy().getStrategyName());
                language.saveToFile();
                if (info) this.logger.info(this.getMessage("language.saving.success", entry.getKey()));
            } catch (final Exception exception) {
                this.logger.error(this.getMessage("language.saving.failed", entry.getKey()), exception);
            }
        }
    }

    /**
     * Saves a specific language to its file and adds it to the manager.
     *
     * @param language the Language object to be saved
     * @throws IOException if an I/O error occurs while saving
     */
    public void saveLanguage(final Language language) throws IOException {
        language.saveToFile();
        this.addLanguage(language);
    }

    /**
     * Loads a language from its file and adds it to the manager.
     *
     * @param language the Language object to be loaded
     * @throws IOException if an I/O error occurs while loading
     */
    public void loadLanguage(final Language language) throws IOException {
        language.loadFromFile();
        this.addLanguage(language);
    }

    /**
     * Loads a language from a file and adds it to the language manager.
     *
     * @param file            The file from which the language will be loaded. The file name should contain the language code (e.g., "pl.txt").
     * @param storageStrategy The storage strategy used to load the language data.
     * @return The {@link Language} object that has been loaded from the file.
     * @throws IOException If the file cannot be read or an error occurs during loading.
     */
    public Language loadFromFile(final File file, final StorageStrategy storageStrategy) throws IOException {
        final String languageCodeName = file.getName().split("\\.")[0];

        final Language language = new Language(languageCodeName, this, storageStrategy) {
        };

        language.getStorageStrategy().load(file);

        this.addLanguage(language);

        return language;
    }

    /**
     * Retrieves the directory where language files are stored.
     *
     * @return the languages directory
     */
    public String getLanguagesDir() {
        return this.languagesDir;
    }

    /**
     * Retrieves all loaded languages.
     *
     * @return a map of language codes to Language objects
     */
    public Map<String, Language> getLanguages() {
        return this.languages;
    }

    /**
     * Retrieves the default language.
     *
     * @return the default Language object, or null if not set
     */
    @Nullable
    public Language getDefaultLanguage() {
        return this.defaultLanguage;
    }

    /**
     * Sets the default language and loads its messages from the corresponding file.
     *
     * @param defaultLanguage the Language object to be set as default
     * @throws IOException if an I/O error occurs while loading the language from its file
     */
    public void setDefaultLanguage(final Language defaultLanguage) throws IOException {
        if (defaultLanguage == null) return;
        this.defaultLanguage = defaultLanguage;

        this.loadLanguage(defaultLanguage);

        defaultLanguage.addMessage("language.saving.success", "&aSaved language:&b %s");
        defaultLanguage.addMessage("language.saving.failed", "&cFailed to save language:&b %s");
        defaultLanguage.addMessage("language.saving.unsupported", "&cStorage strategy:&b %s&r doesn't support saving to file");
    }
}
