package pl.indianbartonka.util.encrypt.aes;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESSettings {

    public static SecretKey generateKey(final AESKeySize keySize) {
        final KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (final NoSuchAlgorithmException ignored) {
            //Algorytm AES jest istniejącym algorytmem
            return null;
        }
        keyGen.init(keySize.getKeySize());
        return keyGen.generateKey();
    }

    public static String encodeKey(final SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static SecretKey decodeKey(final String base64Key) {
        return new SecretKeySpec(Base64.getDecoder().decode(base64Key), "AES");
    }

    public static IvParameterSpec generateIV(final AESMode mode) {
        final byte[] iv = new byte[mode.getIvSize()];
        final SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static Cipher createCipher(final AESMode mode, final AESPadding padding, final SecretKey key,
                                      final IvParameterSpec ivSpec, final String provider, final boolean encrypt)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidAlgorithmParameterException, NoSuchProviderException {

        // Define the encryption transformation
        final String transformation = "AES/" + mode.getMode() + "/" + padding.getPadding();

        // Validate the provider argument
        if (provider != null && provider.trim().isEmpty()) {
            throw new IllegalArgumentException("Provider cannot be an empty string.");
        }

        // Create the Cipher object
        final Cipher cipher = (provider == null) ?
                Cipher.getInstance(transformation) :
                Cipher.getInstance(transformation, provider);

        // Set the mode to encrypt or decrypt
        final int encryptMode = (encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE);

        // Initialize the Cipher based on the mode
        if (mode == AESMode.ECB) {
            cipher.init(encryptMode, key);
        } else if (mode == AESMode.GCM) {
            cipher.init(encryptMode, key, new GCMParameterSpec(128, ivSpec.getIV()));
        } else {
            cipher.init(encryptMode, key, ivSpec);
        }

        return cipher;
    }

    public static Cipher createCipher(final AESMode mode, final AESPadding padding, final SecretKey key,
                                      final IvParameterSpec ivSpec, final boolean encrypt)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidAlgorithmParameterException, NoSuchProviderException {

        return createCipher(mode, padding, key, ivSpec, null, encrypt);
    }

    public static byte[] decodeIv(final String ivBase64) {
        return Base64.getDecoder().decode(ivBase64);
    }

    public static String encodeIv(final byte[] iv) {
        return Base64.getEncoder().encodeToString(iv);
    }

    public enum AESMode {
        ECB("ECB", 0),
        CBC("CBC", 16),
        CFB("CFB", 16),
        OFB("OFB", 16),
        CTR("CTR", 16),
        GCM("GCM", 12);

        private final String mode;
        private final int ivSize;

        AESMode(final String mode, final int ivSize) {
            this.mode = mode;
            this.ivSize = ivSize;
        }

        public String getMode() {
            return this.mode;
        }

        public int getIvSize() {
            return this.ivSize;
        }
    }

    public enum AESPadding {
        NO_PADDING("NoPadding"),
        PKCS5_PADDING("PKCS5Padding"),
        PKCS7_PADDING("PKCS7Padding");

        private final String padding;

        AESPadding(final String padding) {
            this.padding = padding;
        }

        public String getPadding() {
            return this.padding;
        }
    }

    public enum AESKeySize {
        AES_128(128),
        AES_192(192),
        AES_256(256);

        private final int keySize;

        AESKeySize(final int keySize) {
            this.keySize = keySize;
        }

        public int getKeySize() {
            return this.keySize;
        }
    }
}