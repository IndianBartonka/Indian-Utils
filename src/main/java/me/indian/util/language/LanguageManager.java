package me.indian.util.language;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import me.indian.util.MessageUtil;
import me.indian.util.logger.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * The LanguageManager class is responsible for managing multiple language translations.
 * It allows adding languages, retrieving messages based on keys, and loading or saving languages to files.
 * Docs written by ChatGPT
 */
public final class LanguageManager {

    private final Logger logger;
    private final String languagesDir;
    private final Map<String, Language> languages;
    private Language defaultLanguage;

    /**
     * Constructs a LanguageManager with the specified logger and directory for languages.
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
                entry.getValue().saveToFile();
                if (info) this.logger.info(this.getMessage("language.saving.success", entry.getKey()));
            } catch (final Exception exception) {
                this.logger.error(this.getMessage("language.saving.fail"), exception);
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
    public void loadLanguageFromFile(final Language language) throws IOException {
        language.loadFromFile();
        this.addLanguage(language);
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
     * @return the default Language object
     */
    public Language getDefaultLanguage() {
        return this.defaultLanguage;
    }

    /**
     * Sets the default language, loads its messages from the corresponding file
     *
     * @param defaultLanguage the Language object to be set as default
     * @throws IOException if an I/O error occurs while loading the language from its file
     */
    public void setDefaultLanguage(final Language defaultLanguage) throws IOException {
        this.defaultLanguage = defaultLanguage;

        this.loadLanguageFromFile(defaultLanguage);

        defaultLanguage.addMessage("language.saving.success", "&aSaved language:&b %s");
        defaultLanguage.addMessage("language.saving.failed", "&cFailed to save language:&b %s");
    }

}