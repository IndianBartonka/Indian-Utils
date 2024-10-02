package me.indian.util.language.storage;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface StorageStrategy {

    String getStrategyName();

    boolean containsKey(String key);

    boolean saveIsSupported();

    void setMessage(String key, String message);

    String getMessage(String key);

    void load(final File langFile) throws IOException;

    void save(final File langFile) throws IOException;

    Map<String, String> getMessages();

    void clear();

}
