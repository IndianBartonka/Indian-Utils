package pl.indianbartonka.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Utility class providing mathematical operations, data conversions, and formatting functions.
 * Documents written by Chat-GPT
 */
public final class MathUtil {

    // Static instance of DecimalFormat for formatting numbers.
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    // Static initializer to configure DecimalFormat with English locale symbols.
    static {
        final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DECIMAL_FORMAT.setDecimalFormatSymbols(decimalFormatSymbols);
    }

    // Private constructor to prevent instantiation of the utility class.
    private MathUtil() {
    }

    /**
     * Clamps an integer number to be within the specified minimum and maximum range.
     *
     * @param number The number to clamp.
     * @param min    The minimum allowable value.
     * @param max    The maximum allowable value.
     * @return The clamped value within the range.
     */
    public static int getCorrectNumber(final int number, final int min, final int max) {
        return Math.max(min, Math.min(max, number));
    }

    /**
     * Clamps a double number to be within the specified minimum and maximum range.
     *
     * @param number The number to clamp.
     * @param min    The minimum allowable value.
     * @param max    The maximum allowable value.
     * @return The clamped value within the range.
     */
    public static double getCorrectNumber(final double number, final double min, final double max) {
        return Math.max(min, Math.min(max, number));
    }

    /**
     * Formats a double value to the specified number of decimal places.
     *
     * @param decimal The decimal number to format.
     * @param format  The number of decimal places.
     * @return The formatted double value.
     */
    public static double format(final double decimal, final int format) {
        DECIMAL_FORMAT.setMaximumFractionDigits(format);
        return Double.parseDouble(DECIMAL_FORMAT.format(decimal));
    }

    // Data conversion methods

    /**
     * Converts kilobytes to megabytes.
     *
     * @param kilobytes The number of kilobytes.
     * @return The equivalent number of megabytes.
     */
    public static long kilobytesToMb(final long kilobytes) {
        return kilobytes / 1024;
    }

    /**
     * Converts kilobytes to gigabytes.
     *
     * @param kilobytes The number of kilobytes.
     * @return The equivalent number of gigabytes.
     */
    public static long kilobytesToGb(final long kilobytes) {
        return kilobytes / (1024 * 1024);
    }

    /**
     * Converts kilobytes to bytes.
     *
     * @param kilobytes The number of kilobytes.
     * @return The equivalent number of bytes.
     */
    public static long kilobytesToBytes(final long kilobytes) {
        return kilobytes * 1024;
    }

    /**
     * Converts bytes to kilobytes.
     *
     * @param bytes The number of bytes.
     * @return The equivalent number of kilobytes.
     */
    public static long bytesToKB(final long bytes) {
        return bytes / 1024;
    }

    /**
     * Converts bytes to megabytes.
     *
     * @param bytes The number of bytes.
     * @return The equivalent number of megabytes.
     */
    public static long bytesToMB(final long bytes) {
        return bytes / (1024 * 1024);
    }

    /**
     * Converts bytes to gigabytes.
     *
     * @param bytes The number of bytes.
     * @return The equivalent number of gigabytes.
     */
    public static long bytesToGB(final long bytes) {
        return bytes / (1024 * 1024 * 1024);
    }

    // Methods to extract remaining units

    /**
     * Extracts the remaining gigabytes from a total number of bytes.
     *
     * @param bytes The number of bytes.
     * @return The remaining gigabytes.
     */
    public static long getRemainingGbFromBytes(final long bytes) {
        return (bytes % (1024L * 1024 * 1024 * 1024)) / (1024 * 1024 * 1024);
    }

    /**
     * Extracts the remaining megabytes from a total number of bytes.
     *
     * @param bytes The number of bytes.
     * @return The remaining megabytes.
     */
    public static long getRemainingMbFromBytes(final long bytes) {
        return (bytes % (1024 * 1024 * 1024)) / (1024 * 1024);
    }

    /**
     * Extracts the remaining kilobytes from a total number of bytes.
     *
     * @param bytes The number of bytes.
     * @return The remaining kilobytes.
     */
    public static long getRemainingKbFromBytes(final long bytes) {
        return (bytes % (1024 * 1024)) / 1024;
    }

    /**
     * Extracts the remaining gigabytes from a total number of kilobytes.
     *
     * @param kb The number of kilobytes.
     * @return The remaining gigabytes.
     */
    public static long getRemainingGbFromKb(final long kb) {
        return (kb % (1024L * 1024 * 1024)) / (1024 * 1024);
    }

    /**
     * Extracts the remaining megabytes from a total number of kilobytes.
     *
     * @param kb The number of kilobytes.
     * @return The remaining megabytes.
     */
    public static long getRemainingMbFromKb(final long kb) {
        return (kb % (1024 * 1024)) / 1024;
    }

    /**
     * Converts kilobytes to bytes.
     *
     * @param kb The number of kilobytes.
     * @return The equivalent number of bytes.
     */
    public static long getBytesFromKb(final long kb) {
        return kb * 1024;
    }

    // Formatting methods

