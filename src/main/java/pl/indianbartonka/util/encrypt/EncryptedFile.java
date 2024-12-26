package pl.indianbartonka.util.encrypt;

import java.io.File;

public record EncryptedFile(long encryptTime, long fileSize, File encryptedFile, String encryptionAlgorithm) {

}
