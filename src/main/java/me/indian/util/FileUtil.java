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
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
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
        return Files.isExecutable(Path.of(URLDecoder.decode(filePath.replace("/C", "C"), StandardCharsets.UTF_8)));
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

    public static String getFileOwner(final File file) throws IOException {
        final FileOwnerAttributeView view = Files.getFileAttributeView(file.toPath(), FileOwnerAttributeView.class);
        return view.getOwner().getName();
    }

    public static long getCreationTime(final File file) throws IOException {
        final BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        return attrs.creationTime().toMillis();
    }

    public static String getFileExtensionInfo(final String fileName) {
        final int dotIndex = fileName.lastIndexOf('.');

        final String fileExtension = ((dotIndex > 0) ? fileName.substring(dotIndex + 1) : "Unknown");
        return switch (fileExtension.toLowerCase()) {
            case "exe" -> "Aplikacja";
            case "md" -> "MarkDown";
            case "txt" -> "Text File";
            case "json" -> "Json File";
            case "jpg", "jpeg" -> "JPEG Image";
            case "png" -> "PNG Image";
            case "gif" -> "GIF Image";
            case "svg" -> "SVG Image";
            case "mp4" -> "MP4 Video";
            case "avi" -> "AVI Video";
            case "mkv" -> "MKV Video";
            case "mp3" -> "MP3 Audio";
            case "wav" -> "WAV Audio";
            case "flac" -> "FLAC Audio";
            case "pdf" -> "PDF Document";
            case "docx" -> "Word Document";
            case "xlsx" -> "Excel Spreadsheet";
            case "zip" -> "ZIP Archive";
            case "rar" -> "RAR Archive";
            case "tar" -> "TAR Archive";
            case "gz" -> "GZ Compressed Archive";
            case "csv" -> "CSV File";
            case "sql" -> "SQL File";
            case "xml" -> "XML File";
            case "pptx" -> "PowerPoint Presentation";
            case "sh" -> "Bash Script";
            case "ps1" -> "PowerShell Script";
            case "yaml", "yml" -> "YAML File";
            case "ini" -> "INI Configuration File";
            case "bz2" -> "BZ2 Compressed Archive";
            case "java" -> "Java Source Code";
            case "py" -> "Python Source Code";
            case "html" -> "HTML Document";

            default -> fileExtension;
        };
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
}