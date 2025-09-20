package pl.indianbartonka.util.system.parts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.system.SystemUtil;

public class Disk {

    private final String name;
    private final File diskFile;
    private final String type;
    private final long blockSize;
    private final boolean readOnly;
    @Since("0.0.9.5")
    private String model;

    public Disk(final String name, final File diskFile, final String type, final long blockSize, final boolean readOnly) {
        this.name = name;
        this.diskFile = diskFile;
        this.type = type;
        this.blockSize = blockSize;
        this.readOnly = readOnly;
    }

    public String getName() {
        return this.name;
    }

    public String getModel() {
        if (this.model != null) return this.model;

        this.model = switch (SystemUtil.getSystem()) {
            case WINDOWS -> getWindowsDiskModel(this.diskFile);
            case LINUX, FREE_BSD, MAC -> getLinuxDiskModel(this.diskFile);
            default -> "Unknown";
        };

        return this.model;
    }

    public File getDiskFile() {
        return this.diskFile;
    }

    public String getType() {
        return this.type;
    }

    public long getBlockSize() {
        return this.blockSize;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    private static String getWindowsDiskModel(final File diskFile) {
        String model = "UNKNOWN";
        final String diskLetter = diskFile.getPath().substring(0, 1);

        try {
            final Process process = Runtime.getRuntime().exec("powershell.exe -Command \"(Get-Partition -DriveLetter " + diskLetter + " | Get-Disk).Model\"");
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    model = line;
                }
            }

            process.waitFor();
        } catch (final IOException | InterruptedException ignored) {
        }

        return model;
    }

    private static String getLinuxDiskModel(final File diskFile) {
        String model = "UNKNOWN";

        try {
            final String[] cmd = {"/bin/bash", "-c",
                    "lsblk -no MODEL $(df " + diskFile.getAbsolutePath() + " | tail -1 | awk '{print $1}' | sed -E 's/p?[0-9]+$//')"
            };

            final Process process = Runtime.getRuntime().exec(cmd);

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        model = line;
                        break;
                    }
                }
            }

            process.waitFor();
        } catch (final IOException | InterruptedException ignored) {
        }

        return model;
    }

    @Override
    public String toString() {
        return "Disk{" +
                "name='" + this.name + '\'' +
                ", model='" + this.getModel() + '\'' +
                ", diskFile=" + this.diskFile +
                ", type='" + this.type + '\'' +
                ", blockSize=" + this.blockSize +
                ", readOnly=" + this.readOnly +
                '}';
    }
}
