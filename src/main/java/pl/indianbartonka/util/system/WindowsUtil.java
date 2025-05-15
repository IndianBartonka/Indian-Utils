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
import pl.indianbartonka.util.annotation.UtilityClass;

@UtilityClass
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

        //TODO:Przetestować z systemem który ma dwa układy graficzne
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
}
