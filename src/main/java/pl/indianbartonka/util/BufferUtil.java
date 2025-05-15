package pl.indianbartonka.util;

import pl.indianbartonka.util.annotation.UtilityClass;
import pl.indianbartonka.util.system.SystemUtil;

/**
 * Utility class for managing buffer sizes and RAM usage calculations.
 * Provides methods to calculate optimal buffer sizes based on file size and available system memory,
 * as well as settings for minimum and maximum buffer sizes and the RAM usage divisor.
 * <p>
 * Documents and math operations written by ChatGPT
 * </p>
 */
@UtilityClass
public final class BufferUtil {

    private static int MIN_BUFFER = (4_09);
    private static int MAX_BUFFER = (int) MemoryUnit.BYTES.from(256, MemoryUnit.MEBIBYTES);
    private static int RAM_USAGE_DIVISOR = 5;

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private BufferUtil() {

    }

    /**
     * Calculates the optimal buffer size based on file size and available system resources.
     *
     * @param fileSize The size of the file for which to calculate the buffer size.
     * @return The optimal buffer size in bytes.
     */
    public static int calculateOptimalBufferSize(final long fileSize) {
        final long bufferPerRequest = (SystemUtil.getFreeRam() + SystemUtil.getFreeSwap()) / RAM_USAGE_DIVISOR;
        final long bufferSize = Math.min((long) (fileSize * 0.1), bufferPerRequest);

        return (int) MathUtil.getCorrectNumber(bufferSize, MIN_BUFFER, MAX_BUFFER);
    }

    /**
     * Gets the minimum buffer size.
     *
     * @return The minimum buffer size.
     */
    public static int getMinBuffer() {
        return MIN_BUFFER;
    }

    /**
     * Sets the minimum buffer size.
     *
     * @param minBuffer The minimum buffer size to set.
     */
    public static void setMinBuffer(final int minBuffer) {
        MIN_BUFFER = minBuffer;
    }

    /**
     * Gets the maximum buffer size.
     *
     * @return The maximum buffer size.
     */
    public static int getMaxBuffer() {
        return MAX_BUFFER;
    }

    /**
     * Sets the maximum buffer size.
     *
     * @param maxBuffer The maximum buffer size to set.
     */
    public static void setMaxBuffer(final int maxBuffer) {
        MAX_BUFFER = maxBuffer;
    }

    /**
     * Gets the divisor used to calculate RAM usage.
     *
     * @return The RAM usage divisor.
     */
    public static int getRamUsageDivisor() {
        return RAM_USAGE_DIVISOR;
    }

    /**
     * Sets the divisor for calculating RAM usage.
     * Ensures the divisor is a positive integer greater than 0.
     * If out of range, the divisor is clamped to the valid bounds.
     *
     * @param ramUsageDivisor The divisor for RAM usage calculation.
     */
    public static void setRamUsageDivisor(final int ramUsageDivisor) {
        RAM_USAGE_DIVISOR = MathUtil.getCorrectNumber(ramUsageDivisor, 1, Integer.MAX_VALUE);
    }
}
