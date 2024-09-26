package me.indian.util.file;

import java.util.HashMap;
import java.util.Map;

public class FileExtensionMap {

    private final Map<String,String> extensionsMap;

    public FileExtensionMap() {
        this.extensionsMap = new HashMap<>();
        this.init();
    }

    public Map<String, String> getExtensionsMap() {
        return this.extensionsMap;
    }

    private void init(){
        // Executable Files
        this.extensionsMap.put("exe", "Windows Executable");
        this.extensionsMap.put("msi", "Windows Installer Package");
        this.extensionsMap.put("scr", "Windows Screen Saver Executable");
        this.extensionsMap.put("out", "Java Executable");
        this.extensionsMap.put("jar", "Java Architecture File");
        this.extensionsMap.put("app", "macOS Executable");
        this.extensionsMap.put("pkg", "macOS Package");
        this.extensionsMap.put("elf", "Linux Executable");
        this.extensionsMap.put("deb", "Debian Package");
        this.extensionsMap.put("rpm", "RedHat Package");
        this.extensionsMap.put("apk", "Android Application Package");
        this.extensionsMap.put("ipa", "iOS App Store Package");
        this.extensionsMap.put("sys", "System File");
        this.extensionsMap.put("dll", "Dynamic Link Library File");

        // Markdown
        this.extensionsMap.put("md", "Markdown File");
        this.extensionsMap.put("markdown", "Markdown File");
        this.extensionsMap.put("mdown", "Markdown File");
        this.extensionsMap.put("mkd", "Markdown File");
        this.extensionsMap.put("mkdn", "Markdown File");
        this.extensionsMap.put("mdtxt", "Markdown File");

        // Text Files
        this.extensionsMap.put("txt", "Text File");
        this.extensionsMap.put("log", "Log File");
        this.extensionsMap.put("rtf", "Rich Text Format File");

        // Documents
        this.extensionsMap.put("odg", "Open Document Graphics File");
        this.extensionsMap.put("pdf", "PDF Document");
        this.extensionsMap.put("doc", "Microsoft Word Document");
        this.extensionsMap.put("docx", "Word Document");
        this.extensionsMap.put("odt", "Open Document Text File");
        this.extensionsMap.put("wps", "Microsoft Works Document");
        this.extensionsMap.put("html", "HTML Document");
        this.extensionsMap.put("htm", "HTML Document");
        this.extensionsMap.put("xhtml", "XHTML Document");
        this.extensionsMap.put("xls", "Excel Spreadsheet");
        this.extensionsMap.put("xlsx", "Excel Spreadsheet");
        this.extensionsMap.put("ppt", "PowerPoint Presentation");
        this.extensionsMap.put("pptx", "PowerPoint Presentation");

        // Minecraft
        this.extensionsMap.put("mcaddon", "Minecraft Addon");
        this.extensionsMap.put("mcpack", "Minecraft Resource/Behavior Pack");
        this.extensionsMap.put("mcmeta", "Minecraft Resource Pack Configuration File");
        this.extensionsMap.put("mcproject", "Minecraft Editor File");
        this.extensionsMap.put("mcstructure", "Minecraft Structure File");
        this.extensionsMap.put("mctemplate", "Minecraft World Template");
        this.extensionsMap.put("mcworld", "Minecraft World");

        // Data Files
        this.extensionsMap.put("json", "JSON File");
        this.extensionsMap.put("csv", "Comma-Separated Values File");
        this.extensionsMap.put("sql", "SQL File");
        this.extensionsMap.put("xml", "XML File");
        this.extensionsMap.put("xps", "XML-based Document");
        this.extensionsMap.put("db", "Database File");
        this.extensionsMap.put("dat", "Data File");
        this.extensionsMap.put("data", "Data File");
        this.extensionsMap.put("sqlite", "SQLite Database File");
        this.extensionsMap.put("ldb", "LevelDB File");
        this.extensionsMap.put("tmp", "Temporary Data File");
        this.extensionsMap.put("list", "List File");
        this.extensionsMap.put("resources", "Resource File");
        this.extensionsMap.put("asset", "Asset File");
        this.extensionsMap.put("assets", "Asset File");
        this.extensionsMap.put("vdf", "Valve Data File");
        this.extensionsMap.put("pak", "Package File");
        this.extensionsMap.put("bundle", "Bundle File");
        this.extensionsMap.put("pdb", "Program Database File");
        this.extensionsMap.put("nbt", "NBT Data File");

        // Debug Files
        this.extensionsMap.put("debug", "Debug Information File");
        this.extensionsMap.put("dmp", "Memory Dump File");
        this.extensionsMap.put("mdmp", "Memory Dump File");
        this.extensionsMap.put("dbg", "Debug File");
        this.extensionsMap.put("sym", "Symbol File");
        this.extensionsMap.put("trace", "Trace File");
        this.extensionsMap.put("drw", "Debug Raw Data File");
        this.extensionsMap.put("etl", "Event Trace Log");
        this.extensionsMap.put("map", "Map File");
        this.extensionsMap.put("stacktrace", "Stack Trace File");
        this.extensionsMap.put("rpt", "Report File");
        this.extensionsMap.put("err", "Error Log File");
        this.extensionsMap.put("tdf", "Trace Data File");
        this.extensionsMap.put("trc", "Trace File");

        // Git Files
        this.extensionsMap.put("git", "Git Directory");
        this.extensionsMap.put("gitattributes", "Git Attributes File");
        this.extensionsMap.put("gitignore", "Git Ignore File");
        this.extensionsMap.put("gitmodules", "Git Submodule File");
        this.extensionsMap.put("patch", "Patch File");

        // Images
        this.extensionsMap.put("jpg", "JPEG Image");
        this.extensionsMap.put("jpeg", "JPEG Image");
        this.extensionsMap.put("png", "PNG Image");
        this.extensionsMap.put("gif", "GIF Image");
        this.extensionsMap.put("svg", "SVG Image");
        this.extensionsMap.put("webp", "WebP Image");
        this.extensionsMap.put("heic", "HEIC Image");
        this.extensionsMap.put("avif", "AVIF Image");
        this.extensionsMap.put("tga", "Targa Image File");
        this.extensionsMap.put("tiff", "Tagged Image File Format");
        this.extensionsMap.put("bmp", "Bitmap Image File");
        this.extensionsMap.put("ico", "Icon File");

        // Video Files
        this.extensionsMap.put("mp4", "MP4 Video");
        this.extensionsMap.put("avi", "AVI Video");
        this.extensionsMap.put("mkv", "MKV Video");
        this.extensionsMap.put("mov", "QuickTime Movie File");
        this.extensionsMap.put("webm", "WebM Video");
        this.extensionsMap.put("flv", "Flash Video File");
        this.extensionsMap.put("wmv", "Windows Media Video File");

        // Audio Files
        this.extensionsMap.put("mp3", "MP3 Audio");
        this.extensionsMap.put("ogg", "OGG Audio");
        this.extensionsMap.put("wav", "WAV Audio");
        this.extensionsMap.put("flac", "FLAC Audio");
        this.extensionsMap.put("m4a", "M4A Audio");
        this.extensionsMap.put("alac", "Apple Lossless Audio Codec");
        this.extensionsMap.put("fsb", "FSB Audio File");
        this.extensionsMap.put("aac", "Advanced Audio Codec");
        this.extensionsMap.put("wma", "Windows Media Audio File");
        this.extensionsMap.put("m4b", "M4B Audio Book File");

        // Archives
        this.extensionsMap.put("zip", "ZIP Archive");
        this.extensionsMap.put("rar", "RAR Archive");
        this.extensionsMap.put("tar", "TAR Archive");
        this.extensionsMap.put("gz", "GZ Compressed Archive");
        this.extensionsMap.put("tar.gz", "Gzipped Tar Archive");
        this.extensionsMap.put("tgz", "Gzipped Tar Archive");
        this.extensionsMap.put("xz", "XZ Compressed Archive");
        this.extensionsMap.put("bz2", "BZ2 Compressed Archive");
        this.extensionsMap.put("7z", "7-Zip Archive");
        this.extensionsMap.put("unity3d", "Unity3D Asset Archive");

        // Disk Images
        this.extensionsMap.put("iso", "ISO Disk Image");
        this.extensionsMap.put("img", "Disk Image");
        this.extensionsMap.put("dmg", "macOS Disk Image");
        this.extensionsMap.put("bin", "Binary File");
        this.extensionsMap.put("cue", "Cue Sheet");
        this.extensionsMap.put("mdf", "Media Descriptor File");
        this.extensionsMap.put("nrg", "Nero Disk Image");

        // 3D Model Files
        this.extensionsMap.put("obj", "Wavefront 3D Object File");
        this.extensionsMap.put("fbx", "Filmbox 3D Model File");
        this.extensionsMap.put("stl", "Stereolithography 3D Model File");
        this.extensionsMap.put("3ds", "3D Studio Model File");
        this.extensionsMap.put("blend", "Blender 3D Model File");
        this.extensionsMap.put("dae", "COLLADA 3D Model File");
        this.extensionsMap.put("bbmodel", "Blockbench Model File");
        this.extensionsMap.put("gltf", "GL Transmission Format Model File");
        this.extensionsMap.put("glb", "GLB Binary Format Model File");
        this.extensionsMap.put("usdz", "Universal Scene Description Zip File");

        // Backup Files
        this.extensionsMap.put("bak", "Backup File");
        this.extensionsMap.put("old", "Old File Version");

        // Downloading Files
        this.extensionsMap.put("crdownload", "Chrome Downloading File");
        this.extensionsMap.put("part", "Partial Download File");
        this.extensionsMap.put("temp", "Temporary File");
        this.extensionsMap.put("download", "Firefox Downloading File");
        this.extensionsMap.put("partial", "Partial Download File");

        // Browser-Related Files
        this.extensionsMap.put("maff", "Mozilla Archive Format File");
        this.extensionsMap.put("webarchive", "Web Archive File");
        this.extensionsMap.put("mht", "MHTML File");

        // Configuration Files
        this.extensionsMap.put("lang", "Language File");
        this.extensionsMap.put("yaml", "YAML File");
        this.extensionsMap.put("yml", "YAML File");
        this.extensionsMap.put("dconf", "Dconf Configuration File");
        this.extensionsMap.put("hocon", "Human-Optimized Config Object Notation File");
        this.extensionsMap.put("ini", "INI Configuration File");
        this.extensionsMap.put("properties", "Properties Configuration File");
        this.extensionsMap.put("props", "Properties Configuration File");
        this.extensionsMap.put("prop", "Properties Configuration File");
        this.extensionsMap.put("toml", "TOML Configuration File");
        this.extensionsMap.put("conf", "Configuration File");
        this.extensionsMap.put("config", "Configuration File");
        this.extensionsMap.put("cfg", "Configuration File");
        this.extensionsMap.put("env", "Environment File");
        this.extensionsMap.put("plist", "macOS Property List File");
        this.extensionsMap.put("rc", "Resource Configuration File");
        this.extensionsMap.put("desktop", "Desktop Entry File");
        //TODO: Dowiedziec się
        this.extensionsMap.put("epage", "TellTale Save File");
        this.extensionsMap.put("estore", "TellTale Save File");

        // Scripts
        this.extensionsMap.put("sh", "Bash Script");
        this.extensionsMap.put("bash", "Bash Script");
        this.extensionsMap.put("run", "Executable Run Script");
        this.extensionsMap.put("bat", "Batch Script");
        this.extensionsMap.put("ps1", "PowerShell Script");
        this.extensionsMap.put("gradle", "Gradle Build Script");
        this.extensionsMap.put("gradlew", "Gradle Wrapper Script");
        this.extensionsMap.put("kts", "Kotlin Script for Gradle");
        this.extensionsMap.put("node", "Node.js Script");
        this.extensionsMap.put("py", "Python Script");
        this.extensionsMap.put("java", "Java Source File");
        this.extensionsMap.put("kt", "Kotlin Source File");
        this.extensionsMap.put("scala", "Scala Source File");
        this.extensionsMap.put("groovy", "Groovy Script");
        this.extensionsMap.put("class", "Java Bytecode Class File");
        this.extensionsMap.put("jsp", "JavaServer Pages File");
        this.extensionsMap.put("rb", "Ruby Script");
        this.extensionsMap.put("go", "Go Source File");
        this.extensionsMap.put("js", "JavaScript File");
        this.extensionsMap.put("ts", "TypeScript File");
        this.extensionsMap.put("php", "PHP Script");
        this.extensionsMap.put("css", "CSS File");
        this.extensionsMap.put("rs", "Rust Source File");
        this.extensionsMap.put("d", "D Source File");
        this.extensionsMap.put("clj", "Clojure Source File");
        this.extensionsMap.put("pl", "Perl Script");
        this.extensionsMap.put("r", "R Script");
        this.extensionsMap.put("swift", "Swift Source File");
        this.extensionsMap.put("dart", "Dart Source File");
        this.extensionsMap.put("lua", "Lua Script");
        this.extensionsMap.put("v", "V Source File");
        this.extensionsMap.put("awk", "AWK Script");
        this.extensionsMap.put("cpp", "C++ Source File");
        this.extensionsMap.put("c", "C Source File");
        this.extensionsMap.put("h", "C/C++ Header File");
        this.extensionsMap.put("cs", "C# Source File");
        this.extensionsMap.put("fs", "F# Source File");
        this.extensionsMap.put("fsx", "F# Script File");
        this.extensionsMap.put("fsi", "F# Interface File");
        this.extensionsMap.put("jsx", "React JSX File");
        this.extensionsMap.put("tsx", "React TypeScript File");
        this.extensionsMap.put("scss", "Sass Stylesheet");
        this.extensionsMap.put("less", "Less Stylesheet");
        this.extensionsMap.put("tex", "LaTeX Document");
        this.extensionsMap.put("rkt", "Racket Source File");
        this.extensionsMap.put("erl", "Erlang Source File");
        this.extensionsMap.put("ex", "Elixir Source File");
        this.extensionsMap.put("exs", "Elixir Script");
    }
}
