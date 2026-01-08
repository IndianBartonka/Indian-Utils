package pl.indianbartonka.util.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.VisibleForTesting;
import pl.indianbartonka.util.IndianUtils;
import pl.indianbartonka.util.MemoryUnit;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.annotation.UtilityClass;
import pl.indianbartonka.util.system.parts.Disk;
import pl.indianbartonka.util.system.parts.Ram;

@UtilityClass
@Since("0.0.9.3")
public final class LinuxUtil {

    private LinuxUtil() {

    }

    public static String getWiFiSSID() {
        final String nm = getNmWiFiSSID();

        if (nm.equals("preconfigured") || nm.equals("UNKNOWN")) {
            return getWpaWiFiSSID();
        } else {
            return nm;
        }
    }

    private static String getWpaWiFiSSID() {
        try {
            final Process process = Runtime.getRuntime().exec("wpa_cli status");
             try (final BufferedReader reader = process.inputReader()) {
                String ssid;
                while ((ssid = reader.readLine()) != null) {
                    if (ssid.contains("ssid=") && !ssid.contains("bssid=")) {
                        return ssid.substring(5);
                    }
                }
            }
        } catch (final IOException ignored) {

        }
        return "UNKNOWN";
    }

    private static String getNmWiFiSSID() {
        try {
            final Process process = Runtime.getRuntime().exec("nmcli -t -f name connection show --active");
             try (final BufferedReader reader = process.inputReader()) {
                String ssid;
                while ((ssid = reader.readLine()) != null) {
                    if (!ssid.equalsIgnoreCase("lo") && !ssid.isEmpty()) return ssid;
                }
            }
        } catch (final IOException ignored) {

        }
        return "UNKNOWN";
    }

    public static String getProcessorName() throws IOException {
        final Process process = new ProcessBuilder("sh", "-c", "grep -m 1 'Model\\|Hardware\\|model name' /proc/cpuinfo | cut -d ':' -f2 | xargs").start();
         try (final BufferedReader reader = process.inputReader()) {
            final String line = reader.readLine();
            return (line != null && !line.trim().isEmpty()) ? line.trim() : "Nie znaleziono nazwy procesora";
        }
    }

    public static List<String> getGraphicCardsName() throws IOException {
        final List<String> cards = new ArrayList<>();

        //TODO: Dodać wasparcie dla zintegrowanych układów graficznych
        final Process process = new ProcessBuilder("sh", "-c", "lspci | grep -i 'vga\\|3d\\|2d' | cut -d ':' -f3").start();
         try (final BufferedReader reader = process.inputReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                cards.add(line.trim());
            }
        }

        if (cards.isEmpty()) {
            cards.add("Brak Dedykowanych kart graficznych");
        }

