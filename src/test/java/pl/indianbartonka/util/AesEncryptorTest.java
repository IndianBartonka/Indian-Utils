package pl.indianbartonka.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.encrypt.EncryptedFile;
import pl.indianbartonka.util.encrypt.Encryptor;
import pl.indianbartonka.util.encrypt.aes.AESEncryptor;
import pl.indianbartonka.util.encrypt.aes.AESSettings;
import pl.indianbartonka.util.exception.encryption.DecryptException;
import pl.indianbartonka.util.exception.encryption.EncryptException;

public class AesEncryptorTest {

    private final SecretKey secretKey = AESSettings.decodeKey("Rat4eNbQmcLNrU17wDfansXq4QiWUvcH0nZw5c5HOHs=");
    private final IvParameterSpec ivParameterSpec = new IvParameterSpec(AESSettings.decodeIv("Rat4eNbQmcLNrU17wDfansXq4QiWUvcH0nZw5c5HOHs="));
    private final Encryptor encryptor = new AESEncryptor(AESSettings.AESMode.GCM, AESSettings.AESPadding.NO_PADDING, this.ivParameterSpec);

    @BeforeEach
    void encryptorSettings() {
        System.out.println("Algorytm: " + this.encryptor.getAlgorithm());
        System.out.println("ExtensionFile: " + this.encryptor.getFileExtension());
        System.out.println("Provider: " + this.encryptor.getProvider());
        System.out.println();
    }

    @Test
    public void encrypt() throws IOException {
        final File file = new File("Test.txt");

        if (!file.exists()) {
            FileUtil.writeText(file, List.of("DuDuDuDuDu223$%^#%$!%^&#^%$@%^523617"));
        }

        try {
            EncryptedFile encryptedFile = this.encryptor.encryptFile(file, this.secretKey);
            System.out.println(encryptedFile.encryptedFile().getAbsolutePath());
            System.out.println("Zszyfrowano");
        } catch (final EncryptException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void decrypt() {
        final File encryptedFile = new File("Test.txt" + this.encryptor.getFileExtension());

        try {
            final File decryptedFile = this.encryptor.decryptFile(encryptedFile, this.secretKey);
            System.out.println(decryptedFile.getAbsolutePath());
            System.out.println("Odszyfrowano");

            Assertions.assertNotNull(decryptedFile, "Odszyfrowany plik nie powinien być nullem");
            Assertions.assertTrue(decryptedFile.exists(), "Odszyfrowany plik powinien istnieć");
        } catch (final DecryptException exception) {
            exception.printStackTrace();
        }
    }
}