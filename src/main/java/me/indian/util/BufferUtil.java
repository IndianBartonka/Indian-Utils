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
        TWO_MB(2_097_152), // 2 MB
        THREE_MB(3_145_728), // 3 MB
        FOUR_MB(4_194_304), // 4 MB
        FIVE_MB(5_242_880), // 5 MB
        SIX_MB(6_291_456), // 6 MB
        SEVEN_MB(7_340_032), // 7 MB
        EIGHT_MB(8_388_608), // 8 MB
        NINE_MB(9_437_184), // 9 MB
        TEN_MB(10_485_760), // 10 MB
        ELEVEN_MB(11_534_336), // 11 MB
        TWELVE_MB(12_582_912), // 12 MB
        THIRTEEN_MB(13_631_488), // 13 MB
        FOURTEEN_MB(14_680_064), // 14 MB
        FIFTEEN_MB(15_728_640), // 15 MB
        SIXTEEN_MB(16_777_216), // 16 MB
        TWENTY_MB(20_971_520), // 20 MB
        TWENTY_ONE_MB(21_582_720), // 21 MB
        TWENTY_TWO_MB(22_193_920), // 22 MB
        TWENTY_THREE_MB(22_805_120), // 23 MB
        TWENTY_FOUR_MB(24_414_720), // 24 MB
        TWENTY_FIVE_MB(25_025_920), // 25 MB
        TWENTY_SIX_MB(25_636_120), // 26 MB
        TWENTY_SEVEN_MB(26_247_320), // 27 MB
        TWENTY_EIGHT_MB(26_858_520), // 28 MB
        TWENTY_NINE_MB(27_469_720), // 29 MB
        THIRTY_MB(28_080_920), // 30 MB
        THIRTY_ONE_MB(28_692_120), // 31 MB
        THIRTY_TWO_MB(29_303_320), // 32 MB
        THIRTY_THREE_MB(29_914_520), // 33 MB
        THIRTY_FOUR_MB(30_525_720), // 34 MB
        THIRTY_FIVE_MB(31_136_920), // 35 MB
        THIRTY_SIX_MB(31_748_120), // 36 MB
        THIRTY_SEVEN_MB(32_359_320), // 37 MB
        THIRTY_EIGHT_MB(32_970_520), // 38 MB
        THIRTY_NINE_MB(33_581_720), // 39 MB
        FORTY_MB(34_192_920), // 40 MB
        FORTY_ONE_MB(34_804_120), // 41 MB
        FORTY_TWO_MB(35_415_320), // 42 MB
        FORTY_THREE_MB(36_026_520), // 43 MB
        FORTY_FOUR_MB(36_637_720), // 44 MB
        FORTY_FIVE_MB(37_248_920), // 45 MB
        FORTY_SIX_MB(37_860_120), // 46 MB
        FORTY_SEVEN_MB(38_471_320), // 47 MB
        FORTY_EIGHT_MB(39_082_520), // 48 MB
        FORTY_NINE_MB(39_693_720), // 49 MB
        FIFTY_MB(40_304_920), // 50 MB
        FIFTY_ONE_MB(40_916_120), // 51 MB
        FIFTY_TWO_MB(41_527_320), // 52 MB
        FIFTY_THREE_MB(42_138_520), // 53 MB
        FIFTY_FOUR_MB(42_749_720), // 54 MB
        FIFTY_FIVE_MB(43_360_920), // 55 MB
        FIFTY_SIX_MB(43_972_120), // 56 MB
        FIFTY_SEVEN_MB(44_583_320), // 57 MB
        FIFTY_EIGHT_MB(45_194_520), // 58 MB
        FIFTY_NINE_MB(45_805_720), // 59 MB
        SIXTY_MB(46_416_920), // 60 MB
        SIXTY_ONE_MB(47_028_120), // 61 MB
        SIXTY_TWO_MB(47_639_320), // 62 MB
        SIXTY_THREE_MB(48_250_520), // 63 MB
        SIXTY_FOUR_MB(48_861_720); // 64 MB

        private final int buffer;

        DownloadBuffer(final int buffer) {
            this.buffer = buffer;
        }

        public int getBuffer() {
            return this.buffer;
        }
    }
}
