package me.indian.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public final class MessageUtil {

    private static final Random random = new Random();
    private static final String CHARS_STRING = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM123456789@#*";

    private MessageUtil() {
    }

    public static String generateCode(final int length) {
        final StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(CHARS_STRING.charAt(random.nextInt(CHARS_STRING.length())));
        }

        return code.toString();
    }

    public static String buildMessageFromArgs(final String[] args) {
        return buildMessageFromArgs(args, null);
    }

    public static String buildMessageFromArgs(final String[] args, final String[] includeArgs) {
        if (args == null) return "";
        final StringBuilder message = new StringBuilder();
        for (final String arg : args) {
            if (includeArgs != null && Arrays.asList(includeArgs).contains(arg)) continue;
            message.append(arg).append(" ");
        }
        return message.toString().trim();
    }

    public static String[] stringToArgs(final String input) {
        return input.split("\\s+");
    }

    public static String[] removeFirstArgs(final String[] args) {
        return removeArgs(args, 1);
    }

    public static String[] removeArgs(final String[] args, final int startFrom) {
        if (args == null) return new String[]{};
        final String[] newArgs = new String[args.length - startFrom];
        System.arraycopy(args, startFrom, newArgs, 0, newArgs.length);

        return newArgs;
    }

    public static String listToSpacedString(final List<String> list) {
        return stringListToString(list, "\n");
    }

    public static String stringListToString(final List<String> list, String split) {
        if (split == null) split = "";
        if (list == null || list.isEmpty()) return "";
        if (list.size() == 1) return list.get(0);

        return String.join(split, list);
    }

    public static <T> String objectListToString(final List<T> list, String split) {
        if (split == null) split = "";
        if (list == null || list.isEmpty()) return "";
        if (list.size() == 1) return String.valueOf(list.get(0));

        return String.join(split, list.stream().map(Object::toString).toArray(String[]::new));
    }

    public static <E extends Enum<E>> String enumSetToString(final EnumSet<E> enumSet, String split) {
        if (split == null) split = "";
        if (enumSet == null || enumSet.isEmpty()) return "";

        return String.join(split, enumSet.stream().map(Enum::toString).toArray(String[]::new));
    }

    public static List<String> stringToStringList(final String text, String split) {
        if (split == null) split = "";
        if (text == null || text.isEmpty()) return new ArrayList<>();

        return Arrays.asList(text.split(split));
    }

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