package pl.indianbartonka.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import pl.indianbartonka.util.annotation.UtilityClass;

/**
 * The MessageUtil class provides utility methods for generating messages,
 * formatting strings, manipulating arguments, and converting data types.
 */
@UtilityClass
public final class MessageUtil {

    private static final String CHARS_STRING = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM123456789@#*";

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private MessageUtil() {
    }

    /**
     * Generates a random code of the specified length from allowed characters.
     *
     * @param length The length of the code to generate.
     * @return A random code as a string.
     */
    public static String generateCode(final int length) {
        final StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(CHARS_STRING.charAt(MathUtil.RANDOM.nextInt(CHARS_STRING.length())));
        }
        return code.toString();
    }

    /**
     * Builds a message from an array of arguments, ignoring arguments in the includeArgs array.
     *
     * @param args The array of arguments to build the message from.
     * @return The constructed message as a string.
     */
    public static String buildMessageFromArgs(final String[] args) {
        return buildMessageFromArgs(args, null);
    }

    /**
     * Builds a message from an array of arguments, with optional inclusion/exclusion of specified arguments.
     *
     * @param args        The array of arguments to build the message from.
     * @param includeArgs The array of arguments to exclude.
     * @return The constructed message as a string.
     */
    public static String buildMessageFromArgs(final String[] args, final String[] includeArgs) {
        if (args == null) return "";
        final StringBuilder message = new StringBuilder();
        for (final String arg : args) {
            if (includeArgs != null && Arrays.asList(includeArgs).contains(arg)) continue;
            message.append(arg).append(" ");
        }
        return message.toString().trim();
    }

    /**
     * Formats a message by replacing the first occurrence of "%s" with the provided arguments.
     *
     * @param message    The message to format.
     * @param formatArgs The arguments to replace in the message.
     * @return The formatted message as a string.
     */
    public static String formatMessage(String message, final Object... formatArgs) {
        for (final Object object : formatArgs) {
            message = message.replaceFirst("%s", object.toString());
        }
        return message;
    }

    /**
     * Converts a string to an array of arguments.
     *
     * @param input The string to convert.
     * @return An array of arguments.
     */
    public static String[] stringToArgs(final String input) {
        return input.split("\\s+");
    }

    /**
     * Removes the first argument from the array of arguments.
     *
     * @param args The array of arguments.
     * @return An array of arguments after removing the first element.
     */
    public static String[] removeFirstArgs(final String[] args) {
        return removeArgs(args, 1);
    }

    /**
     * Removes arguments starting from the specified index.
     *
     * @param args      The array of arguments.
     * @param startFrom The index from which to start removing arguments.
     * @return An array of arguments after removal.
     */
    public static String[] removeArgs(final String[] args, final int startFrom) {
        if (args == null || args.length <= startFrom) return new String[]{};
        return Arrays.copyOfRange(args, startFrom, args.length);
    }

    /**
     * Removes from the array of arguments those that are present in the argsToRemove array.
     *
     * @param args         The array of arguments.
     * @param argsToRemove The array of arguments to remove.
     * @return An array of arguments after removal.
     */
    public static String[] removeArgs(final String[] args, final String[] argsToRemove) {
        if (args == null || argsToRemove == null) return args;

        return Arrays.stream(args)
                .filter(arg -> !Arrays.asList(argsToRemove).contains(arg))
                .toArray(String[]::new);
    }

    /**
     * Converts a list of strings into a single string separated by spaces.
     *
     * @param list The list of strings to join.
     * @return The joined string.
     */
    public static String listToSpacedString(final List<String> list) {
        return stringListToString(list, " ");
    }

    /**
     * Converts a list of strings into a single string separated by new lines.
     *
     * @param list The list of strings to join.
     * @return The joined string.
     */
    public static String listToNewLineString(final List<String> list) {
        return stringListToString(list, "\n");
    }

    /**
     * Converts a list of strings into a single string, using the specified delimiter.
     *
     * @param list  The list of strings to join.
     * @param split The delimiter to use for joining.
     * @return The joined string.
     */
    public static String stringListToString(final List<String> list, String split) {
        if (split == null) split = "";
        if (list == null || list.isEmpty()) return "";
        return String.join(split, list);
    }

    /**
     * Converts a list of objects into a single string, using the specified delimiter.
     *
     * @param list  The list of objects to join.
     * @param split The delimiter to use for joining.
     * @param <T>   The type of the objects in the list.
     * @return The joined string.
     */
    public static <T> String objectListToString(final List<T> list, String split) {
        if (split == null) split = "";
        if (list == null || list.isEmpty()) return "";
        return String.join(split, list.stream().map(Object::toString).toArray(String[]::new));
    }

    /**
     * Converts an EnumSet into a single string, using the specified delimiter.
     *
     * @param enumSet The EnumSet to convert.
     * @param split   The delimiter to use for joining.
     * @param <E>     The type of the enum.
     * @return The joined string.
     */
    public static <E extends Enum<E>> String enumSetToString(final Set<E> enumSet, String split) {
        if (split == null) split = "";
        if (enumSet == null || enumSet.isEmpty()) return "";
        return String.join(split, enumSet.stream().map(Enum::toString).toArray(String[]::new));
    }

    /**
     * Converts a string into a list of strings based on the specified delimiter.
     *
     * @param text  The string to convert.
     * @param split The delimiter to use for splitting.
     * @return A list of strings.
     */
    public static List<String> stringToStringList(final String text, String split) {
        if (split == null) split = "\\s+";
        if (text == null || text.isEmpty()) return new ArrayList<>();
        return Arrays.asList(text.split(split));
    }

    /**
     * Converts the stack trace of a Throwable into a string.
     *
     * @param throwable The throwable to convert.
     * @return The stack trace as a string.
     */
    public static String getStackTraceAsString(final Throwable throwable) {
        if (throwable == null) return "";
        final StringBuilder stackTraceBuilder = new StringBuilder();
        stackTraceBuilder.append(throwable.getClass().getName()).append(": ").append(throwable.getMessage()).append("\n");
        for (final StackTraceElement element : throwable.getStackTrace()) {
            stackTraceBuilder.append(element.toString()).append("\n");
        }
        return stackTraceBuilder.toString();
    }
}
