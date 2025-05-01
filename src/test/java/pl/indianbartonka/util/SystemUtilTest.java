package pl.indianbartonka.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.FileUtil;
import pl.indianbartonka.util.system.Disk;
import pl.indianbartonka.util.system.SystemArch;
import pl.indianbartonka.util.system.SystemFamily;
import pl.indianbartonka.util.system.SystemOS;
import pl.indianbartonka.util.system.SystemUtil;


public class SystemUtilTest {

    @Test
    public void testGetSystem() {
        System.out.println();
        System.out.println(SystemUtil.getProcesorName());
        System.out.println();

        System.out.println(SystemUtil.getGraphicCardsName());
        System.out.println();

        final SystemOS os = SystemUtil.getSystem();
        Assertions.assertNotNull(os);
        Assertions.assertNotSame(os, SystemOS.UNKNOWN);
        System.out.println(os);
    }

    @Test
    public void testSystemFamily() {
        System.out.println();
        final SystemFamily systemFamily = SystemUtil.getSystemFamily();

        Assertions.assertTrue(systemFamily == SystemFamily.WINDOWS || systemFamily == SystemFamily.UNIX);
        System.out.println(systemFamily);
    }

    @Test
    public void testGetFullyOSName() {
        System.out.println();
        final String osName = SystemUtil.getFullyOSName();
        Assertions.assertNotNull(osName);
        Assertions.assertFalse(osName.isEmpty());
        System.out.println(osName);
    }

    @Test
    public void testGetFullOSNameWithDistribution() {
        System.out.println();
        final String fullOSName = SystemUtil.getFullOSNameWithDistribution();
        Assertions.assertNotNull(fullOSName);
        Assertions.assertFalse(fullOSName.isEmpty());
        System.out.println(fullOSName);
    }

    @Test
    public void testGetOSVersion() {
        System.out.println();
        final String osVersion = SystemUtil.getOSVersion();
        Assertions.assertNotNull(osVersion);
        Assertions.assertFalse(osVersion.isEmpty());
        System.out.println(osVersion);
    }

    @Test
    public void testGetCurrentArch() {
        System.out.println();
        final SystemArch arch = SystemUtil.getCurrentArch();
        Assertions.assertNotNull(arch);
        System.out.println(arch);
    }

    @Test
    public void testGetFullyArchCode() {
        System.out.println();
        final String archCode = SystemUtil.getFullyArchCode();
        Assertions.assertNotNull(archCode);
        Assertions.assertFalse(archCode.isEmpty());
        System.out.println(archCode);
    }

    @Test
    public void testGetDistribution() {
        System.out.println();
        final String distribution = SystemUtil.getDistribution();

        if (SystemUtil.getSystemFamily() == SystemFamily.UNIX) {
            Assertions.assertNotNull(distribution);
            Assertions.assertFalse(distribution.isEmpty());
        } else {
            Assertions.assertEquals("Unknown", distribution);
        }

        System.out.println(distribution);
    }

    @Test
    public void testGetRamUsageByPid() {
        System.out.println();
        final long pid = ProcessHandle.current().pid();
        final long ramUsage = SystemUtil.getRamUsageByPid(pid);
        Assertions.assertTrue(ramUsage >= 0);
        System.out.println(ramUsage);
    }

    @Test
    public void testDiskSpace() throws IOException {
        System.out.println();

        final List<Disk> disks = SystemUtil.getAvailableDisk();

        System.out.println("Dostępne dyski: " + disks.size());

        for (final Disk disk : disks) {
            final File diskFile = disk.diskFile();
            System.out.println();
            System.out.println(disk.name() + " | " + diskFile.getAbsolutePath());
            System.out.println("Typ dysku: " + disk.type());
            System.out.println("Rozmiar bloku: " + disk.blockSize());
            System.out.println("Tylko do odczytu: " + disk.readOnly());

            System.out.println("Całkowita pamięć: " + MathUtil.formatBytesDynamic(SystemUtil.getMaxDiskSpace(diskFile), false));
            System.out.println("Użyta pamięć: " + MathUtil.formatBytesDynamic(SystemUtil.getUsedDiskSpace(diskFile), false));
            System.out.println("Wolna pamięć: " + MathUtil.formatBytesDynamic(SystemUtil.getFreeDiskSpace(diskFile), false));

            if (!diskFile.getPath().contains("C")) {
                System.out.println("Pozyskiwanie plików.....");
                System.out.println();
                for (final File file : FileUtil.listAllFiles(diskFile)) {
                    System.out.println(
                            String.format("%-20s %-30s | %-50s",
                                    MathUtil.formatBytesDynamic(FileUtil.getFileSize(file), true),
                                    Files.getOwner(file.toPath()).getName(),
                                    file.getPath()
                            )
                    );
                }
            }
        }
    }

    @Test
    public void testRam() {
        System.out.println();

        System.out.println("Dostępne: " + MathUtil.formatBytesDynamic(SystemUtil.getFreeRam(), false));
        System.out.println("Użyte: " + MathUtil.formatBytesDynamic(SystemUtil.getUsedRam(), false));
        System.out.println("Maksymalne: " + MathUtil.formatBytesDynamic(SystemUtil.getMaxRam(), false));
    }
}
