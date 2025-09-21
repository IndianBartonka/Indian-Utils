package pl.indianbartonka.util.system.parts;

import java.io.File;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.system.LinuxUtil;
import pl.indianbartonka.util.system.SystemUtil;
import pl.indianbartonka.util.system.WindowsUtil;

public class Disk {

    private final String name;
    private final File diskFile;
    private final String fileSystem;
    private final long blockSize;
    private final boolean readOnly;
    @Since("0.0.9.5")
    private String model;
    @Since("0.0.9.5")
    private String type;

    public Disk(final String name, final File diskFile, final String fileSystem, final long blockSize, final boolean readOnly) {
        this.name = name;
        this.diskFile = diskFile;
        this.fileSystem = fileSystem;
        this.blockSize = blockSize;
        this.readOnly = readOnly;
    }

    public String getName() {
        return this.name;
    }

    public String getModel() {
        if (this.model != null) return this.model;

        this.model = switch (SystemUtil.getSystem()) {
            case WINDOWS -> WindowsUtil.getDiskModel(this.diskFile);
            case LINUX, FREE_BSD, MAC -> LinuxUtil.getDiskModel(this.diskFile);
            default -> "Unknown";
        };

        return this.model;
    }

    public String getType() {
        if (this.type != null) return this.type;

        this.type = switch (SystemUtil.getSystem()) {
            case WINDOWS -> WindowsUtil.getDiskType(this.diskFile);
            case LINUX, FREE_BSD, MAC -> LinuxUtil.getDiskType(this.diskFile);
            default -> "Unknown";
        };

        return this.type;
    }

    public File getDiskFile() {
        return this.diskFile;
    }

    public String getFileSystem() {
        return this.fileSystem;
    }

    public long getBlockSize() {
        return this.blockSize;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

//TODO: Move some methods to WindowsUtil and Linux Util

    @Override
    public String toString() {
        return "Disk{" +
                "name='" + this.name + '\'' +
                ", model='" + this.getModel() + '\'' +
                ", diskFile=" + this.diskFile +
                ", fileSystem='" + this.fileSystem + '\'' +
                ", blockSize=" + this.blockSize +
                ", readOnly=" + this.readOnly +
                '}';
    }
}
