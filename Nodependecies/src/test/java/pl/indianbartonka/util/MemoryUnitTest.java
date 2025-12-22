package pl.indianbartonka.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemoryUnitTest {

    @Test
    public void fromBytes() {
        Assertions.assertEquals(1000, MemoryUnit.BYTES.from(1, MemoryUnit.KILOBYTES), 0.01);
        Assertions.assertEquals(1024, MemoryUnit.BYTES.from(1, MemoryUnit.KIBIBYTES), 0.01);
    }

    @Test
    public void kilobytesTo() {
        final long kilobytes = 2048;
        Assertions.assertEquals(2.00, MemoryUnit.KILOBYTES.to(kilobytes, MemoryUnit.MEGABYTES), 0.01);
        Assertions.assertEquals(2048.00, MemoryUnit.KILOBYTES.to(kilobytes, MemoryUnit.KILOBYTES), 0.01);
    }

    @Test
    public void megabytesTo() {
        Assertions.assertEquals(1000, MemoryUnit.MEGABYTES.to(1, MemoryUnit.KILOBYTES), 0.01);
        Assertions.assertEquals(1024, MemoryUnit.MEBIBYTES.to(1, MemoryUnit.KIBIBYTES), 0.01);
    }

    @Test
    public void gigabytesTo() {
        Assertions.assertEquals(1000, MemoryUnit.GIGABYTES.to(1, MemoryUnit.MEGABYTES), 0.01);
        Assertions.assertEquals(1024.00, MemoryUnit.GIBIBYTES.to(1, MemoryUnit.MEBIBYTES), 0.01);
    }

    @Test
    public void bytesFrom() {
        Assertions.assertEquals(1024, MemoryUnit.BYTES.from(1, MemoryUnit.KIBIBYTES), 0.01);
        Assertions.assertEquals(1000, MemoryUnit.BYTES.from(1, MemoryUnit.KILOBYTES), 0.01);
    }

    @Test
    public void kilobytesFrom() {
        Assertions.assertEquals(2000, MemoryUnit.KILOBYTES.from(2, MemoryUnit.MEGABYTES), 0.01);
        Assertions.assertEquals(2048, MemoryUnit.KIBIBYTES.from(2, MemoryUnit.MEBIBYTES), 0.01);
    }

    @Test
    public void megabytesFrom() {
        Assertions.assertEquals(1000, MemoryUnit.MEGABYTES.from(1, MemoryUnit.GIGABYTES), 0.01);
        Assertions.assertEquals(1024, MemoryUnit.MEBIBYTES.from(1, MemoryUnit.GIBIBYTES), 0.01);
    }

    @Test
    public void gigabytesFrom() {
        Assertions.assertEquals(1.00, MemoryUnit.GIGABYTES.from(1000, MemoryUnit.MEGABYTES), 0.01);
        Assertions.assertEquals(1.00, MemoryUnit.GIBIBYTES.from(1024, MemoryUnit.MEBIBYTES), 0.01);
    }
}
