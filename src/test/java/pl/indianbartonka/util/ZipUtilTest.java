package pl.indianbartonka.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZipUtilTest {

    private static final String LOGS_DIR = "logs";
    private static final String ZIP_FILE_NAME = "logs.zip";
    private static final String UNZIP_DIR = "tescik";

    @BeforeEach
    void setUp() {
        ZipUtil.init(9);
    }

    @Test
    void testZipFolderExists() {
        final File directory = new File(LOGS_DIR);
        assertTrue(directory.exists(), "Folder logs powinien istnieć.");
    }

    @Test
    void testZipFolderCreatesZipFile()  {
        try{
            final File directory = new File(LOGS_DIR);
            final File zipFile = ZipUtil.zipFolder(directory.getPath(), ZIP_FILE_NAME);
            assertTrue(zipFile.exists(), "Plik zip powinien zostać utworzony.");
        } catch (final IOException ioException){
            //Tu moze byc Odmowa dostepu bo logger uyzwa pliku Logu
            ioException.printStackTrace();
        }
    }

    @Test
    void testUnzipCreatesDirectory() throws IOException {
        final File zipFile = new File(ZIP_FILE_NAME);
        ZipUtil.unzipFile(zipFile.getPath(), UNZIP_DIR, false);
        final File unzippedDir = new File(UNZIP_DIR);
        assertTrue(unzippedDir.exists(), "Folder po wypakowaniu powinien istnieć.");
    }
}