        return cards;
    }

    @VisibleForTesting
    public static List<Disk> getAvailableDisks() {
        final List<Disk> disks = new LinkedList<>();

        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/mounts"))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String[] parts = line.split(" ");
                final String mountPoint = parts[1].replaceAll("\\\\040", " ");

                if (parts[0].startsWith("/dev/")) {
                    final File diskFile = new File(mountPoint);

                    final List<String> nameList = List.of(mountPoint.split("/"));

                    final String name;
                    if (nameList.isEmpty()) {
                        name = mountPoint;
                    } else {
                        name = nameList.get(nameList.size() - 1);
                    }

                    String type = "UNKNOWN";
                    boolean readOnly = false;
                    long blockSize = -1;

                    try {
                        final FileStore store = Files.getFileStore(diskFile.toPath());

                        type = store.type();
                        readOnly = store.isReadOnly();
                        blockSize = store.getBlockSize();

                    } catch (final IOException ioException) {
                        //Dla debugu takiego bo nie wiem kiedy to moze wystapic
                        if (IndianUtils.debug) ioException.printStackTrace();
                    }

                    disks.add(new Disk(name, diskFile, type, blockSize, readOnly));
                }
            }
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
        return disks;
    }

    public static String getDiskModel(final File diskFile) {
        String model = "UNKNOWN";

        try {
            final String[] cmd = {"/bin/bash", "-c",
                    "lsblk -no MODEL $(df " + diskFile.getAbsolutePath() + " | tail -1 | awk '{print $1}' | sed -E 's/p?[0-9]+$//')"
            };

            final Process process = Runtime.getRuntime().exec(cmd);

             try (final BufferedReader reader = process.inputReader()) {
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
            if (IndianUtils.debug) exception.printStackTrace();

        }

        return model;
    }

    //Writen witch ChatGPT
    public static String getDiskType(final File diskFile) {
        String type = "UNKNOWN";

        try {
            final Process psychicalDisk = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "basename $(df " + diskFile.getAbsolutePath() + " | tail -1 | awk '{print $1}' | sed -E 's/p?[0-9]+$//')"}
            );

            final String disk;
            try (final BufferedReader reader = psychicalDisk.inputReader()) {
                disk = reader.readLine();
            }

            psychicalDisk.waitFor();

            if (disk != null && !disk.isEmpty()) {
                final Process diskType = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "cat /sys/block/" + disk.trim() + "/queue/rotational"});

                final String rotational;

                try (final BufferedReader r2 = diskType.inputReader()) {
                    rotational = r2.readLine();
                }

                diskType.waitFor();

                if (rotational != null) {
                    type = "0".equals(rotational.trim()) ? "SSD" : "HDD";
                }
            }
        } catch (final IOException | InterruptedException exception) {
            if (IndianUtils.debug) exception.printStackTrace();

        }
        return type;
    }

    public static long getMemoryUsage(final long pid) throws IOException {
        final Process process = Runtime.getRuntime().exec("ps -p " + pid + " -o rss=");

         try (final BufferedReader reader = process.inputReader()) {
            //Robie tak na wszelki wypadek gdyby jakis system mial jakis niepotrzebny header w tym poleceniu
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    return Long.parseLong(line);
                } catch (final NumberFormatException ignored) {

                }
            }
        }

        return -1;
    }

    @Since("0.0.9.5")
    public static List<Ram> getRamData() {
        final List<String> lines = new ArrayList<>();
        final List<Ram> ramList = new ArrayList<>();

        try {
            final Process process = new ProcessBuilder("dmidecode", "--type", "17").start();

             try (final BufferedReader reader = process.inputReader()) {
                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    if (line.isEmpty()) {
                            final Ram ramStick = ramParser(lines);

                            if (ramStick.basicSpeed() != -1) {
                                ramList.add(ramStick);
                            }

                            lines.clear();
                    } else {
                        lines.add(line);
                    }
                }
            }
        } catch (final IOException  ioException) {
            if (IndianUtils.debug) ioException.printStackTrace();
        }

        return ramList;
    }

    private static Ram ramParser(final List<String> lines) {
        long size = -1;
        long basicSpeed = -1;
        long configuredSpeed = -1;
        String memoryType = "";
        String partNumber = "";
        String bankLabel = "";

        for (final String line : lines) {

            if (line.trim().startsWith("Size:")) {
                try {
                    final String[] parts = line.split(":");
                    size = Long.parseLong(parts[1].trim().split(" ")[0].trim());

                    final String[] parts2 = parts[1].trim().split(" ");

                    if (parts2.length >= 2) {
                        final String unit = parts2[1].trim();

                        if (unit.equalsIgnoreCase("GB")) {
                            size = MemoryUnit.GIBIBYTES.to(size, MemoryUnit.BYTES);
                        } else if (unit.equalsIgnoreCase("MB")) {
                            size = MemoryUnit.MEBIBYTES.to(size, MemoryUnit.BYTES);
                        }
                    }
                } catch (final NumberFormatException numberFormatException) {
                    if (IndianUtils.debug) numberFormatException.printStackTrace();
                }
            }

            if (line.contains("Speed")) {
                try {
                final String[] parts = line.split(":");
                basicSpeed = Long.parseLong(parts[1].replaceAll("MT/s", "").trim());
                } catch (final NumberFormatException ignored) {
                }
            }

            if (line.contains("Configured Memory Speed")) {
                try {
                final String[] parts = line.split(":");
                configuredSpeed = Long.parseLong(parts[1].replaceAll("MT/s", "").trim());
                } catch (final NumberFormatException ignored) {
                }
            }

            if (line.contains("Type:")) {
                try {
                final String[] parts = line.split(":");
                memoryType = parts[1].trim();
                } catch (final NumberFormatException ignored) {
                }
            }

            if (line.contains("Part Number")) {
                try {
                final String[] parts = line.split(":");
                partNumber = parts[1].trim();
                } catch (final NumberFormatException ignored) {
                }
            }

            if (line.contains("Bank Locator")) {
                try {
                final String[] parts = line.split(":");
                bankLabel = parts[1].trim();
                } catch (final NumberFormatException ignored) {
                }
            }
        }

        return new Ram(size, basicSpeed, configuredSpeed, memoryType, partNumber, bankLabel);
    }
}