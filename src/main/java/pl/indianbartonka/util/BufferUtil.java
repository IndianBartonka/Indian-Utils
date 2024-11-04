package pl.indianbartonka.util;

import pl.indianbartonka.util.annotation.UtilityClass;
import pl.indianbartonka.util.system.SystemUtil;

@UtilityClass
public final class BufferUtil {

    private static int MIN_BUFFER = 4_09;
    private static int MAX_BUFFER = (256 * 1_048_576);

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private BufferUtil() {

    }

    public static int calculateOptimalBufferSize(final long fileSize) {
        final long bufferPerRequest = SystemUtil.getFreeRam() / 5;
        final long bufferSize = Math.min((long) (fileSize * 0.1), bufferPerRequest);

        return (int) MathUtil.getCorrectNumber(bufferSize, MIN_BUFFER, MAX_BUFFER);
    }

    public static int getMinBuffer() {
        return MIN_BUFFER;
    }

    public static void setMinBuffer(final int minBuffer) {
        MIN_BUFFER = minBuffer;
    }

    public static int getMaxBuffer() {
        return MAX_BUFFER;
    }

    public static void setMaxBuffer(final int maxBuffer) {
        MAX_BUFFER = maxBuffer;
    }
}