    /**
     * Formats kilobytes dynamically based on their magnitude, using either short or long names.
     *
     * @param kilobytes  The number of kilobytes to format.
     * @param shortNames If true, uses short names (e.g., KB, MB, GB); otherwise uses long names (e.g., kilobytes, megabytes, gigabytes).
     * @return The formatted kilobytes as a string.
     */
    public static String formatKiloBytesDynamic(final long kilobytes, final boolean shortNames) {
        if (kilobytes == 0) return "N/A";

        final List<Character> unitsPattern = new ArrayList<>();
        final long gb = kilobytesToGb(kilobytes);
        final long mb = getRemainingMbFromKb(kilobytes);

        if (gb > 0) unitsPattern.add('g');
        if (mb > 0) {
            unitsPattern.add('m');
        } else {
            unitsPattern.add('k');
        }

        return formatKilobytes(kilobytes, unitsPattern, shortNames);
    }

    /**
     * Formats bytes dynamically based on their magnitude, using either short or long names.
     *
     * @param bytes      The number of bytes to format.
     * @param shortNames If true, uses short names (e.g., KB, MB, GB); otherwise uses long names (e.g., kilobytes, megabytes, gigabytes).
     * @return The formatted bytes as a string.
     */
    public static String formatBytesDynamic(final long bytes, final boolean shortNames) {
        if (bytes <= 0) return "N/A";

        final List<Character> unitsPattern = new ArrayList<>();
        final long gb = bytesToGB(bytes);
        final long mb = getRemainingMbFromBytes(bytes);
        final long kb = getRemainingKbFromBytes(bytes);

        if (gb > 0) unitsPattern.add('g');
        if (mb > 0) {
            unitsPattern.add('m');
        } else {
            if (kb > 0) {
                unitsPattern.add('k');
            } else {
                unitsPattern.add('b');
            }
        }

        return formatBytes(bytes, unitsPattern, shortNames);
    }

    /**
     * Formats kilobytes according to the specified units pattern and naming convention.
     *
     * @param kilobytes    The number of kilobytes to format.
     * @param unitsPattern The units pattern to use (e.g., 'k', 'm', 'g').
     * @param shortNames   If true, uses short names; otherwise uses long names.
     * @return The formatted kilobytes as a string.
     */
    public static String formatKilobytes(final long kilobytes, final List<Character> unitsPattern, final boolean shortNames) {
        final StringBuilder formattedKilobytes = new StringBuilder();
        final Map<Character, String> unitMap = getUnitKilobytesMap(kilobytes, shortNames);

        for (final char unit : unitsPattern) {
            if (unitMap.containsKey(unit)) {
                formattedKilobytes.append(unitMap.get(unit)).append(" ");
            }
        }

        return formattedKilobytes.toString().trim();
    }

    /**
     * Formats bytes according to the specified units pattern and naming convention.
     *
     * @param bytes        The number of bytes to format.
     * @param unitsPattern The units pattern to use (e.g., 'b', 'k', 'm', 'g').
     * @param shortNames   If true, uses short names; otherwise uses long names.
     * @return The formatted bytes as a string.
     */
    public static String formatBytes(final long bytes, final List<Character> unitsPattern, final boolean shortNames) {
        final StringBuilder formattedBytes = new StringBuilder();
        final Map<Character, String> unitMap = getUnitBytesMap(bytes, shortNames);

        for (final char unit : unitsPattern) {
            if (unitMap.containsKey(unit)) {
                formattedBytes.append(unitMap.get(unit)).append(" ");
            }
        }

        return formattedBytes.toString().trim();
    }

    // Helper methods to map units to strings

    /**
     * Provides a mapping of unit characters to their string representations for bytes.
     *
     * @param bytes      The number of bytes.
     * @param shortNames If true, uses short names; otherwise uses long names.
     * @return A map of unit characters to their string representations.
     */
    private static Map<Character, String> getUnitBytesMap(final long bytes, final boolean shortNames) {
        final Map<Character, String> UNIT_MAP = new HashMap<>();

        if (shortNames) {
            UNIT_MAP.put('k', getRemainingKbFromBytes(bytes) + " KB");
            UNIT_MAP.put('m', getRemainingMbFromBytes(bytes) + " MB");
            UNIT_MAP.put('g', getRemainingGbFromBytes(bytes) + " GB");
            UNIT_MAP.put('b', bytes + " b");
        } else {
            UNIT_MAP.put('k', getRemainingKbFromBytes(bytes) + " kilobajtów");
            UNIT_MAP.put('m', getRemainingMbFromBytes(bytes) + " megabajtów");
            UNIT_MAP.put('g', getRemainingGbFromBytes(bytes) + " gigabajtów");
            UNIT_MAP.put('b', bytes + " bajtów");
        }

        return UNIT_MAP;
    }

    /**
     * Provides a mapping of unit characters to their string representations for kilobytes.
     *
     * @param kilobytes  The number of kilobytes.
     * @param shortNames If true, uses short names; otherwise uses long names.
     * @return A map of unit characters to their string representations.
     */
    private static Map<Character, String> getUnitKilobytesMap(final long kilobytes, final boolean shortNames) {
        final Map<Character, String> UNIT_MAP = new HashMap<>();

        if (shortNames) {
            UNIT_MAP.put('k', kilobytes + " KB");
            UNIT_MAP.put('m', getRemainingMbFromKb(kilobytes) + " MB");
            UNIT_MAP.put('g', getRemainingGbFromKb(kilobytes) + " GB");
        } else {
            UNIT_MAP.put('k', kilobytes + " kilobajtów");
            UNIT_MAP.put('m', getRemainingMbFromKb(kilobytes) + " megabajtów");
            UNIT_MAP.put('g', getRemainingGbFromKb(kilobytes) + " gigabajtów");
        }

        return UNIT_MAP;
    }
}