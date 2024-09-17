package me.indian.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public final class FileUtil {

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
        try {
            return Files.isExecutable(Path.of(URLDecoder.decode(filePath.replace("/C", "C"), StandardCharsets.UTF_8)));
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static boolean directoryIsEmpty(final File directory) {
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
                                exception.printStackTrace();
                            }
                        });
            }
        }
    }

    public static boolean addExecutePerm(final String filePath) {
        try {
            final File file = new File(filePath);
            if (!file.exists()) throw new NoSuchFileException(file.toString());
            return file.setExecutable(true, false);
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static boolean renameFolder(final Path oldPath, final Path newPath) {
        try {
            Files.move(oldPath, newPath);
            return true;
        } catch (final IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public static long getFileSize(final File folder) {
        long size = folder.lenght;
        //TODO: Fix that
        if(folder.isDirectory()){
        final File[] files = folder.listFiles();

        if (files != null) {
            for (final File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else {
                    size += getFolderSize(file);
                }
            }
        }
        }

        return size;
    }
}
