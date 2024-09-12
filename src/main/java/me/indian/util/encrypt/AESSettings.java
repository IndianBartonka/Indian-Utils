package me.indian.util.encrypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    public enum AESMode {
        ECB("ECB", 0),
        CBC("CBC", 16), // Typowa długość IV dla CBC, CFB, OFB
        CFB("CFB", 16),
        OFB("OFB", 16),
        CTR("CTR", 16),
        GCM("GCM", 12); // Typowa długość IV dla GCM

        private final String mode;
        private final int ivSize;

        AESMode(final String mode, final int ivSize) {
            this.mode = mode;
            this.ivSize = ivSize;
        }

        public static int getPlace(final AESMode aesMode) {
            int counter = 0;
            for (final AESMode mode : values()) {
                if (mode == aesMode) return counter;
                counter++;
            }

            return counter;
        }

        public String getMode() {
            return this.mode;
        }

        public int getIvSize() {
            return this.ivSize;
        }
    }

    public enum AESPadding {
        NoPadding("NoPadding"),
        PKCS5Padding("PKCS5Padding"),
        PKCS7Padding("PKCS7Padding");

        private final String padding;

        AESPadding(final String padding) {
            this.padding = padding;
        }

        public static int getPlace(final AESPadding padding) {
            int counter = 0;
            for (final AESPadding padding1 : values()) {
                if (padding1 == padding) return counter;
                counter++;
            }

            return counter;
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

    public static SecretKey generateKey(final AESKeySize keySize) {
        final KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGen.init(keySize.getKeySize());
        return keyGen.generateKey();
    }

    public static String encodeKey(final SecretKey key) {
        final byte[] keyBytes = key.getEncoded();
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static SecretKey decodeKey(final String base64Key) {
        final byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static IvParameterSpec generateIV(final AESMode mode) {
        final byte[] iv = new byte[mode.getIvSize()];
        final SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static Cipher createCipher(final AESMode mode, final AESPadding padding, final SecretKey key, final IvParameterSpec ivSpec, final boolean encrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        final String transformation = "AES/" + mode.getMode() + "/" + padding.getPadding();
        final Cipher cipher = Cipher.getInstance(transformation);
        final int encryptMode = (encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE);

        if (mode == AESMode.ECB) {
            cipher.init(encryptMode, key);
        } else if (mode == AESMode.GCM) {
            final GCMParameterSpec gcmSpec = new GCMParameterSpec(128, ivSpec.getIV()); // Długość tagu 128-bitowego
            cipher.init(encryptMode, key, gcmSpec);
        } else {
            cipher.init(encryptMode, key, ivSpec);
        }
        return cipher;
    }
}
