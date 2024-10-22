package pl.indianbartonka.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemoryUnitTest {

    @Test
    void fromBytes() {
        Assertions.assertEquals(1073741824, MemoryUnit.BYTES.from(1, MemoryUnit.GIGABYTES), 0.01);
        Assertions.assertEquals(1048576, MemoryUnit.BYTES.from(1, MemoryUnit.MEGABYTES), 0.01);
        Assertions.assertEquals(1024, MemoryUnit.BYTES.from(1, MemoryUnit.KILOBYTES), 0.01);
    }

    @Test
    void kilobytesTo() {
        final long kilobytes = 2048;
        Assertions.assertEquals(2.00, MemoryUnit.KILOBYTES.to(kilobytes, MemoryUnit.MEGABYTES), 0.01);
        Assertions.assertEquals(2048.00, MemoryUnit.KILOBYTES.to(kilobytes, MemoryUnit.KILOBYTES), 0.01);
    }

    @Test
    void megabytesTo() {
        Assertions.assertEquals(1048576.00, MemoryUnit.MEGABYTES.to(1, MemoryUnit.BYTES), 0.01);
        Assertions.assertEquals(1024.00, MemoryUnit.MEGABYTES.to(1, MemoryUnit.KILOBYTES), 0.01);
        Assertions.assertEquals(1.00, MemoryUnit.MEGABYTES.to(1, MemoryUnit.MEGABYTES), 0.01);
    }

    @Test
    void gigabytesTo() {
        Assertions.assertEquals(1.00, MemoryUnit.GIGABYTES.to(1, MemoryUnit.GIGABYTES), 0.01);
        Assertions.assertEquals(1024.00, MemoryUnit.GIGABYTES.to(1, MemoryUnit.MEGABYTES), 0.01);
        Assertions.assertEquals(1048576.00, MemoryUnit.GIGABYTES.to(1, MemoryUnit.KILOBYTES), 0.01);
    }

    @Test
    void bytesFrom() {
        Assertions.assertEquals(1073741824, MemoryUnit.BYTES.from(1, MemoryUnit.GIGABYTES), 0.01);
        Assertions.assertEquals(1048576, MemoryUnit.BYTES.from(1, MemoryUnit.MEGABYTES), 0.01);
        Assertions.assertEquals(1024, MemoryUnit.BYTES.from(1, MemoryUnit.KILOBYTES), 0.01);
    }

    @Test
    void kilobytesFrom() {
        Assertions.assertEquals(2097152, MemoryUnit.KILOBYTES.from(2, MemoryUnit.GIGABYTES), 0.01);
        Assertions.assertEquals(2048.00, MemoryUnit.KILOBYTES.from(2, MemoryUnit.MEGABYTES), 0.01);
        Assertions.assertEquals(2, MemoryUnit.KILOBYTES.from(2, MemoryUnit.KILOBYTES), 0.01);
    }

    @Test
    void megabytesFrom() {
        Assertions.assertEquals(1, MemoryUnit.MEGABYTES.from(1048576, MemoryUnit.BYTES), 0.01);
        Assertions.assertEquals(1.00, MemoryUnit.MEGABYTES.from(1, MemoryUnit.MEGABYTES), 0.01);
        Assertions.assertEquals(1024, MemoryUnit.MEGABYTES.from(1, MemoryUnit.GIGABYTES), 0.01);
    }

    @Test
    void gigabytesFrom() {
        Assertions.assertEquals(1.00, MemoryUnit.GIGABYTES.from(1, MemoryUnit.GIGABYTES), 0.01);
        Assertions.assertEquals(0.00097656, MemoryUnit.GIGABYTES.from(1, MemoryUnit.MEGABYTES), 0.01);
    }
}
