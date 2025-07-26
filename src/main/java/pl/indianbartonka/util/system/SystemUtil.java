package pl.indianbartonka.util.system;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.VisibleForTesting;
import pl.indianbartonka.util.FileUtil;
import pl.indianbartonka.util.MemoryUnit;
import pl.indianbartonka.util.MessageUtil;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.annotation.UtilityClass;
import pl.indianbartonka.util.exception.UnsupportedSystemException;
import pl.indianbartonka.util.system.parts.Disk;
import pl.indianbartonka.util.system.parts.Ram;

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

    @VisibleForTesting
    public static String getProcesorName() {
        try {
            return switch (getSystem()) {
                case WINDOWS -> WindowsUtil.getProcessorName();
                case LINUX, FREE_BSD -> LinuxUtil.getProcessorName();
                case MAC, UNKNOWN -> "Aktualnie pozyskanie nazwy procesora nie jest wspierane dla tego systemu";
            };
        } catch (final IOException ioException) {
            return "Nie udało się pozyskać nazwy procesora, " + MessageUtil.getStackTraceAsString(ioException);
        }
    }

    @VisibleForTesting
    public static List<String> getGraphicCardsName() {
        try {
            return switch (getSystem()) {
                case WINDOWS -> WindowsUtil.getGraphicCardsName();
                case LINUX, FREE_BSD -> LinuxUtil.getGraphicCardsName();
                case MAC, UNKNOWN ->
                        List.of("Aktualnie pozyskanie nazwy karty graficznej nie jest wspierane dla tego systemu");
            };
        } catch (final IOException ioException) {
            return List.of("Nie udało się pozyskać nazwy karty graficznej, " + MessageUtil.getStackTraceAsString(ioException));
        }
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
                case WINDOWS -> WindowsUtil.getMemoryUsage(pid);
                case UNIX -> LinuxUtil.getMemoryUsage(pid);
                default ->
                        throw new UnsupportedSystemException("Pozyskiwanie ilosci ram dla " + getFullyOSName() + " nie jest jeszcze zaimplementowane");
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
            case WINDOWS, MAC -> WindowsUtil.getAvailableDisks();
            case LINUX, FREE_BSD -> LinuxUtil.getAvailableDisks();
            case UNKNOWN ->
                    throw new UnsupportedSystemException("Pozyskiwanie dysków dla " + getFullyOSName() + " nie jest jeszcze zaimplementowane");
        };
    }

    @Since("0.0.9.3")
    public static List<Ram> getRamData() {
        try {
            return switch (getSystem()) {
                case WINDOWS, MAC -> WindowsUtil.getRamData();
                case LINUX, FREE_BSD -> List.of();
                case UNKNOWN ->
                        throw new UnsupportedSystemException("Pozyskiwanie dysków dla " + getFullyOSName() + " nie jest jeszcze zaimplementowane");
            };
        } catch (final IOException ioException) {
            throw new UnsupportedSystemException("Pozyskiwanie danych o ram dla" + getFullyOSName() + " nie jest jeszcze zaimplementowane", ioException);
        }
    }

    @VisibleForTesting
    @Since("0.0.9.3")
    @CheckReturnValue
    public static long testDisk(final Disk disk, final int mbSize, final int totalWrites) throws IOException {
        final File diskFile = disk.diskFile();

        if (diskFile == null) return -1;

        final File fileDir = new File(diskFile, String.valueOf(UUID.randomUUID()));
        final File file = new File(fileDir, "testFile.dat");

        fileDir.deleteOnExit();
        file.deleteOnExit();

        try {
            Files.createDirectories(fileDir.toPath());

            if (!file.createNewFile()) {
                throw new IOException("Nie można utworzyć pliku testowego z nieznanych przyczyn");
            }

            final long startTime = System.currentTimeMillis();

            try (final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
                final byte[] buffer = new byte[Math.toIntExact(MemoryUnit.MEGABYTES.to(mbSize, MemoryUnit.BYTES))];

                for (int i = 0; i < totalWrites; i++) {
                    randomAccessFile.write(buffer);
                    randomAccessFile.getChannel().force(false);
                }
            }

            return System.currentTimeMillis() - startTime;
        } catch (final AccessDeniedException accessDeniedException) {
            return -1;
        } finally {
            if (file.exists()) FileUtil.deleteFile(file);
            if (fileDir.exists()) FileUtil.deleteFile(fileDir);
        }
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
}
