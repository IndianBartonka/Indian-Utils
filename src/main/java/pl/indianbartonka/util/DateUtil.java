package pl.indianbartonka.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for date and time operations.
 * Provides methods for formatting and converting between different time units.
 * Documents written by Chat-GPT
 */
public final class DateUtil {

    /**
     * Some EU time zones
     */
    public static final ZoneId POLISH_ZONE = ZoneId.of("Europe/Warsaw");
    public static final ZoneId LONDON_ZONE = ZoneId.of("Europe/London");
    public static final ZoneId PARIS_ZONE = ZoneId.of("Europe/Paris");
    public static final ZoneId BERLIN_ZONE = ZoneId.of("Europe/Berlin");
    public static final ZoneId MADRID_ZONE = ZoneId.of("Europe/Madrid");
    public static final ZoneId ROME_ZONE = ZoneId.of("Europe/Rome");
    public static final ZoneId AMSTERDAM_ZONE = ZoneId.of("Europe/Amsterdam");
    public static final ZoneId VIENNA_ZONE = ZoneId.of("Europe/Vienna");
    public static final ZoneId ATHENS_ZONE = ZoneId.of("Europe/Athens");
    public static final ZoneId BRUSSELS_ZONE = ZoneId.of("Europe/Brussels");
    public static final ZoneId BUDAPEST_ZONE = ZoneId.of("Europe/Budapest");
    public static final ZoneId BUCHAREST_ZONE = ZoneId.of("Europe/Bucharest");
    public static final ZoneId COPENHAGEN_ZONE = ZoneId.of("Europe/Copenhagen");
    public static final ZoneId DUBLIN_ZONE = ZoneId.of("Europe/Dublin");
    public static final ZoneId HELSINKI_ZONE = ZoneId.of("Europe/Helsinki");
    public static final ZoneId LISBON_ZONE = ZoneId.of("Europe/Lisbon");
    public static final ZoneId LUXEMBOURG_ZONE = ZoneId.of("Europe/Luxembourg");
    public static final ZoneId MOSCOW_ZONE = ZoneId.of("Europe/Moscow");
    public static final ZoneId OSLO_ZONE = ZoneId.of("Europe/Oslo");
    public static final ZoneId PRAGUE_ZONE = ZoneId.of("Europe/Prague");
    public static final ZoneId SOFIA_ZONE = ZoneId.of("Europe/Sofia");
    public static final ZoneId STOCKHOLM_ZONE = ZoneId.of("Europe/Stockholm");
    public static final ZoneId ZURICH_ZONE = ZoneId.of("Europe/Zurich");
    public static final ZoneId ISTANBUL_ZONE = ZoneId.of("Europe/Istanbul");
    public static final ZoneId KIEV_ZONE = ZoneId.of("Europe/Kiev");
    public static final ZoneId VILNIUS_ZONE = ZoneId.of("Europe/Vilnius");
    public static final ZoneId TALLINN_ZONE = ZoneId.of("Europe/Tallinn");
    public static final ZoneId RIGA_ZONE = ZoneId.of("Europe/Riga");
    /**
     * Map for storing time unit strings.
     */
    private static final Map<Character, String> UNIT_MAP = new HashMap<>();
    /**
     * The time zone used for date and time operations (Europe/Warsaw).
     */
    public static ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    // Private constructor to prevent instantiation
    private DateUtil() {
    }

    /**
     * Method to set default ZoneID
     *
     * @param zoneId - ID of zone
     */
    private static void setZone(final ZoneId zoneId) {
        DEFAULT_ZONE = zoneId;
    }

