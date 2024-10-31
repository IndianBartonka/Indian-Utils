package pl.indianbartonka.util;

import java.io.IOException;
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
        System.out.println();
        final SystemOS os = SystemUtil.getSystem();
        assertNotNull(os);
        assertTrue(os == SystemOS.WINDOWS || os == SystemOS.LINUX || os == SystemOS.FREE_BSD || os == SystemOS.MAC);
        System.out.println(os);
    }

    @Test
    public void testSystemFamily() {
        System.out.println();
        final SystemFamily systemFamily = SystemUtil.getSystemFamily();

        assertTrue(systemFamily == SystemFamily.WINDOWS || systemFamily == SystemFamily.UNIX);
        System.out.println(systemFamily);
    }

    @Test
    public void testGetFullyOSName() {
        System.out.println();
        final String osName = SystemUtil.getFullyOSName();
        assertNotNull(osName);
        assertFalse(osName.isEmpty());
        System.out.println(osName);
    }

    @Test
    public void testGetFullOSNameWithDistribution() {
        System.out.println();
        final String fullOSName = SystemUtil.getFullOSNameWithDistribution();
        assertNotNull(fullOSName);
        assertFalse(fullOSName.isEmpty());
        System.out.println(fullOSName);
    }

    @Test
    public void testGetOSVersion() {
        System.out.println();
        final String osVersion = SystemUtil.getOSVersion();
        assertNotNull(osVersion);
        assertFalse(osVersion.isEmpty());
        System.out.println(osVersion);
    }

    @Test
    public void testGetCurrentArch() {
        System.out.println();
        final SystemArch arch = SystemUtil.getCurrentArch();
        assertNotNull(arch);
        System.out.println(arch);
    }

    @Test
    public void testGetFullyArchCode() {
        System.out.println();
        final String archCode = SystemUtil.getFullyArchCode();
        assertNotNull(archCode);
        assertFalse(archCode.isEmpty());
        System.out.println(archCode);
    }

    @Test
    public void testGetDistribution() {
        System.out.println();
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
        System.out.println();
        final long pid = ProcessHandle.current().pid(); // Get current process ID
        final long ramUsage = SystemUtil.getRamUsageByPid(pid);
        assertTrue(ramUsage >= 0); // RAM usage should be non-negative
        System.out.println(ramUsage);
    }

    @Test
    public void testDiskSpace() {
        System.out.println();

        System.out.println("Dostępne: " + MathUtil.formatBytesDynamic(SystemUtil.getFreeDiskSpace(), false));
        System.out.println("Użyte: " + MathUtil.formatBytesDynamic(SystemUtil.getUsedDiskSpace(), false));
        System.out.println("Maksymalne: " + MathUtil.formatBytesDynamic(SystemUtil.getMaxDiskSpace(), false));
    }

    @Test
    public void testRam() {
        System.out.println();

        System.out.println("Dostępne: " + MathUtil.formatBytesDynamic(SystemUtil.getFreeRam(), false));
        System.out.println("Użyte: " + MathUtil.formatBytesDynamic(SystemUtil.getUsedRam(), false));
        System.out.println("Maksymalne: " + MathUtil.formatBytesDynamic(SystemUtil.getMaxRam(), false));
    }
}
