package pl.indianbartonka.util;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathUtilTest {

    @Test
    public void testGetRandomElement() {
        final List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        final String result = MathUtil.getRandomElement(list);
        Assertions.assertTrue(list.contains(result));
    }

    @Test
    public void testKilobytesToMb() {
        assertEquals(1, MathUtil.getRemainingMegabytesFromTotalKilobytes(1024));
    }

    @Test
    public void testFormatKilobytesDynamic() {
        assertEquals("1 MB 24 KB", MathUtil.formatKilobytesDynamic(1024, true));
        assertEquals("1 kilobajtów 24 bajtów", MathUtil.formatBytesDynamic(1024, false));
    }

    @Test
    public void testFormatBytesDynamic() {
        assertEquals("1 MB 48 KB 576 B", MathUtil.formatBytesDynamic(1024 * 1024, true));
        assertEquals("1 megabajtów 48 kilobajtów 576 bajtów", MathUtil.formatBytesDynamic(1024 * 1024, false));
    }

    @Test
    public void testFormatDecimal() {
        assertEquals(12.34, MathUtil.formatDecimal(12.3456, 2), 0.01);
        assertEquals(12.3, MathUtil.formatDecimal(12.3456, 1), 0.01);
    }
}
