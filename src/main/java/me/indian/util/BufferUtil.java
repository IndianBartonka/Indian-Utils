package me.indian.util;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

public final class BufferUtil {

    private BufferUtil() {

    }

    public static int defineBuffer(final DownloadBuffer downloadBuffer, final long fileSize) {
        if (downloadBuffer == BufferUtil.DownloadBuffer.DYNAMIC) return calculateOptimalBufferSize(fileSize);

        return downloadBuffer.getBuffer();
    }

    public static int calculateOptimalBufferSize(final long fileSize) {
        final long bufferPerRequest = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemorySize() / 5;
        final long bufferSize = Math.min((long) (fileSize * 0.1), bufferPerRequest);

        return (int) MathUtil.getCorrectNumber(bufferSize, DownloadBuffer.FOUR_KB.getBuffer(), getBiggestBuffer());
    }

    public static DownloadBuffer findBuffer(final long buffer) {
        DownloadBuffer closestBuffer = BufferUtil.DownloadBuffer.DYNAMIC;
        long minDifference = Integer.MAX_VALUE;

        for (final DownloadBuffer downloadBuffer : BufferUtil.DownloadBuffer.values()) {
            final int currentBufferSize = downloadBuffer.getBuffer();
            final long difference = Math.abs(currentBufferSize - buffer);

            if (difference < minDifference) {
                minDifference = difference;
                closestBuffer = downloadBuffer;
            }
        }

        return closestBuffer;
    }

    public static int getBiggestBuffer() {
        return Arrays.stream(DownloadBuffer.values())
                .mapToInt(DownloadBuffer::getBuffer)
                .max()
                .orElse(-1);
    }

    //TODO: Zmienić to na 'Buffer' i może dać do innej klasy że oddzielnej 
    public enum DownloadBuffer {

        DYNAMIC(-1), // Dynamiczny rozmiar bufora
        FOUR_KB(4_096), // 4 KB (0.004 MB)
        EIGHT_KB(8_192), // 8 KB (0.008 MB)
        SIXTEEN_KB(16_384), // 16 KB (0.016 MB)
        THIRTY_TWO_KB(32_768), // 32 KB (0.03125 MB)
        SIXTY_FOUR_KB(65_536), // 64 KB (0.0625 MB)
        ONE_HUNDRED_TWENTY_EIGHT_KB(131_072), // 128 KB (0.125 MB)
        TWO_FIFTY_SIX_KB(262_144), // 256 KB (0.25 MB)
        FIVE_HUNDRED_TWELVE_KB(524_288), // 512 KB (0.5 MB)
        ONE_MB(1_048_576), // 1 MB
        TWO_MB(2 * 1_048_576), // 2 MB
        THREE_MB(3 * 1_048_576), // 3 MB
        FOUR_MB(4 * 1_048_576), // 4 MB
        FIVE_MB(5 * 1_048_576), // 5 MB
        SIX_MB(6 * 1_048_576), // 6 MB
        SEVEN_MB(7 * 1_048_576), // 7 MB
        EIGHT_MB(8 * 1_048_576), // 8 MB
        NINE_MB(9 * 1_048_576), // 9 MB
        TEN_MB(10 * 1_048_576), // 10 MB
        ELEVEN_MB(11 * 1_048_576), // 11 MB
        TWELVE_MB(12 * 1_048_576), // 12 MB
        THIRTEEN_MB(13 * 1_048_576), // 13 MB
        FOURTEEN_MB(14 * 1_048_576), // 14 MB
        FIFTEEN_MB(15 * 1_048_576), // 15 MB
        SIXTEEN_MB(16 * 1_048_576), // 16 MB
        SEVENTEEN_MB(17 * 1_048_576), // 17 MB
        EIGHTEEN_MB(18 * 1_048_576), // 18 MB
        NINETEEN_MB(19 * 1_048_576), // 19 MB
        TWENTY_MB(20 * 1_048_576), // 20 MB
        TWENTY_ONE_MB(21 * 1_048_576), // 21 MB
        TWENTY_TWO_MB(22 * 1_048_576), // 22 MB
        TWENTY_THREE_MB(23 * 1_048_576), // 23 MB
        TWENTY_FOUR_MB(24 * 1_048_576), // 24 MB
        TWENTY_FIVE_MB(25 * 1_048_576), // 25 MB
        TWENTY_SIX_MB(26 * 1_048_576), // 26 MB
        TWENTY_SEVEN_MB(27 * 1_048_576), // 27 MB
        TWENTY_EIGHT_MB(28 * 1_048_576), // 28 MB
        TWENTY_NINE_MB(29 * 1_048_576), // 29 MB
        THIRTY_MB(30 * 1_048_576), // 30 MB
        THIRTY_ONE_MB(31 * 1_048_576), // 31 MB
        THIRTY_TWO_MB(32 * 1_048_576), // 32 MB
        THIRTY_THREE_MB(33 * 1_048_576), // 33 MB
        THIRTY_FOUR_MB(34 * 1_048_576), // 34 MB
        THIRTY_FIVE_MB(35 * 1_048_576), // 35 MB
        THIRTY_SIX_MB(36 * 1_048_576), // 36 MB
        THIRTY_SEVEN_MB(37 * 1_048_576), // 37 MB
        THIRTY_EIGHT_MB(38 * 1_048_576), // 38 MB
        THIRTY_NINE_MB(39 * 1_048_576), // 39 MB
        FORTY_MB(40 * 1_048_576), // 40 MB
        FORTY_ONE_MB(41 * 1_048_576), // 41 MB
        FORTY_TWO_MB(42 * 1_048_576), // 42 MB
        FORTY_THREE_MB(43 * 1_048_576), // 43 MB
        FORTY_FOUR_MB(44 * 1_048_576), // 44 MB
        FORTY_FIVE_MB(45 * 1_048_576), // 45 MB
        FORTY_SIX_MB(46 * 1_048_576), // 46 MB
        FORTY_SEVEN_MB(47 * 1_048_576), // 47 MB
        FORTY_EIGHT_MB(48 * 1_048_576), // 48 MB
        FORTY_NINE_MB(49 * 1_048_576), // 49 MB
        FIFTY_MB(50 * 1_048_576), // 50 MB
        FIFTY_ONE_MB(51 * 1_048_576), // 51 MB
        FIFTY_TWO_MB(52 * 1_048_576), // 52 MB
        FIFTY_THREE_MB(53 * 1_048_576), // 53 MB
        FIFTY_FOUR_MB(54 * 1_048_576), // 54 MB
        FIFTY_FIVE_MB(55 * 1_048_576), // 55 MB
        FIFTY_SIX_MB(56 * 1_048_576), // 56 MB
        FIFTY_SEVEN_MB(57 * 1_048_576), // 57 MB
        FIFTY_EIGHT_MB(58 * 1_048_576), // 58 MB
        FIFTY_NINE_MB(59 * 1_048_576), // 59 MB
        SIXTY_MB(60 * 1_048_576), // 60 MB
        SIXTY_ONE_MB(61 * 1_048_576), // 61 MB
        SIXTY_TWO_MB(62 * 1_048_576), // 62 MB
        SIXTY_THREE_MB(63 * 1_048_576), // 63 MB
        SIXTY_FOUR_MB(64 * 1_048_576), // 64 MB
        ONE_HUNDRED_TWENTY_EIGHT_MB(134_217_728), // 128 MB
        TWO_HUNDRED_FIFTY_SIX_MB(268_435_456); // 256 MB

        private final int buffer;

        DownloadBuffer(final int buffer) {
            this.buffer = buffer;
        }

        public int getBuffer() {
            return this.buffer;
        }
    }
}
