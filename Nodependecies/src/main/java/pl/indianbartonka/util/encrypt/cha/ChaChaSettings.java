package pl.indianbartonka.util.encrypt.cha;

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
import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class ChaChaSettings {

    public static SecretKey generateKey() {
        try {
            final KeyGenerator keyGen = KeyGenerator.getInstance("ChaCha20");
            keyGen.init(256);
            return keyGen.generateKey();
        } catch (final NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String encodeKey(final SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static SecretKey decodeKey(final String base64Key) {
        return new SecretKeySpec(Base64.getDecoder().decode(base64Key), "ChaCha20");
    }

    public static IvParameterSpec generateIV(final ChaChaMode mode) {
        final byte[] iv = new byte[mode.getIvSize()];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static Cipher createCipher(final ChaChaMode mode,
                                      final SecretKey key,
                                      final IvParameterSpec ivSpec,
                                      final String provider,
                                      final boolean encrypt)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidAlgorithmParameterException, NoSuchProviderException {

        final String transformation = mode.getTransformation();

        final Cipher cipher = (provider == null) ?
                Cipher.getInstance(transformation) :
                Cipher.getInstance(transformation, provider);

        final int cipherMode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;


        if (mode == ChaChaMode.CHACHA20) {
            cipher.init(cipherMode, key, new ChaCha20ParameterSpec(ivSpec.getIV(), 1));
        } else {
            cipher.init(cipherMode, key, ivSpec);
        }

        return cipher;
    }

    public static byte[] decodeIv(final String ivBase64) {
        return Base64.getDecoder().decode(ivBase64);
    }

    public static String encodeIv(final byte[] iv) {
        return Base64.getEncoder().encodeToString(iv);
    }

    public enum ChaChaMode {
        CHACHA20("ChaCha20", 12),
        CHACHA20_POLY1305("ChaCha20-Poly1305", 12);

        private final String transformation;
        private final int ivSize;

        ChaChaMode(final String transformation, final int ivSize) {
            this.transformation = transformation;
            this.ivSize = ivSize;
        }

        public String getTransformation() {
            return this.transformation;
        }

        public int getIvSize() {
            return this.ivSize;
        }
    }
}

