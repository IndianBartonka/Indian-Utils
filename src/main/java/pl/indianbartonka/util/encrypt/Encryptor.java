package pl.indianbartonka.util.encrypt;

import java.io.File;
import java.io.IOException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import pl.indianbartonka.util.logger.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.CheckReturnValue;

public interface Encryptor {

    @NotNull
    EncryptedFile encryptFile(@NotNull File inputFile, @NotNull SecretKey key) throws Exception;

    @NotNull
    File decryptFile(@NotNull File inputFile, @NotNull SecretKey key);

    //TODO: Popraw to.i owo związanego z exception i z adnotacjami 

    @NotNull
    String encryptText(@NotNull String text, @NotNull SecretKey key) ;

    @NotNull
    String decryptText(@NotNull String encryptedText, @NotNull SecretKey key) throws Exception;

    void setLogger(@NotNull Logger logger);

    @NotNull
    IvParameterSpec getIvParameterSpec();

    void setIvParameterSpec(@NotNull IvParameterSpec ivParameterSpec);

    @NotNull
    String getProvider();

    void setProvider(@NotNull String provider);

    @NotNull
    String getEncryptedDir();

    void setEncryptedDir(@NotNull String encryptedDir) throws IOException;

    @NotNull
    String getUserDir();

    void setUserDir(@NotNull String userDir) throws IOException;
}
