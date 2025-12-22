package pl.indianbartonka.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.jetbrains.annotations.Nullable;
import pl.indianbartonka.util.annotation.UtilityClass;
import pl.indianbartonka.util.logger.Logger;

/**
 * <p>
 * Utility class for creating and extracting ZIP files.
 * Provides methods to zip folders and files, and to unzip files.
 * </p>
 * <p>
 * Documents written by ChatGPT
 * </p>
 */
@UtilityClass
public final class ZipUtil {

    private static Logger logger;
    private static int compressionLevel = 5;

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private ZipUtil() {
    }

    /**
     * Initializes the ZipUtil class with a logger instance and sets the compression level.
     *
     * @param parent           The Logger instance used for logging, can be null.
     * @param compressionLevel The compression level to be used, ranging from 0 (no compression) to 9 (maximum compression).
     *                         If the specified level is outside this range, it will be adjusted to fit within the limits.
     */
    public static void init(final @Nullable Logger parent, final int compressionLevel) {
        if (parent != null) {
            ZipUtil.logger = parent.prefixed("ZipUtil");
        }
        ZipUtil.compressionLevel = MathUtil.getCorrectNumber(compressionLevel, 0, 9);
    }

    /**
     * Initializes the ZipUtil class with a default logger and sets the compression level.
     *
     * @param compressionLevel The compression level to be used, ranging from 0 to 9.
     */
    public static void init(final int compressionLevel) {
        init(null, compressionLevel);
    }


    /**
     * Zips a folder and all its contents into a ZIP file.
     *
     * @param sourceFolderPath The path to the folder to be zipped.
     * @param zipFilePath      The path where the ZIP file will be created.
     * @throws IOException              If an error occurs during the zipping process.
     * @throws IllegalArgumentException If the source folder is empty.
     */
    public static File zipFolder(final String sourceFolderPath, final String zipFilePath) throws IOException {
        final File sourceFolder = new File(sourceFolderPath);
        final File zipFile = new File(zipFilePath);

        if (FileUtil.directoryIsEmpty(sourceFolder)) {
            throw new IllegalArgumentException("You can't pack empty folders.");
        }

        try (final FileOutputStream fos = new FileOutputStream(zipFile);
             final ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            zipOut.setLevel(compressionLevel);
            addDirectoryToZip(sourceFolder, sourceFolder.getName(), zipOut);
        }

        return zipFile;
    }

    /**
     * Returns the compression level used in zip operations.
     *
     * <p>
     * This method allows users to obtain the current compression level
     * that is used for compressing files and folders. The compression level
     * can be set in the range from 0 (no compression) to 9 (maximum
     * compression).
     * </p>
     *
     * @return The compression level used in zip operations.
     * @see #init(Logger, int)
     */
    public static int getCompressionLevel() {
        return compressionLevel;
    }

    /**
     * Adds a directory and its contents to the ZIP output stream.
     *
     * @param folder     The directory to add.
     * @param parentName The name of the parent directory in the ZIP file.
     * @param zos        The ZipOutputStream to write to.
     * @throws IOException If an error occurs during the zipping process.
     */
    private static void addDirectoryToZip(final File folder, final String parentName, final ZipOutputStream zos) throws IOException {
        if (logger != null) logger.debug("Packing: " + folder.getPath());

        final File[] files = folder.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory()) {
                    addDirectoryToZip(file, parentName + File.separator + file.getName(), zos);
                } else {
                    addFileToZip(file, parentName, zos);
                }
            }
        }
    }

    /**
     * Unzips a ZIP file to the specified target directory.
     *
     * @param zipFilePath     The path to the ZIP file to be extracted.
     * @param targetDirectory The directory where the contents will be extracted.
     * @param deleteOnEnd     Whether to delete the ZIP file after extraction.
     * @throws IOException If an error occurs during the extraction process.
     */
    public static void unzipFile(final String zipFilePath, final String targetDirectory, final boolean deleteOnEnd) throws IOException {
        unzipFile(zipFilePath, targetDirectory, deleteOnEnd, null);
    }

    /**
     * Unzips a ZIP file to the specified target directory, with options to skip certain files and delete the ZIP file after extraction.
     *
     * @param zipFilePath     The path to the ZIP file to be extracted.
     * @param targetDirectory The directory where the contents will be extracted.
     * @param deleteOnEnd     Whether to delete the ZIP file after extraction.
     * @param skipFiles       List of file paths to skip during extraction.
     * @throws IOException If an error occurs during the extraction process.
     */
    public static void unzipFile(final String zipFilePath, final String targetDirectory, final boolean deleteOnEnd, final List<String> skipFiles) throws IOException {
        final Path path = Path.of(zipFilePath);
        createDirectoryIfNotExists(Path.of(targetDirectory));

        try (final ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(path))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                final String entryName = zipEntry.getName().replace("/", File.separator);
                final File outputFile = new File(targetDirectory + File.separator + entryName);

                //  Skip files specified in the skipFiles list
                if (outputFile.exists() && skipFiles != null && skipFiles.contains(outputFile.getAbsolutePath())) {
                    if (logger != null) logger.info("Skipping file: " + outputFile.getAbsolutePath());
                    continue;
                }

                if (logger != null) logger.debug("Extracting: " + outputFile.getAbsolutePath());

                if (zipEntry.isDirectory()) {
                    createDirectoryIfNotExists(outputFile.toPath());
                } else {
                    final File parentDir = outputFile.getParentFile();
                    if (parentDir != null && !parentDir.exists()) {
                        createDirectoryIfNotExists(parentDir.toPath());
                    }

                    try (final FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                        final byte[] buffer = new byte[BufferUtil.calculateOptimalBufferSize(zipEntry.getSize())];
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }
            }

            if (deleteOnEnd) {
                try {
                    Files.deleteIfExists(path);
                } catch (final Exception exception) {
                    FileUtil.deleteFile(path.toFile());
                }
            }
        }
    }

    /**
     * Adds a file to the ZIP output stream with a specified folder name.
     *
     * @param file       The file to add.
     * @param folderName The name of the folder in the ZIP file.
     * @param zos        The ZipOutputStream to write to.
     * @throws IOException If an error occurs during the zipping process.
     */
    private static void addFileToZip(final File file, final String folderName, final ZipOutputStream zos) throws IOException {
        if (logger != null) logger.debug("Packing: " + file.getPath());

        final byte[] buffer = new byte[BufferUtil.calculateOptimalBufferSize(file.length())];

        try (final FileInputStream fis = new FileInputStream(file)) {
            final ZipEntry zipEntry = new ZipEntry(folderName.replace("\\", "/") + "/" + file.getName());

            zos.putNextEntry(zipEntry);
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }

    /**
     * Creates a directory if it does not already exist.
     *
     * @param path The path to the directory.
     * @throws IOException If an error occurs during directory creation.
     */
    private static void createDirectoryIfNotExists(final Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }
}

