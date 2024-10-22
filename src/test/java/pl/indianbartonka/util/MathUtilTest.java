package pl.indianbartonka.util;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathUtilTest {

    @Test
    void testGetRandomElement() {
        final List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        final String result = MathUtil.getRandomElement(list);
        Assertions.assertTrue(list.contains(result));
    }

    @Test
    void testKilobytesToMb() {
        assertEquals(1, MathUtil.getRemainingMegabytesFromTotalKilobytes(1024));
    }

    @Test
    void testFormatKilobytesDynamic() {
        assertEquals("1 MB", MathUtil.formatKilobytesDynamic(1024, true));
        assertEquals("1 kilobajtów", MathUtil.formatBytesDynamic(1024, false));
    }

    @Test
    void testFormatBytesDynamic() {
        assertEquals("1 MB", MathUtil.formatBytesDynamic(1024 * 1024, true));
        assertEquals("1 megabajtów", MathUtil.formatBytesDynamic(1024 * 1024, false));
    }

    @Test
    void testFormatDecimal() {
        assertEquals(12.34, MathUtil.formatDecimal(12.3456, 2), 0.01);
        assertEquals(12.3, MathUtil.formatDecimal(12.3456, 1), 0.01);
    }
}
