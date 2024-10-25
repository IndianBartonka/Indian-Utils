package pl.indianbartonka.util;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.system.SystemArch;
import pl.indianbartonka.util.system.SystemOS;
import pl.indianbartonka.util.system.SystemUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SystemUtilTest {

    @Test
    void testGetSystem() {
        final SystemOS os = SystemUtil.getSystem();
        assertNotNull(os);
        assertTrue(os == SystemOS.WINDOWS || os == SystemOS.LINUX || os == SystemOS.FREE_BSD || os == SystemOS.MAC);
    }

    @Test
    void testGetFullyOSName() {
        final String osName = SystemUtil.getFullyOSName();
        assertNotNull(osName);
        assertFalse(osName.isEmpty());
    }

    @Test
    void testGetFullOSNameWithDistribution() {
        final String fullOSName = SystemUtil.getFullOSNameWithDistribution();
        assertNotNull(fullOSName);
        assertFalse(fullOSName.isEmpty());
    }

    @Test
    void testGetOSVersion() {
        final String osVersion = SystemUtil.getOSVersion();
        assertNotNull(osVersion);
        assertFalse(osVersion.isEmpty());
    }

    @Test
    void testGetCurrentArch() {
        final SystemArch arch = SystemUtil.getCurrentArch();
        assertNotNull(arch);
    }

    @Test
    void testGetFullyArchCode() {
        final String archCode = SystemUtil.getFullyArchCode();
        assertNotNull(archCode);
        assertFalse(archCode.isEmpty());
    }

    @Test
    void testGetDistribution() {
        final String distribution = SystemUtil.getDistribution();
        SystemOS systemOS = SystemUtil.getSystem();

        if (systemOS == SystemOS.LINUX || systemOS == SystemOS.FREE_BSD) {
            assertNotNull(distribution);
            assertFalse(distribution.isEmpty());
        } else {
            assertEquals("Unknown", distribution);
        }
    }

    @Test
    void testGetRamUsageByPid() throws IOException {
        final long pid = ProcessHandle.current().pid(); // Get current process ID
        final long ramUsage = SystemUtil.getRamUsageByPid(pid);
        assertTrue(ramUsage >= 0); // RAM usage should be non-negative
    }

    @Test
    void testDiskSpaceLogging() {
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
