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

    private static final File FILE = new File(File.separator);

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

    public static long availableDiskSpace() {
        return (FILE.exists() ? FILE.getUsableSpace() : 0);
    }

    public static long maxDiskSpace() {
        return (FILE.exists() ? FILE.getTotalSpace() : 0);
    }

    public static long usedDiskSpace() {
        return (maxDiskSpace() - availableDiskSpace());
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
                                throw new RuntimeException(exception);
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

    public static String getFileType(final String fileName) {
        final int dotIndex = fileName.lastIndexOf('.');

        final String fileExtension = ((dotIndex > 0) ? fileName.substring(dotIndex + 1) : fileName);
        return switch (fileExtension.toLowerCase()) {
            // Executable Files
            case "exe" -> "Windows Executable";
            case "msi" -> "Windows Installer Package";
            case "scr" -> "Windows Screen Saver Executable";
            case "out" -> "Java Executable";
            case "jar" -> "Java Architecture File";
            case "app" -> "macOS Executable";
            case "pkg" -> "macOS Package";
            case "elf" -> "Linux Executable";
            case "deb" -> "Debian Package";
            case "rpm" -> "RedHat Package";
            case "apk" -> "Android Application Package";
            case "ipa" -> "iOS App Store Package";
            case "sys" -> "System File";
            case "dll" -> "Dynamic Link Library File";

            // Markdown
            case "md", "markdown", "mdown", "mkd", "mkdn", "mdtxt" -> "Markdown File";

            // Text Files
            case "txt" -> "Text File";
            case "log" -> "Log File";
            case "rtf" -> "Rich Text Format File";

            // Documents
            case "pdf" -> "PDF Document";
            case "doc" -> "Microsoft Word Document";
            case "docx" -> "Word Document";
            case "odt" -> "Open Document Text File";
            case "wps" -> "Microsoft Works Document";
            case "html", "htm" -> "HTML Document";
            case "xhtml" -> "XHTML Document";
            case "xlsx" -> "Excel Spreadsheet";
            case "pptx" -> "PowerPoint Presentation";

            // Minecraft
            case "mcaddon" -> "Minecraft Addon";
            case "mcpack" -> "Minecraft Resource/Behavior Pack";
            case "mcmeta" -> "Minecraft Resource Pack Configuration File";
            case "mcproject" -> "Minecraft Editor File";
            case "mcstructure" -> "Minecraft Structure File";
            case "mctemplate" -> "Minecraft World Template";
            case "mcworld" -> "Minecraft World";

            // Data Files
            case "json" -> "JSON File";
            case "csv" -> "CSV File";
            case "sql" -> "SQL File";
            case "xml" -> "XML File";
            case "xps" -> "XML-based Document";
            case "db" -> "Database File";
            case "dat", "data" -> "Data File";
            case "sqlite" -> "SQLite Database File";
            case "ldb" -> "LevelDB File";
            case "tmp" -> "Temporary Data File";
            case "list" -> "List File";
            case "resources" -> "Resource File";
            case "asset", "assets" -> "Asset File";
            case "vdf" -> "Valve Data File";
            case "pak" -> "Package File";
            case "bundle" -> "Bundle File";
            case "pdb" -> "Program Database File";

            // Git Files
            case "git" -> "Git Directory";
            case "gitattributes" -> "Git Attributes File";
            case "gitignore" -> "Git Ignore File";
            case "gitmodules" -> "Git Submodule File";
            case "patch" -> "Patch File";

            // Images
            case "jpg", "jpeg" -> "JPEG Image";
            case "png" -> "PNG Image";
            case "gif" -> "GIF Image";
            case "svg" -> "SVG Image";
            case "webp" -> "WebP Image";
            case "heic" -> "HEIC Image";
            case "avif" -> "AVIF Image";

            // Video Files
            case "mp4" -> "MP4 Video";
            case "avi" -> "AVI Video";
            case "mkv" -> "MKV Video";
            case "mov" -> "QuickTime Movie File";
            case "webm" -> "WebM Video";

            // Audio Files
            case "mp3" -> "MP3 Audio";
            case "ogg" -> "OGG Audio";
            case "wav" -> "WAV Audio";
            case "flac" -> "FLAC Audio";
            case "m4a" -> "M4A Audio";
            case "alac" -> "Apple Lossless Audio Codec";

            // Archives
            case "zip" -> "ZIP Archive";
            case "rar" -> "RAR Archive";
            case "tar" -> "TAR Archive";
            case "gz" -> "GZ Compressed Archive";
            case "bz2" -> "BZ2 Compressed Archive";
            case "7z" -> "7-Zip Archive";
            case "unity3d" -> "Unity3D Asset Archive";

            // Disk Images
            case "iso" -> "ISO Disk Image";
            case "img" -> "Disk Image";
            case "dmg" -> "macOS Disk Image";
            case "bin" -> "Binary File";
            case "cue" -> "Cue Sheet";
            case "mdf" -> "Media Descriptor File";
            case "nrg" -> "Nero Disk Image";

            // 3D Model Files
            case "obj" -> "Wavefront 3D Object File";
            case "fbx" -> "Filmbox 3D Model File";
            case "stl" -> "Stereolithography 3D Model File";
            case "3ds" -> "3D Studio Model File";
            case "blend" -> "Blender 3D Model File";
            case "dae" -> "COLLADA 3D Model File";
            case "bbmodel" -> "Blockbench Model File";
            case "gltf" -> "GL Transmission Format Model File";
            case "glb" -> "GLB Binary Format Model File";
            case "usdz" -> "Universal Scene Description Zip File";

            // Backup Files
            case "bak" -> "Backup File";
            case "old" -> "Old File Version";

            // Downloading Files
            case "crdownload" -> "Chrome Downloading File";
            case "part" -> "Partial Download File";
            case "temp" -> "Temporary File";
            case "download" -> "Firefox Downloading File";
            case "partial" -> "Partial Download File";

            // Browser-Related Files
            case "maff" -> "Mozilla Archive Format File";
            case "webarchive" -> "Web Archive File";
            case "mht" -> "MHTML File";

            // Configuration Files
            case "lang" -> "Language File";
            case "yaml", "yml" -> "YAML File";
            case "ini" -> "INI Configuration File";
            case "properties", "props", "prop" -> "Properties Configuration File";
            case "toml" -> "TOML Configuration File";
            case "conf", "config", "cfg" -> "Configuration File";
            case "env" -> "Environment File";
            case "plist" -> "macOS Property List File";
            case "rc" -> "Resource Configuration File";
            case "desktop" -> "Desktop Entry File";
            //TODO: Dowiedziec się
            case "epage" -> "TellTale Save File";
            case "estore" -> "TellTale Save File";

            // Scripts
            case "sh" -> "Bash Script";
            case "bash" -> "Bash Script";
            case "bat" -> "Batch Script";
            case "ps1" -> "PowerShell Script";
            case "gradle" -> "Gradle Build Script";
            case "gradlew" -> "Gradle Wrapper Script";
            case "kts" -> "Kotlin Script for Gradle";
            case "node" -> "Node.js Script";
            case "py" -> "Python Script";
            case "java" -> "Java Source File";
            case "kt" -> "Kotlin Source File";
            case "scala" -> "Scala Source File";
            case "groovy" -> "Groovy Script";
            case "class" -> "Java Bytecode Class File";
            case "jsp" -> "JavaServer Pages File";
            case "rb" -> "Ruby Script";
            case "go" -> "Go Source File";
            case "js" -> "JavaScript File";
            case "ts" -> "TypeScript File";
            case "php" -> "PHP Script";
            case "css" -> "CSS File";
            case "rs" -> "Rust Source File";
            case "d" -> "D Source File";
            case "clj" -> "Clojure Source File";
            case "pl" -> "Perl Script";
            case "r" -> "R Script";
            case "swift" -> "Swift Source File";
            case "dart" -> "Dart Source File";
            case "lua" -> "Lua Script";
            case "v" -> "V Source File";
            case "awk" -> "AWK Script";
            case "cpp" -> "C++ Source File";
            case "c" -> "C Source File";
            case "h" -> "C/C++ Header File";
            case "cs" -> "C# Source File";
            case "fs" -> "F# Source File";
            case "fsx" -> "F# Script File";
            case "fsi" -> "F# Interface File";
            case "jsx" -> "React JSX File";
            case "tsx" -> "React TypeScript File";
            case "scss" -> "Sass Stylesheet";
            case "less" -> "Less Stylesheet";
            case "tex" -> "LaTeX Document";
            case "rkt" -> "Racket Source File";
            case "erl" -> "Erlang Source File";
            case "ex" -> "Elixir Source File";
            case "exs" -> "Elixir Script";

            default -> fileExtension;
        };
    }
}
