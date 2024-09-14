package me.indian.util.encrypt;

import java.io.File;
import java.io.IOException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import me.indian.util.logger.Logger;

public interface Encryptor {

    File encryptFile(File inputFile, SecretKey key) throws Exception;

    File decryptFile(File inputFile, SecretKey key) throws Exception;

    String encryptText(String text, SecretKey key) throws Exception;

    String decryptText(String encryptedText, SecretKey key) throws Exception;

    void setLogger(Logger logger);

    IvParameterSpec getIvParameterSpec();

    void setIvParameterSpec(IvParameterSpec ivParameterSpec);

    void setEncryptedDir(String encryptedDir) throws IOException;

    void setUserDir(String userDir) throws IOException;
}