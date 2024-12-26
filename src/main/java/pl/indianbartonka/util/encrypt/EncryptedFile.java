package pl.indianbartonka.util.encrypt;

import java.io.File;

public record EncryptedFile(long encryptMillisTime, long fileSize, File encryptedFile, String encryptionAlgorithm) {

}
