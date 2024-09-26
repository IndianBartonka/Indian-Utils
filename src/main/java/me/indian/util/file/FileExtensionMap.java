package me.indian.util.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileExtensionMap {

    private final Map<String,String> extensionsMap;

    public FileExtensionMap() {
        this.extensionsMap = new HashMap<>();
        this.init();
    }

    public void addExtension(final String extensionName, final String description) {
        this.extensionsMap.putIfAbsent(extensionName.toLowerCase(), description);
    }

    public void addExtension(final List<String> extensions, final String description) {
        for (final String extensionName : extensions) {
            this.extensionsMap.putIfAbsent(extensionName.toLowerCase(), description);
        }
    }

    public Map<String, String> getExtensionsMap() {
        return this.extensionsMap;
    }

    private void init(){
        // Executable Files
        this.addExtension("exe", "Windows Executable");
        this.addExtension("msi", "Windows Installer Package");
        this.addExtension("scr", "Windows Screen Saver Executable");
        this.addExtension("out", "Java Executable");
        this.addExtension("jar", "Java Architecture File");
        this.addExtension("app", "macOS Executable");
        this.addExtension("pkg", "macOS Package");
        this.addExtension("elf", "Linux Executable");
        this.addExtension("deb", "Debian Package");
        this.addExtension("rpm", "RedHat Package");
        this.addExtension("apk", "Android Application Package");
        this.addExtension("ipa", "iOS App Store Package");
        this.addExtension("sys", "System File");
        this.addExtension("dll", "Dynamic Link Library File");

        // Markdown
        this.addExtension(List.of("md", "markdown", "mdown", "mkd", "mkdn", "mdtxt"), "Markdown File");

        // Text Files
        this.addExtension("txt", "Text File");
        this.addExtension("log", "Log File");
        this.addExtension("rtf", "Rich Text Format File");

        // Documents
        this.addExtension("odg", "Open Document Graphics File");
        this.addExtension("pdf", "PDF Document");
        this.addExtension("doc", "Microsoft Word Document");
        this.addExtension("docx", "Word Document");
        this.addExtension("odt", "Open Document Text File");
        this.addExtension("wps", "Microsoft Works Document");
        this.addExtension("html", "HTML Document");
        this.addExtension("htm", "HTML Document");
        this.addExtension("xhtml", "XHTML Document");
        this.addExtension("xls", "Excel Spreadsheet");
        this.addExtension("xlsx", "Excel Spreadsheet");
        this.addExtension("ppt", "PowerPoint Presentation");
        this.addExtension("pptx", "PowerPoint Presentation");

        // Minecraft
        this.addExtension("mcaddon", "Minecraft Addon");
        this.addExtension("mcpack", "Minecraft Resource/Behavior Pack");
        this.addExtension("mcmeta", "Minecraft Resource Pack Configuration File");
        this.addExtension("mcproject", "Minecraft Editor File");
        this.addExtension("mcstructure", "Minecraft Structure File");
        this.addExtension("mctemplate", "Minecraft World Template");
        this.addExtension("mcworld", "Minecraft World");

        // Data Files
        this.addExtension("json", "JSON File");
        this.addExtension("csv", "Comma-Separated Values File");
        this.addExtension("sql", "SQL File");
        this.addExtension("xml", "XML File");
        this.addExtension("xps", "XML-based Document");
        this.addExtension("db", "Database File");
        this.addExtension(List.of("dat", "data"), "Data File");
        this.addExtension("sqlite", "SQLite Database File");
        this.addExtension("ldb", "LevelDB File");
        this.addExtension("tmp", "Temporary Data File");
        this.addExtension("list", "List File");
        this.addExtension("resources", "Resource File");
        this.addExtension(List.of("asset", "assets"), "Asset File");
        this.addExtension("vdf", "Valve Data File");
        this.addExtension("pak", "Package File");
        this.addExtension("bundle", "Bundle File");
        this.addExtension("pdb", "Program Database File");
        this.addExtension("nbt", "NBT Data File");

        // Debug Files
        this.addExtension("debug", "Debug Information File");
        this.addExtension(List.of("dmp", "mdmp"), "Memory Dump File");
        this.addExtension("dbg", "Debug File");
        this.addExtension("sym", "Symbol File");
        this.addExtension("trace", "Trace File");
        this.addExtension("drw", "Debug Raw Data File");
        this.addExtension("etl", "Event Trace Log");
        this.addExtension("map", "Map File");
        this.addExtension("stacktrace", "Stack Trace File");
        this.addExtension("rpt", "Report File");
        this.addExtension("err", "Error Log File");
        this.addExtension("tdf", "Trace Data File");
        this.addExtension("trc", "Trace File");

        // Git Files
        this.addExtension(".gitattributes", "Git Attributes File");
        this.addExtension(".gitignore", "Git Ignore File");
        this.addExtension(".gitmodules", "Git Submodule File");
        this.addExtension("patch", "Patch File");

        // Images
        this.addExtension(List.of("jpg", "jpeg"), "JPEG Image");
        this.addExtension("png", "PNG Image");
        this.addExtension("gif", "GIF Image");
        this.addExtension("svg", "SVG Image");
        this.addExtension("webp", "WebP Image");
        this.addExtension("heic", "HEIC Image");
        this.addExtension("avif", "AVIF Image");
        this.addExtension("tga", "Targa Image File");
        this.addExtension("tiff", "Tagged Image File Format");
        this.addExtension("bmp", "Bitmap Image File");
        this.addExtension("ico", "Icon File");

        // Video Files
        this.addExtension("mp4", "MP4 Video");
        this.addExtension("avi", "AVI Video");
        this.addExtension("mkv", "MKV Video");
        this.addExtension("mov", "QuickTime Movie File");
        this.addExtension("webm", "WebM Video");
        this.addExtension("flv", "Flash Video File");
        this.addExtension("wmv", "Windows Media Video File");

        // Audio Files
        this.addExtension("mp3", "MP3 Audio");
        this.addExtension("ogg", "OGG Audio");
        this.addExtension("wav", "WAV Audio");
        this.addExtension("flac", "FLAC Audio");
        this.addExtension("m4a", "M4A Audio");
        this.addExtension("alac", "Apple Lossless Audio Codec");
        this.addExtension("fsb", "FSB Audio File");
        this.addExtension("aac", "Advanced Audio Codec");
        this.addExtension("wma", "Windows Media Audio File");
        this.addExtension("m4b", "M4B Audio Book File");

        // Archives
        this.addExtension("zip", "ZIP Archive");
        this.addExtension("rar", "RAR Archive");
        this.addExtension("tar", "TAR Archive");
        this.addExtension("gz", "GZ Compressed Archive");
        this.addExtension("tar.gz", "Gzipped Tar Archive");
        this.addExtension("tgz", "Gzipped Tar Archive");
        this.addExtension("xz", "XZ Compressed Archive");
        this.addExtension("bz2", "BZ2 Compressed Archive");
        this.addExtension("7z", "7-Zip Archive");
        this.addExtension("unity3d", "Unity3D Asset Archive");

        // Disk Images
        this.addExtension("iso", "ISO Disk Image");
        this.addExtension("img", "Disk Image");
        this.addExtension("dmg", "macOS Disk Image");
        this.addExtension("bin", "Binary File");
        this.addExtension("cue", "Cue Sheet");
        this.addExtension("mdf", "Media Descriptor File");
        this.addExtension("nrg", "Nero Disk Image");

        // 3D Model Files
        this.addExtension("obj", "Wavefront 3D Object File");
        this.addExtension("fbx", "Filmbox 3D Model File");
        this.addExtension("stl", "Stereolithography 3D Model File");
        this.addExtension("3ds", "3D Studio Model File");
        this.addExtension("blend", "Blender 3D Model File");
        this.addExtension("dae", "COLLADA 3D Model File");
        this.addExtension("bbmodel", "Blockbench Model File");
        this.addExtension("gltf", "GL Transmission Format Model File");
        this.addExtension("glb", "GLB Binary Format Model File");
        this.addExtension("usdz", "Universal Scene Description Zip File");

        // Backup Files
        this.addExtension("bak", "Backup File");
        this.addExtension("old", "Old File Version");

        //License files
        this.addExtension(List.of("license", "lic"), "License File");

        // Downloading Files
        this.addExtension("crdownload", "Chrome Downloading File");
        this.addExtension("part", "Partial Download File");
        this.addExtension("temp", "Temporary File");
        this.addExtension("download", "Firefox Downloading File");
        this.addExtension("partial", "Partial Download File");

        // Browser-Related Files
        this.addExtension("maff", "Mozilla Archive Format File");
        this.addExtension("webarchive", "Web Archive File");
        this.addExtension("mht", "MHTML File");

        // Configuration Files
        this.addExtension("lang", "Language File");
        this.addExtension(List.of("yaml", "yml"), "YAML File");
        this.addExtension("dconf", "Dconf Configuration File");
        this.addExtension("hocon", "Human-Optimized Config Object Notation File");
        this.addExtension("ini", "INI Configuration File");
        this.addExtension("properties", "Properties Configuration File");
        this.addExtension("props", "Properties Configuration File");
        this.addExtension("prop", "Properties Configuration File");
        this.addExtension("toml", "TOML Configuration File");
        this.addExtension(List.of("conf", "config", "cfg"), "Configuration File");
        this.addExtension("env", "Environment File");
        this.addExtension("plist", "macOS Property List File");
        this.addExtension("rc", "Resource Configuration File");
        this.addExtension("desktop", "Desktop Entry File");
        //TODO: Dowiedziec się
        this.addExtension("epage", "TellTale Save File");
        this.addExtension("estore", "TellTale Save File");

        // Scripts
        this.addExtension("sh", "Bash Script");
        this.addExtension("bash", "Bash Script");
        this.addExtension("run", "Executable Run Script");
        this.addExtension("bat", "Batch Script");
        this.addExtension("ps1", "PowerShell Script");
        this.addExtension("gradle", "Gradle Build Script");
        this.addExtension("gradlew", "Gradle Wrapper Script");
        this.addExtension("kts", "Kotlin Script for Gradle");
        this.addExtension("node", "Node.js Script");
        this.addExtension("py", "Python Script");
        this.addExtension("java", "Java Source File");
        this.addExtension("kt", "Kotlin Source File");
        this.addExtension("scala", "Scala Source File");
        this.addExtension("groovy", "Groovy Script");
        this.addExtension("class", "Java Bytecode Class File");
        this.addExtension("jsp", "JavaServer Pages File");
        this.addExtension("rb", "Ruby Script");
        this.addExtension("go", "Go Source File");
        this.addExtension("js", "JavaScript File");
        this.addExtension("ts", "TypeScript File");
        this.addExtension("php", "PHP Script");
        this.addExtension("css", "CSS File");
        this.addExtension("rs", "Rust Source File");
        this.addExtension("d", "D Source File");
        this.addExtension("clj", "Clojure Source File");
        this.addExtension("pl", "Perl Script");
        this.addExtension("r", "R Script");
        this.addExtension("swift", "Swift Source File");
        this.addExtension("dart", "Dart Source File");
        this.addExtension("lua", "Lua Script");
        this.addExtension("v", "V Source File");
        this.addExtension("awk", "AWK Script");
        this.addExtension("cpp", "C++ Source File");
        this.addExtension("c", "C Source File");
        this.addExtension("h", "C/C++ Header File");
        this.addExtension("cs", "C# Source File");
        this.addExtension("fs", "F# Source File");
        this.addExtension("fsx", "F# Script File");
        this.addExtension("fsi", "F# Interface File");
        this.addExtension("jsx", "React JSX File");
        this.addExtension("tsx", "React TypeScript File");
        this.addExtension("scss", "Sass Stylesheet");
        this.addExtension("less", "Less Stylesheet");
        this.addExtension("tex", "LaTeX Document");
        this.addExtension("rkt", "Racket Source File");
        this.addExtension("erl", "Erlang Source File");
        this.addExtension("ex", "Elixir Source File");
        this.addExtension("exs", "Elixir Script");
    }
}