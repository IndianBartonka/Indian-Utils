package me.indian.util.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import me.indian.util.logger.Logger;

public final class Encryptor {

    private static Logger LOGGER;
    private static AESSettings.AESMode MODE;
    private static AESSettings.AESPadding PADDING;
    private static IvParameterSpec IV_PARAMETER_SPEC;
    public static String ENCRYPTED_DIR = System.getProperty("user.dir") + File.separator + "encrypted_dir" + File.separator;
    private static String USER_DIR = System.getProperty("user.dir") + File.separator;

    static {
        try {
            Files.createDirectories(Path.of(ENCRYPTED_DIR));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setLogger(final Logger logger) {
        LOGGER = logger.prefixed("Encryptor");
    }

    public static void setEncryptorSettings(final AESSettings.AESMode mode, final AESSettings.AESPadding padding, final IvParameterSpec ivParameterSpec) {
        MODE = mode;
        PADDING = padding;
        IV_PARAMETER_SPEC = ivParameterSpec;
    }

    public static void setUserDir(final String userDir) {
        USER_DIR = userDir;
    }

    public static void setEncryptedDir(final String encryptedDir) {
        ENCRYPTED_DIR = encryptedDir;
    }

    public static File encryptFile(final File inputFile, final SecretKey key) throws Exception {
        final File encryptedFile = new File(ENCRYPTED_DIR, inputFile.getName());
        final Cipher cipher = AESSettings.createCipher(MODE, PADDING, key, IV_PARAMETER_SPEC, true);
        if (LOGGER != null) LOGGER.debug("Szyfrowanie pliku: " + inputFile.getPath());
        processFile(cipher, inputFile, encryptedFile);
        if (LOGGER != null) LOGGER.debug("Zszyfrowna plik " + encryptedFile.getPath());
        return encryptedFile;
    }

    public static File decryptFile(final File inputFile, final SecretKey key) throws Exception {
        final File decryptedFile = new File(USER_DIR, inputFile.getName());
        final Cipher cipher = AESSettings.createCipher(MODE, PADDING, key, IV_PARAMETER_SPEC, false);
        if (LOGGER != null) LOGGER.debug("Odszyfrowanie pliku: " + inputFile.getPath());
        processFile(cipher, inputFile, decryptedFile);
        if (LOGGER != null) LOGGER.debug("Odszyfrowna plik " + inputFile.getPath());
        return decryptedFile;
    }

    private static void processFile(final Cipher cipher, final File inputFile, final File outputFile) throws Exception {
        try (final FileInputStream in = new FileInputStream(inputFile);
             final FileOutputStream out = new FileOutputStream(outputFile)) {
            final byte[] inputBytes = new byte[64];
            int bytesRead;
            while ((bytesRead = in.read(inputBytes)) != -1) {
                final byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
                if (outputBytes != null) {
                    out.write(outputBytes);
                }
            }
            final byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                out.write(outputBytes);
            }
        }
    }

    public static String encryptText(final String text, final SecretKey key) throws Exception {
        final Cipher cipher = AESSettings.createCipher(MODE, PADDING, key, IV_PARAMETER_SPEC, true);
        final byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptText(final String encryptedText, final SecretKey key) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        final Cipher cipher = AESSettings.createCipher(MODE, PADDING, key, IV_PARAMETER_SPEC, false);
        final byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        final byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}