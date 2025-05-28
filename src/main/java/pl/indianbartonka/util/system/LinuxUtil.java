package pl.indianbartonka.util.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.VisibleForTesting;
import pl.indianbartonka.util.IndianUtils;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.annotation.UtilityClass;

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
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
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
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
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
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            final String line = reader.readLine();
            return (line != null && !line.trim().isEmpty()) ? line.trim() : "Nie znaleziono nazwy procesora";
        }
    }

    public static List<String> getGraphicCardsName() throws IOException {
        final List<String> cards = new ArrayList<>();

        //TODO: Dodać wasparcie dla zintegrowanych układów graficznych
        final Process process = new ProcessBuilder("sh", "-c", "lspci | grep -i 'vga\\|3d\\|2d' | cut -d ':' -f3").start();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
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

    public static long getMemoryUsage(final long pid) throws IOException {
        final Process process = Runtime.getRuntime().exec("ps -p " + pid + " -o rss=");

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
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
}