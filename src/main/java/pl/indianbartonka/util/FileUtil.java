package pl.indianbartonka.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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
        return Files.isExecutable(Path.of(URLDecoder.decode(filePath.replace("/C", "C"), StandardCharsets.UTF_8)));
    }

    public static boolean addExecutePerm(final String filePath) {
        final File file = new File(filePath);
        return file.setExecutable(true, false);
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

    public static List<File> listAllFiles(final File file) {
        final File[] files = file.listFiles();

        if (files == null) return new ArrayList<>();

        return listAllFiles(Arrays.asList(files));
    }

    public static List<File> listAllFiles(final List<File> files) {
        return files.stream()
                .flatMap(file -> {
                    final List<File> filesList = new ArrayList<>();
                    filesList.add(file);
                    if (file.isDirectory()) {
                        final File[] fileFiles = file.listFiles();
                        if (fileFiles != null) {
                            filesList.addAll(listAllFiles(Arrays.asList(fileFiles)));
                        }
                    }
                    return filesList.stream();
                }).collect(Collectors.toList());
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
}
