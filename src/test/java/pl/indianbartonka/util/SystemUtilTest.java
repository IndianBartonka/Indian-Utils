package pl.indianbartonka.util;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.system.SystemArch;
import pl.indianbartonka.util.system.SystemFamily;
import pl.indianbartonka.util.system.SystemOS;
import pl.indianbartonka.util.system.SystemUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SystemUtilTest {

    @Test
    public void testGetSystem() {
        final SystemOS os = SystemUtil.getSystem();
        assertNotNull(os);
        assertTrue(os == SystemOS.WINDOWS || os == SystemOS.LINUX || os == SystemOS.FREE_BSD || os == SystemOS.MAC);
        System.out.println(os);
    }

    @Test
    public void testSystemFamily() {
        final SystemFamily systemFamily = SystemUtil.getSystemFamily();

        assertTrue(systemFamily == SystemFamily.WINDOWS || systemFamily == SystemFamily.UNIX);
        System.out.println(systemFamily);
    }

    @Test
    public void testGetFullyOSName() {
        final String osName = SystemUtil.getFullyOSName();
        assertNotNull(osName);
        assertFalse(osName.isEmpty());
        System.out.println(osName);
    }

    @Test
    public void testGetFullOSNameWithDistribution() {
        final String fullOSName = SystemUtil.getFullOSNameWithDistribution();
        assertNotNull(fullOSName);
        assertFalse(fullOSName.isEmpty());
        System.out.println(fullOSName);
    }

    @Test
    public void testGetOSVersion() {
        final String osVersion = SystemUtil.getOSVersion();
        assertNotNull(osVersion);
        assertFalse(osVersion.isEmpty());
        System.out.println(osVersion);
    }

    @Test
    public void testGetCurrentArch() {
        final SystemArch arch = SystemUtil.getCurrentArch();
        assertNotNull(arch);
        System.out.println(arch);
    }

    @Test
    public void testGetFullyArchCode() {
        final String archCode = SystemUtil.getFullyArchCode();
        assertNotNull(archCode);
        assertFalse(archCode.isEmpty());
        System.out.println(archCode);
    }

    @Test
    public void testGetDistribution() {
        final String distribution = SystemUtil.getDistribution();
        final SystemOS systemOS = SystemUtil.getSystem();

        if (systemOS == SystemOS.LINUX || systemOS == SystemOS.FREE_BSD) {
            assertNotNull(distribution);
            assertFalse(distribution.isEmpty());
        } else {
            assertEquals("Unknown", distribution);
        }

        System.out.println(distribution);
    }

    @Test
    public void testGetRamUsageByPid() throws IOException {
        final long pid = ProcessHandle.current().pid(); // Get current process ID
        final long ramUsage = SystemUtil.getRamUsageByPid(pid);
        assertTrue(ramUsage >= 0); // RAM usage should be non-negative
        System.out.println(ramUsage);
    }

    @Test
    public void testDiskSpaceLogging() {
        final long availableSpace = SystemUtil.availableDiskSpace();
        final long usedSpace = SystemUtil.usedDiskSpace();
        final long maxSpace = SystemUtil.maxDiskSpace();

        System.out.println("Dostępne: " + MathUtil.formatBytesDynamic(availableSpace, false));
        System.out.println("Użyte: " + MathUtil.formatBytesDynamic(usedSpace, false));
        System.out.println("Maksymalne: " + MathUtil.formatBytesDynamic(maxSpace, false));

        Assertions.assertTrue(availableSpace >= 0);
        Assertions.assertTrue(usedSpace >= 0);
    }
}
