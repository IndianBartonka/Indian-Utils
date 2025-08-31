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
import pl.indianbartonka.util.encrypt.aes.AESSettings;
import pl.indianbartonka.util.encrypt.cha.ChaChaSettings;
import pl.indianbartonka.util.encrypt.cha.ChaChaEncryptor;
import pl.indianbartonka.util.exception.encryption.DecryptException;
import pl.indianbartonka.util.exception.encryption.EncryptException;

public class ChaChaEncryptorTest {

    private final SecretKey secretKey = AESSettings.decodeKey("XfMjx8FOEc1LKb2q1aM9YoSpSwvYejA8W//FjAVkuDI=");
    private final IvParameterSpec ivParameterSpec = new IvParameterSpec(AESSettings.decodeIv("VngZURGvzyQ3kUBX"));
    private final Encryptor encryptor = new ChaChaEncryptor(ChaChaSettings.ChaChaMode.CHACHA20_POLY1305, this.ivParameterSpec);

    @BeforeEach
    void encryptorSettings() {
        System.out.println("Algorytm: " + this.encryptor.getAlgorithm());
        System.out.println("ExtensionFile: " + this.encryptor.getFileExtension());
        System.out.println("Provider: " + this.encryptor.getProvider());
        System.out.println();
    }

    @Test
    public void encrypt() throws IOException {
        final File file = new File("ChaChaTest.txt");

        if (!file.exists()) {
            FileUtil.writeText(file, List.of("Czekoladowy pociąg🚂🚂"));
        }

        try {
            final EncryptedFile encryptedFile = this.encryptor.encryptFile(file, this.secretKey);
            System.out.println(encryptedFile.encryptedFile().getAbsolutePath());
            System.out.println("Zszyfrowano");
        } catch (final EncryptException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void decrypt() {
        final File encryptedFile = new File("ChaChaTest.txt" + this.encryptor.getFileExtension());

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