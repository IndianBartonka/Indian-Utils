package pl.indianbartonka.util.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import pl.indianbartonka.util.annotation.UtilityClass;

@UtilityClass
public final class SystemUtil {

    public static final Locale LOCALE = Locale.getDefault();
    private static final File FILE = new File(File.separator);

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
        return switch (getSystem()) {
            case WINDOWS, MAC, UNKNOWN -> getFullyOSName();
            case LINUX, FREE_BSD -> getFullyOSName() + " (" + getDistribution() + ")";
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
    public static long getRamUsageByPid(final long pid) throws IOException {
        return switch (getSystemFamily()) {
            case WINDOWS -> getWindowsMemoryUsage(pid);
            case UNIX -> getUnixMemoryUsage(pid);
            default ->
                    throw new UnsupportedOperationException("Pozyskiwanie ilosci ram dla " + getFullyOSName() + " nie jest wspierane");
        };
    }

    public static long availableDiskSpace() {
        return (FILE.exists() ? FILE.getUsableSpace() : 0);
    }

    public static long maxDiskSpace() {
        return (FILE.exists() ? FILE.getTotalSpace() : 0);
    }

    public static long usedDiskSpace() {
        return (maxDiskSpace() - availableDiskSpace());
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

    private static long getUnixMemoryUsage(final long pid) throws IOException {
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
