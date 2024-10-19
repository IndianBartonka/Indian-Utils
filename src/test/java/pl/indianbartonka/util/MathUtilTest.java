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
    void testGetCorrectNumberInt() {
        assertEquals(5, MathUtil.getCorrectNumber(5, 1, 10));
        assertEquals(1, MathUtil.getCorrectNumber(-5, 1, 10));
        assertEquals(10, MathUtil.getCorrectNumber(15, 1, 10));
    }

    @Test
    void testGetCorrectNumberDouble() {
        assertEquals(5.5, MathUtil.getCorrectNumber(5.5, 1.0, 10.0));
        assertEquals(1.0, MathUtil.getCorrectNumber(-5.5, 1.0, 10.0));
        assertEquals(10.0, MathUtil.getCorrectNumber(15.5, 1.0, 10.0));
    }

    @Test
    void testKilobytesToMb() {
        assertEquals(1, MathUtil.kilobytesToMb(1024));
        assertEquals(0, MathUtil.kilobytesToMb(512));
    }

    @Test
    void testKilobytesToGb() {
        assertEquals(1, MathUtil.kilobytesToGb(1024 * 1024));
        assertEquals(0, MathUtil.kilobytesToGb(512 * 1024));
    }

    @Test
    void testFormatKiloBytesDynamic() {
        assertEquals("1 MB", MathUtil.formatKiloBytesDynamic(1024, true));
        assertEquals("1 kilobajtów", MathUtil.formatBytesDynamic(1024, false));
    }

    @Test
    void testBytesToKb() {
        assertEquals(1, MathUtil.bytesToKB(1024));
        assertEquals(0, MathUtil.bytesToKB(512));
    }

    @Test
    void testFormatBytesDynamic() {
        assertEquals("1 MB", MathUtil.formatBytesDynamic(1024 * 1024, true));
        assertEquals("1 megabajtów", MathUtil.formatBytesDynamic(1024 * 1024, false));
    }

    @Test
    void testFormatDecimal() {
        assertEquals(12.34, MathUtil.format(12.3456, 2), 0.01);
        assertEquals(12.3, MathUtil.format(12.3456, 1), 0.01);
    }
}
