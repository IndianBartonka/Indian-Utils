package pl.indianbartonka.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.file.FileUtil;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testDiskSpaceLogging() {
        final long availableSpace = FileUtil.availableDiskSpace();
        final long usedSpace = FileUtil.usedDiskSpace();
        final long maxSpace = FileUtil.maxDiskSpace();

        System.out.println("Dostępne: " + MathUtil.formatBytesDynamic(availableSpace, false));
        System.out.println("Użyte: " + MathUtil.formatBytesDynamic(usedSpace, false));
        System.out.println("Maksymalne: " + MathUtil.formatBytesDynamic(maxSpace, false));

        assertTrue(availableSpace >= 0);
        assertTrue(usedSpace >= 0);
    }

    @Test
    void testWriteTextCreatesFile() throws IOException {
        FileUtil.writeText(this.testFile, Arrays.asList("Hello", "World"));
        assertTrue(this.testFile.exists());
    }

    @Test
    void testDirectoryIsEmptyTrue() {
        final File directory = new File("emptyDir");
        directory.mkdir();
        assertTrue(FileUtil.directoryIsEmpty(directory));
        directory.delete();
    }

    @Test
    void testDirectoryIsEmptyFalse() throws IOException {
        final File directory = new File("nonEmptyDir");
        directory.mkdir();
        new File(directory, "file.txt").createNewFile();
        assertFalse(FileUtil.directoryIsEmpty(directory));
        FileUtil.deleteFile(directory);
    }

    @Test
    void testDeleteFile() throws IOException {
        FileUtil.writeText(this.testFile, List.of("Some content"));
        assertTrue(this.testFile.exists());
        FileUtil.deleteFile(this.testFile);
        assertFalse(this.testFile.exists());
    }
}
