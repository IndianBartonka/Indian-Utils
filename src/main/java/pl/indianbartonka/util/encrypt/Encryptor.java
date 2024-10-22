package pl.indianbartonka.util.encrypt;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.jetbrains.annotations.NotNull;
import pl.indianbartonka.util.logger.Logger;

public interface Encryptor {

    EncryptedFile encryptFile(@NotNull File inputFile, @NotNull SecretKey key) throws Exception;

    File decryptFile(@NotNull File inputFile, @NotNull SecretKey key) throws NoSuchAlgorithmException;

    //TODO: Popraw to.i owo związanego z exception i z adnotacjami 

    String encryptText(@NotNull String text, @NotNull SecretKey key) ;

    String decryptText(@NotNull String encryptedText, @NotNull SecretKey key) throws Exception;

    void setLogger(@NotNull Logger logger);

    IvParameterSpec getIvParameterSpec();

    void setIvParameterSpec(@NotNull IvParameterSpec ivParameterSpec);

    String getProvider();

    void setProvider(@NotNull String provider);

    String getEncryptedDir();

    void setEncryptedDir(@NotNull String encryptedDir) throws IOException;

    String getUserDir();

    void setUserDir(@NotNull String userDir) throws IOException;
}
