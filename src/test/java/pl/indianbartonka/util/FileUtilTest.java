package pl.indianbartonka.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


class FileUtilTest {

    private File testFile;

    @BeforeEach
    void setUp() {
        this.testFile = new File("test.txt");
        if (this.testFile.exists()) {
            this.testFile.delete();
        }
    }

    @AfterEach
    void tearDown() {
        if (this.testFile.exists()) {
            this.testFile.delete();
        }
    }

    @Test
    public void testWriteTextCreatesFile() throws IOException {
        FileUtil.writeText(this.testFile, Arrays.asList("Hello", "World"));
        Assertions.assertTrue(this.testFile.exists());
    }

    @Test
    public void testDirectoryIsEmptyTrue() {
        final File directory = new File("emptyDir");
        directory.mkdir();
        Assertions.assertTrue(FileUtil.directoryIsEmpty(directory));
        directory.delete();
    }

    @Test
    public void testDirectoryIsEmptyFalse() throws IOException {
        final File directory = new File("nonEmptyDir");
        directory.mkdir();
        new File(directory, "file.txt").createNewFile();
        assertFalse(FileUtil.directoryIsEmpty(directory));
        FileUtil.deleteFile(directory);
    }

    @Test
    public void testDeleteFile() throws IOException {
        FileUtil.writeText(this.testFile, List.of("Some content"));
        Assertions.assertTrue(this.testFile.exists());
        FileUtil.deleteFile(this.testFile);
        assertFalse(this.testFile.exists());
    }
}