    /**
     * Gets the current date and time in "yyyy-MM-dd HH:mm:ss" format, with colons replaced by hyphens.
     *
     * @return The formatted date and time as a string.
     */
    public static String getFixedDate() {
        return LocalDateTime.now(DEFAULT_ZONE).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).replace(":", "-");
    }

    /**
     * Gets the current date and time in "yyyy-MM-dd HH:mm:ss" format.
     *
     * @return The formatted date and time as a string.
     */
    public static String getDate() {
        return LocalDateTime.now(DEFAULT_ZONE).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Gets the current time in "HH:mm" format.
     *
     * @return The formatted time as a string.
     */
    public static String getTimeHM() {
        return LocalDateTime.now(DEFAULT_ZONE).format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Gets the current time in "HH:mm:ss" format.
     *
     * @return The formatted time as a string.
     */
    public static String getTimeHMS() {
        return LocalDateTime.now(DEFAULT_ZONE).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * Gets the current time in "HH:mm:ss:SSS" format, including milliseconds.
     *
     * @return The formatted time as a string.
     */
    public static String getTimeHMSMS() {
        return LocalDateTime.now(DEFAULT_ZONE).format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
    }

    /**
     * Converts a {@link LocalDateTime} to epoch seconds.
     *
     * @param localDateTime The {@link LocalDateTime} to convert.
     * @return The epoch seconds.
     */
    public static long localDateTimeToSeconds(final LocalDateTime localDateTime) {
        return localDateTime.atZone(DEFAULT_ZONE).toEpochSecond();
    }

    /**
     * Converts a {@link LocalDateTime} to epoch millis.
     *
     * @param localDateTime The {@link LocalDateTime} to convert.
     * @return The epoch millis.
     */
    public static long localDateTimeToMillis(final LocalDateTime localDateTime) {
        return localDateTime.atZone(DEFAULT_ZONE).toInstant().toEpochMilli();
    }

    /**
     * Converts epoch seconds to a {@link LocalDateTime}.
     *
     * @param seconds The epoch seconds to convert.
     * @return The corresponding {@link LocalDateTime}.
     */
    public static LocalDateTime secondsToLocalDateTime(final long seconds) {
        return Instant.ofEpochSecond(seconds).atZone(DEFAULT_ZONE).toLocalDateTime();
    }

    /**
     * Converts epoch millis to a {@link LocalDateTime}.
     *
     * @param millis The epoch millis to convert.
     * @return The corresponding {@link LocalDateTime}.
     */
    public static LocalDateTime millisToLocalDateTime(final long millis) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(millis),
                DEFAULT_ZONE
        );
    }

    /**
     * Converts a time value from the specified source unit to days.
     *
     * @param time       The time value to convert.
     * @param sourceUnit The source {@link TimeUnit}.
     * @return The equivalent time in days.
     */
    public static long daysFrom(final long time, final TimeUnit sourceUnit) {
        return sourceUnit.toDays(time);
    }

    /**
     * Converts a time value in days to the specified target unit.
     *
     * @param time       The time value to convert.
     * @param targetUnit The target {@link TimeUnit}.
     * @return The equivalent time in the target unit.
     */
    public static long daysTo(final long time, final TimeUnit targetUnit) {
        return targetUnit.convert(time, TimeUnit.DAYS);
    }

    /**
     * Converts a time value from the specified source unit to hours.
     *
     * @param time       The time value to convert.
     * @param sourceUnit The source {@link TimeUnit}.
     * @return The equivalent time in hours.
     */
    public static long hoursFrom(final long time, final TimeUnit sourceUnit) {
        return sourceUnit.toHours(time);
    }

    /**
     * Converts a time value in hours to the specified target unit.
     *
     * @param time       The time value to convert.
     * @param targetUnit The target {@link TimeUnit}.
     * @return The equivalent time in the target unit.
     */
    public static long hoursTo(final long time, final TimeUnit targetUnit) {
        return targetUnit.convert(time, TimeUnit.HOURS);
    }

    /**
     * Converts a time value from the specified source unit to minutes.
     *
     * @param time       The time value to convert.
     * @param sourceUnit The source {@link TimeUnit}.
     * @return The equivalent time in minutes.
     */
    public static long minutesFrom(final long time, final TimeUnit sourceUnit) {
        return sourceUnit.toMinutes(time);
    }

    /**
     * Converts a time value in minutes to the specified target unit.
     *
     * @param time       The time value to convert.
     * @param targetUnit The target {@link TimeUnit}.
     * @return The equivalent time in the target unit.
     */
    public static long minutesTo(final long time, final TimeUnit targetUnit) {
        return targetUnit.convert(time, TimeUnit.MINUTES);
    }

    /**
     * Converts a time value from the specified source unit to milliseconds.
     *
     * @param time       The time value to convert.
     * @param sourceUnit The source {@link TimeUnit}.
     * @return The equivalent time in milliseconds.
     */
    public static long millisFrom(final long time, final TimeUnit sourceUnit) {
        return sourceUnit.toMillis(time);
    }

    /**
     * Converts a time value in milliseconds to the specified target unit.
     *
     * @param time       The time value to convert.
     * @param targetUnit The target {@link TimeUnit}.
     * @return The equivalent time in the target unit.
     */
    public static long millisTo(final long time, final TimeUnit targetUnit) {
        return targetUnit.convert(time, TimeUnit.MILLISECONDS);
    }

    /**
     * Converts seconds to milliseconds.
     *
     * @param seconds The number of seconds to convert.
     * @return The equivalent time in milliseconds.
     */
    public static long secondToMillis(final long seconds) {
        return Duration.ofSeconds(seconds).toMillis();
    }

    /**
     * Calculates the number of days in a given time duration in milliseconds.
     *
     * @param millis The time duration in milliseconds.
     * @return The number of days.
     */
    private static long formatDays(final long millis) {
        final long totalSeconds = millis / 1000;
        final long totalMinutes = totalSeconds / 60;
        final long totalHours = totalMinutes / 60;

        return totalHours / 24;
    }

    /**
     * Calculates the number of hours (excluding full days) in a given time duration in milliseconds.
     *
     * @param millis The time duration in milliseconds.
     * @return The number of hours.
     */
    private static long formatHours(final long millis) {
        final long totalSeconds = millis / 1000;
        final long totalMinutes = totalSeconds / 60;
        final long totalHours = totalMinutes / 60;

        return totalHours % 24;
    }

    /**
     * Calculates the number of minutes (excluding full hours) in a given time duration in milliseconds.
     *
     * @param millis The time duration in milliseconds.
     * @return The number of minutes.
     */
    private static long formatMinutes(final long millis) {
        final long totalSeconds = millis / 1000;
        final long totalMinutes = totalSeconds / 60;

        return totalMinutes % 60;
    }

    /**
     * Calculates the number of seconds (excluding full minutes) in a given time duration in milliseconds.
     *
     * @param millis The time duration in milliseconds.
     * @return The number of seconds.
     */
    private static long formatSeconds(final long millis) {
        return (millis / 1000) % 60;
    }

    /**
     * Formats a time duration in milliseconds into a human-readable string with days, hours, minutes, and seconds.
     * Defaults to full names if shortNames is false.
     *
     * @param millis The time duration in milliseconds.
     * @return The formatted time string.
     */
    public static String formatTimeDynamic(final long millis) {
        return formatTimeDynamic(millis, false);
    }

    /**
     * Formats a time duration in milliseconds into a human-readable string with days, hours, minutes, seconds, and milliseconds.
     * Allows for short names if specified.
     *
     * @param millis     The time duration in milliseconds.
     * @param shortNames If true, uses short names for units; otherwise, uses full names.
     * @return The formatted time string.
     */
    public static String formatTimeDynamic(final long millis, final boolean shortNames) {
        if (millis == 0) return "N/A";

        final List<Character> unitsPattern = new ArrayList<>();
        final long days = formatDays(millis);
        final long hours = formatHours(millis);
        final long minutes = formatMinutes(millis);
        final long seconds = formatSeconds(millis);
        final long formatedMillis = millis % 1000;

        if (days > 0) unitsPattern.add('d');
        if (hours > 0) unitsPattern.add('h');
        if (minutes > 0) unitsPattern.add('m');
        if (seconds > 0) unitsPattern.add('s');
        if (formatedMillis > 0) unitsPattern.add('i');

        return formatTime(millis, unitsPattern, shortNames);
    }

    /**
     * Formats a time duration in milliseconds into a human-readable string based on the specified unit pattern.
     * Allows for short names if specified.
     *
     * @param millis       The time duration in milliseconds.
     * @param unitsPattern A list of characters representing the time units to include in the output.
     * @param shortNames   If true, uses short names for units; otherwise, uses full names.
     * @return The formatted time string.
     */
    public static String formatTime(final long millis, final List<Character> unitsPattern, final boolean shortNames) {
        final StringBuilder formattedTime = new StringBuilder();
        final Map<Character, String> unitMap = getUnitMap(millis, shortNames);

        for (final char unit : unitsPattern) {
            if (unitMap.containsKey(unit)) {
                formattedTime.append(unitMap.get(unit)).append(" ");
            }
        }

        return formattedTime.toString().trim();
    }

    /**
     * Formats a time duration in milliseconds into a human-readable string based on the specified unit pattern.
     * Uses full names for units by default.
     *
     * @param millis       The time duration in milliseconds.
     * @param unitsPattern A list of characters representing the time units to include in the output.
     * @return The formatted time string.
     */
    public static String formatTime(final long millis, final List<Character> unitsPattern) {
        return formatTime(millis, unitsPattern, false);
    }

    /**
     * Builds a map of time units and their corresponding formatted strings based on the given time duration and short name preference.
     *
     * @param millis     The time duration in milliseconds.
     * @param shortNames If true, uses short names for units; otherwise, uses full names.
     * @return A map of time units and their formatted strings.
     */
    private static Map<Character, String> getUnitMap(final long millis, final boolean shortNames) {
        UNIT_MAP.clear();
        if (shortNames) {
            UNIT_MAP.put('d', formatDays(millis) + "dni");
            UNIT_MAP.put('h', formatHours(millis) + "godz");
            UNIT_MAP.put('m', formatMinutes(millis) + "min");
            UNIT_MAP.put('s', formatSeconds(millis) + "s");
            UNIT_MAP.put('i', millis % 1000 + "ms");
        } else {
            UNIT_MAP.put('d', formatDays(millis) + " dni");
            UNIT_MAP.put('h', formatHours(millis) + " godzin");
            UNIT_MAP.put('m', formatMinutes(millis) + " minut");
            UNIT_MAP.put('s', formatSeconds(millis) + " sekund");
            UNIT_MAP.put('i', millis % 1000 + " milisekund");
        }
        return UNIT_MAP;
    }
}