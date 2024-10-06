package pl.indianbartonka.util.encrypt;

import java.io.File;

public class EncryptedFile {

    private final long encryptTime;
    private final long fileSize;
    private final File encryptedFile;
    private final String encryptionAlgorithm;

    public EncryptedFile(final long encryptTime, final long fileSize, final File encryptedFile, final String encryptionAlgorithm) {
        this.encryptTime = encryptTime;
        this.fileSize = fileSize;
        this.encryptedFile = encryptedFile;
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public long getEncryptTime() {
        return this.encryptTime;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public File getEncryptedFile() {
        return this.encryptedFile;
    }

    public String getEncryptionAlgorithm() {
        return this.encryptionAlgorithm;
    }
}
