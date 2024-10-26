package pl.indianbartonka.util.encrypt;

import java.io.File;
import java.io.IOException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.jetbrains.annotations.NotNull;
import pl.indianbartonka.util.exception.encryption.DecryptException;
import pl.indianbartonka.util.exception.encryption.EncryptException;
import pl.indianbartonka.util.logger.Logger;

public interface Encryptor {

    EncryptedFile encryptFile(@NotNull File inputFile, @NotNull SecretKey key) throws EncryptException;

    File decryptFile(@NotNull File inputFile, @NotNull SecretKey key) throws DecryptException;

    String encryptText(@NotNull String text, @NotNull SecretKey key) throws EncryptException;

    String decryptText(@NotNull String encryptedText, @NotNull SecretKey key) throws DecryptException;

    void setLogger(@NotNull Logger logger);

    String getAlgorithm();

    String getFileExtension();

    IvParameterSpec getIvParameterSpec();

    void setIvParameterSpec(@NotNull IvParameterSpec ivParameterSpec);

    String getProvider();

    void setProvider(@NotNull String provider);

    String getEncryptedDir();

    void setEncryptedDir(@NotNull String encryptedDir) throws IOException;

    String getUserDir();

    void setUserDir(@NotNull String userDir) throws IOException;
}
