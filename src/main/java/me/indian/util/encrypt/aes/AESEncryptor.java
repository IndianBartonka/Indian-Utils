package me.indian.util.encrypt.aes;

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
import me.indian.util.BufferUtil;
import me.indian.util.FileUtil;
import me.indian.util.encrypt.Encryptor;
import me.indian.util.logger.Logger;

public final class AESEncryptor implements Encryptor {

    private AESSettings.AESMode aesMode;
    private AESSettings.AESPadding aesPadding;
    private IvParameterSpec ivParameterSpec;
    private String encryptedDir, userDir;
    private Logger logger;

    public AESEncryptor(final AESSettings.AESMode aesMode, final AESSettings.AESPadding aesPadding, final IvParameterSpec ivParameterSpec) {
        this.aesMode = aesMode;
        this.aesPadding = aesPadding;
        this.ivParameterSpec = ivParameterSpec;
        this.encryptedDir = System.getProperty("user.dir") + File.separator + "encrypted_dir" + File.separator;
        this.userDir = System.getProperty("user.dir") + File.separator;
    }

    @Override
    public File encryptFile(final File inputFile, final SecretKey key) throws Exception {
        this.createMissingDirs();
        final File encryptedFile = new File(this.encryptedDir, inputFile.getName());
        final Cipher cipher = AESSettings.createCipher(this.aesMode, this.aesPadding, key, this.ivParameterSpec, true);
        if (this.logger != null) this.logger.debug("Szyfrowanie pliku: " + inputFile.getPath());
        this.processFile(cipher, inputFile, encryptedFile);
        if (this.logger != null) this.logger.debug("Zszyfrowna plik " + encryptedFile.getPath());
        return encryptedFile;
    }

    @Override
    public File decryptFile(final File inputFile, final SecretKey key) throws Exception {
        this.createMissingDirs();
        final File decryptedFile = new File(this.userDir, inputFile.getName());
        final Cipher cipher = AESSettings.createCipher(this.aesMode, this.aesPadding, key, this.ivParameterSpec, false);
        if (this.logger != null) this.logger.debug("Odszyfrowanie pliku: " + inputFile.getPath());
        this.processFile(cipher, inputFile, decryptedFile);
        if (this.logger != null) this.logger.debug("Odszyfrowna plik " + inputFile.getPath());
        return decryptedFile;
    }

    private void processFile(final Cipher cipher, final File inputFile, final File outputFile) throws Exception {
        try (final FileInputStream in = new FileInputStream(inputFile);
             final FileOutputStream out = new FileOutputStream(outputFile)) {
            final byte[] inputBytes = new byte[BufferUtil.defineBuffer(BufferUtil.DownloadBuffer.DYNAMIC, FileUtil.getFileSize(inputFile))];
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

    @Override
    public String encryptText(final String text, final SecretKey key) throws Exception {
        final Cipher cipher = AESSettings.createCipher(this.aesMode, this.aesPadding, key, this.ivParameterSpec, true);
        final byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @Override
    public String decryptText(final String encryptedText, final SecretKey key) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        final Cipher cipher = AESSettings.createCipher(this.aesMode, this.aesPadding, key, this.ivParameterSpec, false);
        final byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        final byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    @Override
    public void setLogger(final Logger logger) {
        if (this.logger == null) this.logger = logger.prefixed("AESEncryptor");
    }

    public AESSettings.AESMode getAesMode() {
        return this.aesMode;
    }

    public void setAesMode(final AESSettings.AESMode aesMode) {
        this.aesMode = aesMode;
    }

    @Override
    public IvParameterSpec getIvParameterSpec() {
        return this.ivParameterSpec;
    }

    @Override
    public void setIvParameterSpec(final IvParameterSpec ivParameterSpec) {
        this.ivParameterSpec = ivParameterSpec;
    }

    public AESSettings.AESPadding getAesPadding() {
        return this.aesPadding;
    }

    public void setAesPadding(final AESSettings.AESPadding aesPadding) {
        this.aesPadding = aesPadding;
    }

    @Override
    public void setEncryptedDir(final String encryptedDir) throws IOException {
        this.encryptedDir = encryptedDir;

        Files.createDirectories(Path.of(encryptedDir));
    }

    @Override
    public void setUserDir(final String userDir) throws IOException {
        this.userDir = userDir;

        Files.createDirectories(Path.of(userDir));
    }

    private void createMissingDirs() throws IOException {
        Files.createDirectories(Path.of(this.userDir));
        Files.createDirectories(Path.of(this.encryptedDir));
    }

    @Override
    public String getEncryptedDir() {
        return this.encryptedDir;
    }

    @Override
    public String getUserDir() {
        return this.userDir;
    }
}