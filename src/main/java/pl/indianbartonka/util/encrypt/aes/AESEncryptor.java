package pl.indianbartonka.util.encrypt.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.indianbartonka.util.BufferUtil;
import pl.indianbartonka.util.encrypt.EncryptedFile;
import pl.indianbartonka.util.encrypt.Encryptor;
import pl.indianbartonka.util.exception.encryption.DecryptException;
import pl.indianbartonka.util.exception.encryption.EncryptException;
import pl.indianbartonka.util.file.FileUtil;
import pl.indianbartonka.util.logger.Logger;

public final class AESEncryptor implements Encryptor {

    private final String fileExtension;
    private AESSettings.AESMode aesMode;
    private AESSettings.AESPadding aesPadding;
    private IvParameterSpec ivParameterSpec;
    private String provider;
    private String encryptedDir;
    private String userDir;
    private Logger logger;

    public AESEncryptor(final AESSettings.AESMode aesMode, final AESSettings.AESPadding aesPadding, final IvParameterSpec ivParameterSpec) {
        this(aesMode, aesPadding, ivParameterSpec, null);
    }

    public AESEncryptor(final AESSettings.AESMode aesMode, final AESSettings.AESPadding aesPadding, final IvParameterSpec ivParameterSpec, final String provider) {
        this.aesMode = aesMode;
        this.aesPadding = aesPadding;
        this.ivParameterSpec = ivParameterSpec;
        this.provider = provider;
        this.encryptedDir = System.getProperty("user.dir") + File.separator + "encrypted_dir" + File.separator;
        this.userDir = System.getProperty("user.dir") + File.separator;
        this.fileExtension = ".aes";
    }

    @Override
    public EncryptedFile encryptFile(final @NotNull File inputFile, final @NotNull SecretKey key) throws EncryptException {
        try {
            this.createMissingDirs();
            // Append .aes extension to the encrypted file
            final File encryptedFile = new File(this.encryptedDir, inputFile.getName() + this.fileExtension);
            final Cipher cipher = AESSettings.createCipher(this.aesMode, this.aesPadding, key, this.ivParameterSpec, this.provider, true);
            if (this.logger != null) this.logger.debug("Encrypting file: " + inputFile.getPath());
            this.processFile(cipher, inputFile, encryptedFile);
            if (this.logger != null) this.logger.debug("Encrypted file created: " + encryptedFile.getPath());
            return new EncryptedFile(System.currentTimeMillis(), inputFile.length(), encryptedFile, "AES");
        } catch (final IOException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                       NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException exception) {
            throw new EncryptException("An error occurred while encrypting the file " + inputFile.getName(), exception);
        }
    }

    @Override
    public File decryptFile(final @NotNull File inputFile, final @NotNull SecretKey key) throws DecryptException {
        try {
            this.createMissingDirs();
            // Create decrypted file without .aes extension
            final File decryptedFile = new File(this.userDir, inputFile.getName().replace(this.fileExtension, ""));
            final Cipher cipher = AESSettings.createCipher(this.aesMode, this.aesPadding, key, this.ivParameterSpec, this.provider, false);
            if (this.logger != null) this.logger.debug("Decrypting file: " + inputFile.getPath());
            this.processFile(cipher, inputFile, decryptedFile);
            if (this.logger != null) this.logger.debug("Decrypted file created: " + decryptedFile.getPath());
            return decryptedFile;
        } catch (final IOException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                       NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException exception) {
            throw new DecryptException("An error occurred while decrypting the file " + inputFile.getName(), exception);
        }
    }

    private void processFile(final Cipher cipher, final File inputFile, final File outputFile) throws IOException {
        try (final FileInputStream in = new FileInputStream(inputFile);
             final CipherInputStream cipherIn = new CipherInputStream(in, cipher);
             final FileOutputStream out = new FileOutputStream(outputFile)) {

            final byte[] buffer = new byte[BufferUtil.calculateOptimalBufferSize(FileUtil.getFileSize(inputFile))];
            int bytesRead;

            while ((bytesRead = cipherIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    @Override
    public String encryptText(final @NotNull String text, final @NotNull SecretKey key) throws EncryptException {
        try {
            final Cipher cipher = AESSettings.createCipher(this.aesMode, this.aesPadding, key, this.ivParameterSpec, this.provider, true);
            final byte[] encryptedBytes = cipher.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (final InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchAlgorithmException |
                       InvalidKeyException | NoSuchProviderException | IllegalBlockSizeException |
                       BadPaddingException exception) {
            throw new EncryptException("An error occurred while encrypting the text " + text, exception);
        }
    }

    @Override
    public String decryptText(final @NotNull String encryptedText, final @NotNull SecretKey key) throws DecryptException {
        try {
            final Cipher cipher = AESSettings.createCipher(this.aesMode, this.aesPadding, key, this.ivParameterSpec, this.provider, false);
            final byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            final byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (final InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchAlgorithmException |
                       InvalidKeyException | NoSuchProviderException | IllegalBlockSizeException |
                       BadPaddingException exception) {
            throw new DecryptException("An error occurred while decrypting the text " + encryptedText, exception);
        }
    }

    @Override
    public void setDebugLogger(final @NotNull Logger logger) {
        if (this.logger == null) this.logger = logger;
    }

    @Override
    public String getAlgorithm() {
        return "AES";
    }

    @Override
    public String getFileExtension() {
        return this.fileExtension;
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
    public void setIvParameterSpec(final @NotNull IvParameterSpec ivParameterSpec) {
        this.ivParameterSpec = ivParameterSpec;
    }

    @Nullable
    @Override
    public String getProvider() {
        return this.provider;
    }

    @Override
    public void setProvider(final @NotNull String provider) {
        this.provider = provider;
    }

    public AESSettings.AESPadding getAesPadding() {
        return this.aesPadding;
    }

    public void setAesPadding(final AESSettings.AESPadding aesPadding) {
        this.aesPadding = aesPadding;
    }

    public void createMissingDirs() throws IOException {
        Files.createDirectories(Path.of(this.userDir));
        Files.createDirectories(Path.of(this.encryptedDir));
    }

    @Override
    public String getEncryptedDir() {
        return this.encryptedDir;
    }

    @Override
    public void setEncryptedDir(final @NotNull String encryptedDir) throws IOException {
        this.encryptedDir = encryptedDir;

        Files.createDirectories(Path.of(encryptedDir));
    }

    @Override
    public String getUserDir() {
        return this.userDir;
    }

    @Override
    public void setUserDir(final @NotNull String userDir) throws IOException {
        this.userDir = userDir;

        Files.createDirectories(Path.of(userDir));
    }
}