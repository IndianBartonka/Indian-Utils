package pl.indianbartonka.util.system.parts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import pl.indianbartonka.util.IndianUtils;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.system.SystemUtil;

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
            case WINDOWS -> getWindowsDiskModel(this.diskFile);
            case LINUX, FREE_BSD, MAC -> getLinuxDiskModel(this.diskFile);
            default -> "Unknown";
        };

        return this.model;
    }

    public String getType() {
        if (this.type != null) return this.type;

        this.type = switch (SystemUtil.getSystem()) {
            case WINDOWS -> getWindowsDiskType(this.diskFile);
            case LINUX, FREE_BSD, MAC -> getLinuxDiskType(this.diskFile);
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
        } catch (final IOException | InterruptedException exception) {
            if (IndianUtils.debug) {
                exception.printStackTrace();
            }
        }

        return model;
    }

    private static String getWindowsDiskType(final File diskFile) {
        String type = "UNKNOWN";
        final String diskLetter = diskFile.getPath().substring(0, 1);

        try {
            final String command = """
                    $disk = Get-Partition -DriveLetter <LETTER> | Get-Disk
                    Get-PhysicalDisk | Where-Object { $_.DeviceId -eq $disk.Number } | Select-Object -ExpandProperty MediaType
                    """.replace("<LETTER>", diskLetter);

            final Process process = new ProcessBuilder("powershell.exe", "-Command", command).start();

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                final String line = reader.readLine();

                if (line != null && !line.isEmpty()) type = line;
            }

            process.waitFor();
        } catch (final IOException | InterruptedException exception) {
            if (IndianUtils.debug) {
                exception.printStackTrace();
            }
        }

        return type;
    }

    //Writen witch ChatGPT
    private static String getLinuxDiskType(final File diskFile) {
        String type = "UNKNOWN";

        try {
            final Process psychicalDisk = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "basename $(df " + diskFile.getAbsolutePath() + " | tail -1 | awk '{print $1}' | sed -E 's/p?[0-9]+$//')"}
            );

            final String disk;
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(psychicalDisk.getInputStream()))) {
                disk = reader.readLine();
            }

            psychicalDisk.waitFor();

            if (disk != null && !disk.isEmpty()) {
                final Process diskType = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "cat /sys/block/" + disk.trim() + "/queue/rotational"});

                final String rotational;

                try (final BufferedReader r2 = new BufferedReader(new InputStreamReader(diskType.getInputStream()))) {
                    rotational = r2.readLine();
                }

                diskType.waitFor();

                if (rotational != null) {
                    type = "0".equals(rotational.trim()) ? "SSD" : "HDD";
                }
            }
        } catch (final IOException | InterruptedException exception) {
            if (IndianUtils.debug) {
                exception.printStackTrace();
            }
        }
        return type;
    }

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
