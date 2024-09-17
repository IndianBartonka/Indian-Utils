package me.indian.util;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

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

        return (int) MathUtil.getCorrectNumber(bufferSize, DownloadBuffer.FOUR_KB.getBuffer(), BufferUtil.DownloadBuffer.SIXTEEN_MB.getBuffer());
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

    public enum DownloadBuffer {

        DYNAMIC(-1),

        FOUR_KB(4_096),
        EIGHT_KB(8_192),
        SIXTEEN_KB(16_384),
        THIRTY_TWO_KB(32_768),
        SIXTY_FOUR_KB(65_536),
        ONE_HUNDRED_TWENTY_EIGHT_KB(131_072),
        TWO_FIFTY_SIX_KB(262_144),
        FIVE_HUNDRED_TWELVE_KB(524_288),
        ONE_MB(1_048_576),
        TWO_MB(2_097_152),
        THREE_MB(3_145_728),
        FOUR_MB(4_194_304),
        FIVE_MB(5_242_880),
        SIX_MB(6_291_456),
        SEVEN_MB(7_340_032),
        EIGHT_MB(8_388_608),
        NINE_MB(9_437_184),
        TEN_MB(10_485_760),
        ELEVEN_MB(11_534_336),
        TWELVE_MB(12_582_912),
        THIRTEEN_MB(13_631_488),
        FOURTEEN_MB(14_680_064),
        FIFTEEN_MB(15_728_640),
        SIXTEEN_MB(16_777_216);

        private final int buffer;

        DownloadBuffer(final int buffer) {
            this.buffer = buffer;
        }

        public int getBuffer() {
            return this.buffer;
        }
    }
}