package pl.indianbartonka.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.jetbrains.annotations.VisibleForTesting;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.annotation.UtilityClass;

/**
 * <p>
 * Utility class providing mathematical operations, data conversions, and formatting functions.
 * </p>
 * <p>
 * Documents and math operations written by ChatGPT
 * </p>
 */

@UtilityClass
public final class MathUtil {

    /**
     * The singleton instance of {@link Random} used for generating random numbers.
     */
    public static final Random RANDOM = new Random();

    /**
     * Static instance of DecimalFormat for formatting numbers.
     */
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    //Static initializer to configure DecimalFormat with English locale symbols.
    static {
        final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DECIMAL_FORMAT.setDecimalFormatSymbols(decimalFormatSymbols);
        DECIMAL_FORMAT.setGroupingUsed(false);
    }

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private MathUtil() {
    }

    /**
     * Returns a random element from the provided list.
     *
     * @param list the list from which to select a random element
     * @param <T>  the type of elements in the list
     * @return a randomly selected element from the list
     * @throws IllegalArgumentException if the list is empty
     */
    public static <T> T getRandomElement(final List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        return list.get(RANDOM.nextInt(list.size()));
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
     * Clamps long number to be within the specified minimum and maximum range.
     *
     * @param number The number to clamp.
     * @param min    The minimum allowable value.
     * @param max    The maximum allowable value.
     * @return The clamped value within the range.
     */
    public static long getCorrectNumber(final long number, final long min, final long max) {
        return Math.max(min, Math.min(max, number));
    }

    /**
     * Clamps float number to be within the specified minimum and maximum range.
     *
     * @param number The number to clamp.
     * @param min    The minimum allowable value.
     * @param max    The maximum allowable value.
     * @return The clamped value within the range.
     */
    public static float getCorrectNumber(final float number, final float min, final float max) {
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

    // Methods to extract remaining units

    /**
     * Extracts the remaining gigabytes from a total number of bytes.
     *
     * @param bytes The number of bytes.
     * @return The remaining gigabytes.
     */
    public static long getRemainingGibibytesFromTotalBytes(final long bytes) {
        return (bytes % (1024L * 1024 * 1024 * 1024)) / (1024 * 1024 * 1024);
    }

    /**
     * Extracts the remaining terabytes from a total number of bytes.
     *
     * @param bytes The number of bytes.
     * @return The remaining terabytes.
     */
    public static long getRemainingTebibytesFromTotalBytes(final long bytes) {
        return (bytes % (1024L * 1024 * 1024 * 1024 * 1024)) / (1024L * 1024 * 1024 * 1024);
    }

    /**
     * Extracts the remaining megabytes from a total number of bytes.
     *
     * @param bytes The number of bytes.
     * @return The remaining megabytes.
     */
    public static long getRemainingMebibytesFromTotalBytes(final long bytes) {
        return (bytes % (1024 * 1024 * 1024)) / (1024 * 1024);
    }

    /**
     * Extracts the remaining kilobytes from a total number of bytes.
     *
     * @param bytes The number of bytes.
     * @return The remaining kilobytes.
     */
    public static long getRemainingKibibytesFromTotalBytes(final long bytes) {
        return (bytes % (1024 * 1024)) / 1024;
    }

    /**
     * Extracts the remaining bytes from a total number of bytes, after accounting for complete kilobytes.
     * This method calculates the remainder of bytes that don't form a full kilobyte (1024 bytes).
     *
     * @param bytes The total number of bytes.
     * @return The remaining bytes, which are les than 1024.
     */
    public static long getRemainingBytesFromTotalBytes(final long bytes) {
        return bytes % 1024;
    }

    /**
     * Extracts the remaining Gibibytes from a total number of kilobytes,
     * after counting full terabytes.
     *
     * @param kilobytes The number of kilobytes.
     * @return The remaining Gibibytes.
     */
    public static long getRemainingGibibytesFromTotalKibibytes(final long kilobytes) {
        return (kilobytes % (1024L * 1024 * 1024)) / (1024 * 1024);
    }

    /**
     * Extracts the remaining terabytes from a total number of kilobytes.
     *
     * @param kilobytes The number of kilobytes.
     * @return The remaining terabytes.
     */
    public static long getRemainingTebibytesFromTotalKibibytes(final long kilobytes) {
        return (kilobytes % (1024L * 1024 * 1024 * 1024)) / (1024L * 1024 * 1024);
    }


    /**
     * Extracts the remaining megabytes from a total number of kilobytes.
     *
     * @param kilobytes The number of kilobytes.
     * @return The remaining megabytes.
     */
    public static long getRemainingMebibytesFromTotalKibibytes(final long kilobytes) {
        return (kilobytes % (1024 * 1024)) / 1024;
    }

    /**
     * Returns the remaining kilobytes from the total number of kilobytes after full megabytes.
     *
     * @param kilobytes the total number of kilobytes
     * @return the remaining kilobytes after full megabytes
     */
    public static long getRemainingKibibytesFromTotalKibibytes(final long kilobytes) {
        return kilobytes % 1024;
    }

    // Formatting methods

    /**
     * Formats a double value to the specified number of decimal places.
     *
     * @param decimal The decimal number to format.
     * @param format  The number of decimal places.
     * @return The formatted double value.
     */
    public static double formatDecimal(final double decimal, final int format) {
        DECIMAL_FORMAT.setMaximumFractionDigits(format);
        return Double.parseDouble(DECIMAL_FORMAT.format(decimal));
    }

    /**
     * Formats kilobytes dynamically based on their magnitude, using either short or long names.
     *
     * @param kilobytes  The number of kilobytes to format.
     * @param shortNames If true, uses short names (e.g., KB, MB, GB); otherwise uses long names (e.g., kilobytes, megabytes, Gibibytes).
     * @param spaceBetweenUnits If true, adding spaces between units.
     * @return The formatted kilobytes as a string.
     */
    public static String formatKibibytesDynamic(final long kilobytes, final boolean shortNames, final boolean spaceBetweenUnits) {
        if (kilobytes < 0) return "N/A";

        final List<Character> unitsPattern = new ArrayList<>();

        final long tb = getRemainingTebibytesFromTotalKibibytes(kilobytes);
        final long gb = getRemainingGibibytesFromTotalKibibytes(kilobytes);
        final long mb = getRemainingMebibytesFromTotalKibibytes(kilobytes);
        final long kb = getRemainingKibibytesFromTotalKibibytes(kilobytes);

        if (kilobytes == 0) unitsPattern.add('k');

        if (tb > 0) unitsPattern.add('t');
        if (gb > 0) unitsPattern.add('g');
        if (mb > 0) unitsPattern.add('m');
        if (kb > 0) unitsPattern.add('k');

        return formatKibibytes(kilobytes, unitsPattern, shortNames, spaceBetweenUnits);
    }


    /**
     * Formats kilobytes dynamically based on their magnitude, using either short or long names.
     *
     * @param kilobytes The number of kilobytes to format.
     * @return The formatted kilobytes as a string.
     */
    @Since("0.0.9.3")
    public static String formatKibibytesDynamic(final long kilobytes) {
        return formatKibibytesDynamic(kilobytes, true, true);
    }

    /**
     * Formats bytes dynamically based on their magnitude, using either short or long names.
     *
     * @param bytes The number of bytes to format.
     * @return The formatted bytes as a string.
     */
    @Since("0.0.9.3")
    public static String formatBytesDynamic(final long bytes) {
        return formatBytesDynamic(bytes, true);
    }

    /**
     * Formats bytes dynamically based on their magnitude, using either short or long names.
     *
     * @param bytes      The number of bytes to format.
     * @param shortNames If true, uses short names (e.g., KB, MB, GB); otherwise uses long names (e.g., kilobytes, megabytes, Gibibytes).
     * @return The formatted bytes as a string.
     */
    public static String formatBytesDynamic(final long bytes, final boolean shortNames) {
        return formatBytesDynamic(bytes, shortNames, true);
    }

    /**
     * Formats bytes dynamically based on their magnitude, using either short or long names.
     *
     * @param bytes      The number of bytes to format.
     * @param shortNames If true, uses short names (e.g., KB, MB, GB); otherwise uses long names (e.g., kilobytes, megabytes, Gibibytes).
     * @param spaceBetweenUnits If true, adding spaces between units.
     * @return The formatted bytes as a string.
     */
    public static String formatBytesDynamic(final long bytes, final boolean shortNames, final boolean spaceBetweenUnits) {
        if (bytes < 0) return "N/A";

        final List<Character> unitsPattern = new ArrayList<>();

        final long tb = getRemainingTebibytesFromTotalBytes(bytes);
        final long gb = getRemainingGibibytesFromTotalBytes(bytes);
        final long mb = getRemainingMebibytesFromTotalBytes(bytes);
        final long kb = getRemainingKibibytesFromTotalBytes(bytes);
        final long remainingBytes = getRemainingBytesFromTotalBytes(bytes);

        if (bytes == 0) unitsPattern.add('b');

        if (tb > 0) unitsPattern.add('t');
        if (gb > 0) unitsPattern.add('g');
        if (mb > 0) unitsPattern.add('m');
        if (kb > 0) unitsPattern.add('k');
        if (remainingBytes > 0) unitsPattern.add('b');

        return formatBytes(bytes, unitsPattern, shortNames, spaceBetweenUnits);
    }

    /**
     * Formats kilobytes according to the specified units pattern and naming convention.
     *
     * @param kilobytes    The number of kilobytes to format.
     * @param unitsPattern The units pattern to use (e.g., 'k', 'm', 'g').
     * @param shortNames   If true, uses short names; otherwise uses long names.
     * @param spaceBetweenUnits If true, adding spaces between units.
     * @return The formatted kilobytes as a string.
     */
    @VisibleForTesting
    public static String formatKibibytes(final long kilobytes, final List<Character> unitsPattern, final boolean shortNames, final boolean spaceBetweenUnits) {
        final StringBuilder formattedKibibytes = new StringBuilder();
        final Map<Character, String> unitMap = getUnitKibibytesMap(kilobytes, shortNames, spaceBetweenUnits);

        for (final char unit : unitsPattern) {
            if (unitMap.containsKey(unit)) {
                formattedKibibytes.append(unitMap.get(unit)).append(" ");
            }
        }

        return formattedKibibytes.toString().trim();
    }

    /**
     * Formats bytes according to the specified units pattern and naming convention.
     *
     * @param bytes        The number of bytes to format.
     * @param unitsPattern The units pattern to use (e.g., 'b', 'k', 'm', 'g').
     * @param shortNames   If true, uses short names; otherwise uses long names.
     * @param spaceBetweenUnits If true, adding spaces between units.
     * @return The formatted bytes as a string.
     */
    @VisibleForTesting
    public static String formatBytes(final long bytes, final List<Character> unitsPattern, final boolean shortNames, final boolean spaceBetweenUnits) {
        final StringBuilder formattedBytes = new StringBuilder();
        final Map<Character, String> unitMap = getUnitBytesMap(bytes, shortNames, spaceBetweenUnits);

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
     * @param spaceBetweenUnits If true, adding spaces between units.
     * @return A map of unit characters to their string representations.
     */
    private static Map<Character, String> getUnitBytesMap(final long bytes, final boolean shortNames, final boolean spaceBetweenUnits) {
        final Map<Character, String> unitMap = new HashMap<>();

        final long b = getRemainingBytesFromTotalBytes(bytes);
        final long kb = getRemainingKibibytesFromTotalBytes(bytes);
        final long mb = getRemainingMebibytesFromTotalBytes(bytes);
        final long gb = getRemainingGibibytesFromTotalBytes(bytes);
        final long tb = getRemainingTebibytesFromTotalBytes(bytes);

        final String space = (spaceBetweenUnits ? " " : "");

        if (shortNames) {
            unitMap.put('k', kb + space + "KB");
            unitMap.put('m', mb + space + "MB");
            unitMap.put('g', gb + space + "GB");
            unitMap.put('t', tb + space + "TB");
            unitMap.put('b', b + space + "B");
        } else {
            unitMap.put('k', kb + space + "kilobajtów");
            unitMap.put('m', mb + space + "megabajtów");
            unitMap.put('g', gb + space + "gigabajtów");
            unitMap.put('t', tb + space + "terabajtów");
            unitMap.put('b', b + space + "bajtów");

            if (bytes == 0) {
                unitMap.put('b', b + " bajtów");
            }
        }

        if (bytes == 0) {
            if (shortNames) {
                unitMap.put('b', b + " B");
            } else {
                unitMap.put('b', b + " bajtów");
            }
        }

        return unitMap;
    }

    /**
     * Provides a mapping of unit characters to their string representations for kilobytes.
     *
     * @param kilobytes  The number of kilobytes.
     * @param shortNames If true, uses short names; otherwise uses long names.
     * @param spaceBetweenUnits If true, adding spaces between units.
     * @return A map of unit characters to their string representations.
     */
    private static Map<Character, String> getUnitKibibytesMap(final long kilobytes, final boolean shortNames, final boolean spaceBetweenUnits) {
        final Map<Character, String> unitMap = new HashMap<>();

        final long tb = getRemainingTebibytesFromTotalKibibytes(kilobytes);
        final long gb = getRemainingGibibytesFromTotalKibibytes(kilobytes);
        final long mb = getRemainingMebibytesFromTotalKibibytes(kilobytes);
        final long kb = getRemainingKibibytesFromTotalKibibytes(kilobytes);

        final String space = (spaceBetweenUnits ? " " : "");

        if (shortNames) {
            unitMap.put('t', tb + space + "TB");
            unitMap.put('g', gb + space + "GB");
            unitMap.put('m', mb + space + "MB");
            unitMap.put('k', kb + space + "KB");
        } else {
            unitMap.put('t', tb + space + "terabajtów");
            unitMap.put('g', gb + space + "gigabajtów");
            unitMap.put('m', mb + space + "megabajtów");
            unitMap.put('k', kb + space + "kilobajtów");
        }

        if (kilobytes == 0) {
            if (shortNames) {
                unitMap.put('k', kb + space + "KB");
            } else {
                unitMap.put('k', kb + space + "kilobajtów");
            }
        }

        return unitMap;
    }

}
