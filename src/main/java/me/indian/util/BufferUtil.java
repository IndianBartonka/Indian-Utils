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
       //TODO: W większości wypadków użyj tego zamiast defineBuffer
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
//TODO: Opisz wielkosc w komentarzu ze np 64MB
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
        SIXTEEN_MB(16_777_216),
        TWENTY_MB(20_971_520),
        TWENTY_ONE_MB(21_582_720),
        TWENTY_TWO_MB(22_193_920),
        TWENTY_THREE_MB(22_805_120),
        TWENTY_FOUR_MB(24_414_720),
        TWENTY_FIVE_MB(25_025_920),
        TWENTY_SIX_MB(25_636_120),
        TWENTY_SEVEN_MB(26_247_320),
        TWENTY_EIGHT_MB(26_858_520),
        TWENTY_NINE_MB(27_469_720),
        THIRTY_MB(28_080_920),
        THIRTY_ONE_MB(28_692_120),
        THIRTY_TWO_MB(29_303_320),
        THIRTY_THREE_MB(29_914_520),
        THIRTY_FOUR_MB(30_525_720),
        THIRTY_FIVE_MB(31_136_920),
        THIRTY_SIX_MB(31_748_120),
        THIRTY_SEVEN_MB(32_359_320),
        THIRTY_EIGHT_MB(32_970_520),
        THIRTY_NINE_MB(33_581_720),
        FORTY_MB(34_192_920),
        FORTY_ONE_MB(34_804_120),
        FORTY_TWO_MB(35_415_320),
        FORTY_THREE_MB(36_026_520),
        FORTY_FOUR_MB(36_637_720),
        FORTY_FIVE_MB(37_248_920),
        FORTY_SIX_MB(37_860_120),
        FORTY_SEVEN_MB(38_471_320),
        FORTY_EIGHT_MB(39_082_520),
        FORTY_NINE_MB(39_693_720),
        FIFTY_MB(40_304_920),
        FIFTY_ONE_MB(40_916_120),
        FIFTY_TWO_MB(41_527_320),
        FIFTY_THREE_MB(42_138_520),
        FIFTY_FOUR_MB(42_749_720),
        FIFTY_FIVE_MB(43_360_920),
        FIFTY_SIX_MB(43_972_120),
        FIFTY_SEVEN_MB(44_583_320),
        FIFTY_EIGHT_MB(45_194_520),
        FIFTY_NINE_MB(45_805_720),
        SIXTY_MB(46_416_920),
        SIXTY_ONE_MB(47_028_120),
        SIXTY_TWO_MB(47_639_320),
        SIXTY_THREE_MB(48_250_520),
        SIXTY_FOUR_MB(48_861_720);

        private final int buffer;

        DownloadBuffer(final int buffer) {
            this.buffer = buffer;
        }

        public int getBuffer() {
            return this.buffer;
        }
    }
}
