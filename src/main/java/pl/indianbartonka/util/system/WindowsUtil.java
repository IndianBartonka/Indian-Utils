package pl.indianbartonka.util.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.VisibleForTesting;
import pl.indianbartonka.util.IndianUtils;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.annotation.UtilityClass;
import pl.indianbartonka.util.system.parts.Disk;
import pl.indianbartonka.util.system.parts.Ram;

@UtilityClass
@Since("0.0.9.3")
public final class WindowsUtil {

    private WindowsUtil() {

    }

    public static String getWiFiSSID() throws IOException {
        final Process process = new ProcessBuilder("powershell.exe", "Get-NetConnectionProfile").start();

        try (final BufferedReader reader = new BufferedReader(process.inputReader())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("Name")) {
                    final String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        return parts[1].trim();
                    }
                }
            }
        }
        return "UNKNOWN";
    }

    public static String getProcessorName() throws IOException {
        final Process process = new ProcessBuilder("powershell.exe", "-Command", "Get-CimInstance", "Win32_Processor", "|", "Select-Object", "-ExpandProperty", "Name").start();

        try (final BufferedReader bufferedReader = new BufferedReader(process.inputReader())) {
            return bufferedReader.readLine();
        }
    }

    public static List<String> getGraphicCardsName() throws IOException {
        final Process process = new ProcessBuilder("powershell.exe", "-Command", "Get-CimInstance", "Win32_VideoController", "|", "Select-Object", "-ExpandProperty", "Name").start();

        final List<String> graphicCards = new ArrayList<>();
        try (final BufferedReader bufferedReader = new BufferedReader(process.inputReader())) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                graphicCards.add(line.trim());
            }
        }

        return graphicCards;
    }

    @VisibleForTesting
    public static List<Disk> getAvailableDisks() {
        final List<Disk> disks = new LinkedList<>();

        for (final File diskFile : File.listRoots()) {
            String name = diskFile.getPath().replaceAll(":", "").replaceAll("[/\\\\]", "").trim();
            String type = "UNKNOWN";
            boolean readOnly = false;
            long blockSize = -1;

            try {
                final FileStore store = Files.getFileStore(diskFile.toPath());
                final String diskName = store.name();

                if (!diskName.isEmpty()) {
                    name = diskName;
                }

                type = store.type();
                readOnly = store.isReadOnly();
                blockSize = store.getBlockSize();

            } catch (final IOException ioException) {
                //Dla debugu takiego bo nie wiem kiedy to moze wystapic
                if (IndianUtils.debug) ioException.printStackTrace();
            }

            disks.add(new Disk(name, diskFile, type, blockSize, readOnly));
        }

        return disks;
    }

    public static long getMemoryUsage(final long pid) throws IOException {
        final Process process = Runtime.getRuntime().exec("tasklist /NH /FI \"PID eq " + pid + "\"");
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(".exe")) {
                    final String[] tokens = line.split("\\s+");
                    if (tokens.length > 4) {
                        final String memoryStr = tokens[4].replaceAll("\\D", "");
                        return Long.parseLong(memoryStr);
                    }
                }
            }
        }
        return -1;
    }

    @Since("0.0.9.3")
    public static List<Ram> getRamData() throws IOException {
        final List<String> lines = new ArrayList<>();
        final List<Ram> ram = new ArrayList<>();

        final String psScript = "Get-CimInstance Win32_PhysicalMemory | ForEach-Object { " +
                "Write-Output ('Size : ' + $_.Capacity); " +
                "Write-Output ('BasicSpeed : ' + $_.Speed); " +
                "Write-Output ('ConfiguredSpeed : ' + $_.ConfiguredClockSpeed); " +
                "Write-Output ('MemoryType : ' + $_.SMBIOSMemoryType); " +
                "Write-Output ('PartNumber : ' + $_.PartNumber); " +
                "Write-Output ('BankLabel : ' + $_.BankLabel); " +
                "Write-Output ''; " +
                "}";

        final Process process = new ProcessBuilder("powershell.exe", "-Command", psScript).start();

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    ram.add(ramParser(lines));
                    lines.clear();
                } else {
                    lines.add(line);
                }
            }
        }

        return ram;
    }

    private static Ram ramParser(final List<String> lines) {
        long size = -1;
        long basicSpeed = -1;
        long configuredSpeed = -1;
        String memoryType = "";
        String partNumber = "";
        String bankLabel = "";

        for (final String line : lines) {
            if (line.contains("Size")) {
                final String[] parts = line.split(":");
                size = Long.parseLong(parts[1].trim());
            }

            if (line.contains("BasicSpeed")) {
                final String[] parts = line.split(":");
                basicSpeed = Long.parseLong(parts[1].trim());
            }

            if (line.contains("ConfiguredSpeed")) {
                final String[] parts = line.split(":");
                configuredSpeed = Long.parseLong(parts[1].trim());
            }

            if (line.contains("MemoryType")) {
                final String[] parts = line.split(":");
                memoryType = switch (Integer.parseInt(parts[1].trim())) {
                    case 20 -> "DDR";
                    case 21 -> "DDR2";
                    case 22 -> "DDR2 FB-DIMM";
                    case 24 -> "DDR3";
                    case 26 -> "DDR4";
                    case 27 -> "DDR5";
                    default -> "Unknown";
                };
            }

            if (line.contains("PartNumber")) {
                final String[] parts = line.split(":");
                partNumber = parts[1].trim();
            }

            if (line.contains("BankLabel")) {
                final String[] parts = line.split(":");
                bankLabel = parts[1].trim();
            }
        }

        return new Ram(size, basicSpeed, configuredSpeed, memoryType, partNumber, bankLabel);
    }
}