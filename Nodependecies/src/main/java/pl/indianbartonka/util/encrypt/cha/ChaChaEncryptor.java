package pl.indianbartonka.util.encrypt.cha;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import pl.indianbartonka.util.FileUtil;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.encrypt.EncryptedFile;
import pl.indianbartonka.util.encrypt.Encryptor;
import pl.indianbartonka.util.exception.encryption.DecryptException;
import pl.indianbartonka.util.exception.encryption.EncryptException;
import pl.indianbartonka.util.logger.Logger;

@Since("0.0.9.5")
public final class ChaChaEncryptor implements Encryptor {

    private final String fileExtension;
    private ChaChaSettings.ChaChaMode chaChaMode;
    private IvParameterSpec ivParameterSpec;
    private String provider;
    private Logger logger;

    public ChaChaEncryptor(final ChaChaSettings.ChaChaMode chaChaMode, final IvParameterSpec ivParameterSpec) {
        this(chaChaMode, ivParameterSpec, null);
    }

    public ChaChaEncryptor(final ChaChaSettings.ChaChaMode chaChaMode, final IvParameterSpec ivParameterSpec, final String provider) {
        this.chaChaMode = chaChaMode;
        this.ivParameterSpec = ivParameterSpec;
        this.provider = provider;
        this.fileExtension = ".cha";
    }

    @Override
    public EncryptedFile encryptFile(final @NotNull File inputFile, final @NotNull SecretKey key) throws EncryptException {
        try {
            // Append .cha extension to the encrypted file
            final File encryptedFile = new File(inputFile.getParentFile(), inputFile.getName() + this.fileExtension);
            final Cipher cipher = ChaChaSettings.createCipher(this.chaChaMode, key, this.ivParameterSpec, this.provider, true);
            if (this.logger != null) this.logger.debug("Encrypting file: " + inputFile.getPath());
            this.processFile(cipher, inputFile, encryptedFile);
            if (this.logger != null) this.logger.debug("Encrypted file created: " + encryptedFile.getPath());
            return new EncryptedFile(System.currentTimeMillis(), inputFile.length(), encryptedFile, "ChaCha");
        } catch (final IOException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                       NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException exception) {
            throw new EncryptException("An error occurred while encrypting the file " + inputFile.getName(), exception);
        }
    }

    @Override
    public File decryptFile(final @NotNull File inputFile, final @NotNull SecretKey key) throws DecryptException {
        try {
            // Create decrypted file without .cha extension
            final File decryptedFile = new File(inputFile.getParentFile(), inputFile.getName().replace(this.fileExtension, ""));
            final Cipher cipher = ChaChaSettings.createCipher(this.chaChaMode, key, this.ivParameterSpec, this.provider, false);
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
            final Cipher cipher = ChaChaSettings.createCipher(this.chaChaMode, key, this.ivParameterSpec, this.provider, true);
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
            final Cipher cipher = ChaChaSettings.createCipher(this.chaChaMode, key, this.ivParameterSpec, this.provider, false);
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
        return "ChaCha";
    }

    @Override
    public String getFileExtension() {
        return this.fileExtension;
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

    public ChaChaSettings.ChaChaMode getChaChaMode() {
        return this.chaChaMode;
    }

    public void setChaChaMode(final ChaChaSettings.ChaChaMode chaChaMode) {
        this.chaChaMode = chaChaMode;
    }
}
