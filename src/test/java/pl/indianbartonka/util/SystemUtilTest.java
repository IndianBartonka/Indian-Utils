package pl.indianbartonka.util;

import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.system.SystemArch;
import pl.indianbartonka.util.system.SystemOS;
import pl.indianbartonka.util.system.SystemUtil;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SystemUtilTest {

    @Test
    void testGetSystem() {
        final SystemOS os = SystemUtil.getSystem();
        assertNotNull(os);
        assertTrue(os == SystemOS.WINDOWS || os == SystemOS.LINUX || os == SystemOS.MAC || os == SystemOS.UNSUPPORTED);
    }

    @Test
    void testGetFullyOsName() {
        final String osName = SystemUtil.getFullyOsName();
        assertNotNull(osName);
        assertFalse(osName.isEmpty());
    }

    @Test
    void testGetFullOsNameWithDistribution() {
        final String fullOsName = SystemUtil.getFullOsNameWithDistribution();
        assertNotNull(fullOsName);
        assertFalse(fullOsName.isEmpty());
    }

    @Test
    void testGetOsVersion() {
        final String osVersion = SystemUtil.getOsVersion();
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
    void testGetLinuxDistribution() {
        final String distribution = SystemUtil.getLinuxDistribution();
        if (SystemUtil.getSystem() == SystemOS.LINUX) {
            assertNotNull(distribution);
            assertFalse(distribution.isEmpty());
        } else {
            assertEquals("Nieznana", distribution);
        }
    }

    @Test
    void testGetRamUsageByPid() throws IOException {
        final long pid = ProcessHandle.current().pid(); // Get current process ID
        final long ramUsage = SystemUtil.getRamUsageByPid(pid);
        assertTrue(ramUsage >= 0); // RAM usage should be non-negative
    }
}
