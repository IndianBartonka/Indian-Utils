package pl.indianbartonka.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class FileUtil {

    private static final FileExtensionMap FILE_EXTENSION_MAP = new FileExtensionMap();
    private static final Map<String, String> EXTENSIONS_MAP = FILE_EXTENSION_MAP.getExtensionsMap();

    private FileUtil() {
    }

    public static void writeText(final File file, final List<String> lines, final boolean removeOld) throws IOException {
        final LinkedList<String> currentLines;

        if (file.exists()) {
            if (removeOld) {
                currentLines = new LinkedList<>(lines);
            } else {
                currentLines = new LinkedList<>();
                currentLines.addAll(lines);
                currentLines.addAll(Files.readAllLines(file.toPath()));
            }
        } else {
            if (!file.createNewFile()) throw new FileNotFoundException(file.getName());
            currentLines = new LinkedList<>(lines);
        }

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (final String line : currentLines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static void writeText(final File file, final List<String> lines) throws IOException {
        writeText(file, lines, true);
    }

    public static boolean canExecute(final String filePath) {
        return Files.isExecutable(Path.of(URLDecoder.decode(filePath.replace("/C", "C"), StandardCharsets.UTF_8)));
    }

    public static boolean directoryIsEmpty(final File directory) {
        if (!directory.exists()) return false;
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Provided file is not a directory.");
        }

        final File[] files = directory.listFiles();
        if (files == null) return true;

        for (final File file : files) {
            if (file.isDirectory()) {
                if (!directoryIsEmpty(file)) return false;
            } else {
                return false;
            }
        }

        return true;
    }

    public static void deleteFile(final File file) throws IOException {
        try {
            Files.delete(file.toPath());
        } catch (final IOException ioException) {
            try (final Stream<Path> stream = Files.walk(file.toPath())) {
                stream.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (final IOException exception) {
                                throw new UncheckedIOException(exception);
                            }
                        });
            }
        }
    }

    public static String getFileOwner(final File file) throws IOException {
        final FileOwnerAttributeView view = Files.getFileAttributeView(file.toPath(), FileOwnerAttributeView.class);
        return view.getOwner().getName();
    }

    public static long getCreationTime(final File file) throws IOException {
        final BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        return attrs.creationTime().toMillis();
    }

    public static boolean addExecutePerm(final String filePath) throws NoSuchFileException {
        final File file = new File(filePath);
        if (!file.exists()) throw new NoSuchFileException(file.toString());
        return file.setExecutable(true, false);
    }

    public static void renameFolder(final Path oldPath, final Path newPath) throws IOException {
        Files.move(oldPath, newPath);
    }

    public static long getFileSize(final File file) {
        long size = file.length();

        if (file.isDirectory()) {
            final File[] files = file.listFiles();

            if (files != null) {
                for (final File file1 : files) {
                    if (file1.isFile()) {
                        size += file1.length();
                    } else {
                        size += getFileSize(file1);
                    }
                }
            }
        }

        return size;
    }

    public static String getFileTypeInfo(final String fileName) {
        final int dotIndex = fileName.lastIndexOf('.');
        final String fileExtension = ((dotIndex > 0) ? fileName.substring(dotIndex + 1) : fileName);

        return EXTENSIONS_MAP.getOrDefault(fileExtension.toLowerCase(), fileExtension);
    }

    public static void addExtensionName(final String extensionName, final String description) {
        FILE_EXTENSION_MAP.addExtension(extensionName, description);
    }

    public static void addExtensionName(final List<String> extensions, final String description) {
        FILE_EXTENSION_MAP.addExtension(extensions, description);
    }
}