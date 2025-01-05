package pl.indianbartonka.util.system;

import com.sun.management.OperatingSystemMXBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.management.ManagementFactory;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.VisibleForTesting;
import pl.indianbartonka.util.IndianUtils;
import pl.indianbartonka.util.annotation.UtilityClass;
import pl.indianbartonka.util.exception.UnsupportedSystemException;

@UtilityClass
public final class SystemUtil {

    public static final Locale LOCALE = Locale.getDefault();

    private SystemUtil() {
    }

    public static SystemOS getSystem() {
        final String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return SystemOS.WINDOWS;
        } else if (os.contains("nux")) {
            return SystemOS.LINUX;
        } else if (os.contains("bsd")) {
            return SystemOS.FREE_BSD;
        } else if (os.contains("mac")) {
            return SystemOS.MAC;
        } else {
            if (!getDistribution().equals("Unknown")) {
                return SystemOS.LINUX;
            } else {
                return SystemOS.UNKNOWN;
            }
        }
    }

    public static SystemFamily getSystemFamily() {
        return switch (getSystem()) {
            case WINDOWS -> SystemFamily.WINDOWS;
            case LINUX, FREE_BSD, MAC -> SystemFamily.UNIX;
            case UNKNOWN -> {
                if (!getDistribution().equals("Unknown")) {
                    yield SystemFamily.UNIX;
                } else {
                    yield SystemFamily.UNKNOWN;
                }
            }
        };
    }

    public static String getFullyOSName() {
        return System.getProperty("os.name");
    }

    public static String getFullOSNameWithDistribution() {
        return switch (getSystemFamily()) {
            case WINDOWS, UNKNOWN -> getFullyOSName();
            case UNIX -> {
                if (getSystem() == SystemOS.MAC) {
                    yield getFullyOSName();
                } else {
                    yield getFullyOSName() + " (" + getDistribution() + ")";
                }
            }
        };
    }

    public static String getOSVersion() {
        return System.getProperty("os.version");
    }

    public static SystemArch getCurrentArch() {
        return switch (System.getProperty("os.arch")) {
            case "amd64", "x86_64" -> SystemArch.AMD_X64;
            case "x86", "i386", "i486", "i586", "i686" -> SystemArch.AMD_X32;
            case "aarch64" -> SystemArch.ARM_64X;
            case "arm", "arm32", "armv7", "armv8" -> SystemArch.ARM_32X;
            default -> SystemArch.UNKNOWN;
        };
    }

    public static String getFullyArchCode() {
        return System.getProperty("os.arch");
    }

    public static String getDistribution() {
        try {
            final List<String> lines = Files.readAllLines(Paths.get("/etc/os-release"));

            for (final String line : lines) {
                if (line.startsWith("PRETTY_NAME=")) {
                    return line.substring(12, line.length() - 1).replaceAll("\"", "");
                }
            }
        } catch (final IOException ignored) {
        }

        return "Unknown";
    }

    //Nie wiadomo czy działa to dobrze na mac
    public static long getRamUsageByPid(final long pid) {
        try {
            return switch (getSystemFamily()) {
                case WINDOWS -> getWindowsMemoryUsage(pid);
                case UNIX -> getLinuxMemoryUsage(pid);
                default -> throw new UnsupportedSystemException("Pozyskiwanie ilosci ram dla " + getFullyOSName() + " nie jest jeszcze zaimplementowane");
            };
        } catch (final IOException ioException) {
            throw new UnsupportedSystemException("Pozyskiwanie ilosci ram dla " + getFullyOSName() + " nie jest prawodopodobnie jeszcze wspierane", ioException);
        }
    }

    public static void setConsoleName(final String name) {
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder();

            switch (getSystemFamily()) {
                case WINDOWS -> processBuilder.command("cmd.exe", "/c", "title", name);
                case UNIX -> processBuilder.command("bash", "-c", "printf '\\033]0;%s\\007' \"" + name + "\"");
                default ->
                        throw new UnsupportedSystemException("Ustawianie nazwy konsoli dla " + getFullyOSName() + " nie jest jeszcze zaimplementowane");
            }

            processBuilder.inheritIO().start().waitFor();
        } catch (final IOException | InterruptedException exception) {
            throw new UnsupportedSystemException("Ustawianie nazwy konsoli dla " + getFullyOSName() + " nie jest jeszcze zaimplementowane", exception);
        }
    }

    public static List<Disk> getAvailableDisk() {
        return switch (getSystem()) {
            //Free BSD i Mac są dla testów nie wiem jak działają :)
            case WINDOWS, MAC -> getAvailableRootsDisk();
            case LINUX, FREE_BSD -> getLinuxDisks();
            case UNKNOWN ->
                    throw new UnsupportedSystemException("Pozyskiwanie dysków dla " + getFullyOSName() + " nie jest jeszcze zaimplementowane");
        };
    }

    @VisibleForTesting
    public static List<Disk> getAvailableRootsDisk() {
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

    @VisibleForTesting
    public static List<Disk> getLinuxDisks() {
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

    public static long getFreeCurrentDiskSpace() {
        return getFreeDiskSpace(new File(System.getProperty("user.dir")));
    }

    public static long getMaxCurrentDiskSpace() {
        return getMaxDiskSpace(new File(System.getProperty("user.dir")));
    }

    public static long getUsedCurrentDiskSpace() {
        return getUsedDiskSpace(new File(System.getProperty("user.dir")));
    }

    public static long getFreeDiskSpace(final File diskFile) {
        return (diskFile.exists() ? diskFile.getUsableSpace() : 0);
    }

    public static long getMaxDiskSpace(final File diskFile) {
        return (diskFile.exists() ? diskFile.getTotalSpace() : 0);
    }

    public static long getUsedDiskSpace(final File diskFile) {
        return (getMaxDiskSpace(diskFile) - getFreeDiskSpace(diskFile));
    }

    public static long getUsedRam() {
        return getMaxRam() - getFreeRam();
    }

    public static long getMaxRam() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize();
    }

    public static long getFreeRam() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemorySize();
    }

    public static long getMaxSwap() {
        final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        if (getSystem() == SystemOS.WINDOWS) {
            //Windows zwraca SWAP + RAM
            return osBean.getTotalSwapSpaceSize() - getMaxRam();
        } else {
            return osBean.getTotalSwapSpaceSize();
        }
    }

    public static long getFreeSwap() {
        final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        if (getSystem() == SystemOS.WINDOWS) {
            //Windows zwraca SWAP + RAM
            return osBean.getFreeSwapSpaceSize() - getFreeRam();
        } else {
            return osBean.getFreeSwapSpaceSize();
        }
    }

    public static long getUsedSwap() {
        return getMaxSwap() - getFreeSwap();
    }

    private static long getWindowsMemoryUsage(final long pid) throws IOException {
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

    private static long getLinuxMemoryUsage(final long pid) throws IOException {
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
