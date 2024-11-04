package pl.indianbartonka.util;

import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import org.jetbrains.annotations.CheckReturnValue;
import pl.indianbartonka.util.annotation.UtilityClass;

/**
 * <p>
 * Utility class for handling user input through a {@link Scanner}.
 * It provides methods to ask various types of questions and process the user's responses.
 * </p>
 * <p>
 * Documentation written by ChatGPT.
 * </p>
 */
@UtilityClass
public final class ScannerUtil {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private ScannerUtil() {

    }

    /**
     * Asks a string question to the user, provides a default value if no input is given,
     * and processes the user's response.
     *
     * @param question     A {@link Consumer} that handles the question prompt.
     * @param defaultValue The default value to return if the user provides no input.
     * @param response     A {@link Consumer} that handles the user's response.
     * @return The user's input or the default value if no input was provided.
     */
    @CheckReturnValue
    public static String addStringQuestion(final Consumer<String> question, final String defaultValue, final Consumer<String> response) {
        LOCK.lock();
        try {
            question.accept(defaultValue);
            String input = getInput();
            input = input.isEmpty() ? defaultValue : input;
            response.accept(input);
            return input;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Asks a boolean question to the user, provides a default value if no input is given,
     * and processes the user's response.
     *
     * @param question     A {@link Consumer} that handles the question prompt.
     * @param defaultValue The default value to return if the user provides no input.
     * @param response     A {@link Consumer} that handles the user's response.
     * @return The user's input as a boolean or the default value if no input was provided.
     */
    @CheckReturnValue
    public static boolean addBooleanQuestion(final Consumer<Boolean> question, final boolean defaultValue, final Consumer<Boolean> response) {
        LOCK.lock();
        try {
            question.accept(defaultValue);
            final String input = getInput();
            final boolean userInput = input.isEmpty() ? defaultValue : Boolean.parseBoolean(input);
            response.accept(userInput);
            return userInput;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Asks an integer question to the user, provides a default value if no input is given,
     * and processes the user's response.
     *
     * @param question     A {@link Consumer} that handles the question prompt.
     * @param defaultValue The default value to return if the user provides no input.
     * @param response     A {@link Consumer} that handles the user's response.
     * @return The user's input as an int or the default value if no input was provided.
     */
    @CheckReturnValue
    public static int addIntQuestion(final Consumer<Integer> question, final int defaultValue, final Consumer<Integer> response) {
        LOCK.lock();
        try {
            question.accept(defaultValue);
            final String input = getInput();
            final int userInput = input.isEmpty() ? defaultValue : Integer.parseInt(input);
            response.accept(userInput);
            return userInput;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Asks a long question to the user, provides a default value if no input is given,
     * and processes the user's response.
     *
     * @param question     A {@link Consumer} that handles the question prompt.
     * @param defaultValue The default value to return if the user provides no input.
     * @param response     A {@link Consumer} that handles the user's response.
     * @return The user's input as a long or the default value if no input was provided.
     */
    @CheckReturnValue
    public static long addLongQuestion(final Consumer<Long> question, final long defaultValue, final Consumer<Long> response) {
        LOCK.lock();
        try {
            question.accept(defaultValue);
            final String input = getInput();
            final long userInput = input.isEmpty() ? defaultValue : Long.parseLong(input);
            response.accept(userInput);
            return userInput;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Asks a double question to the user, provides a default value if no input is given,
     * and processes the user's response.
     *
     * @param question     A {@link Consumer} that handles the question prompt.
     * @param defaultValue The default value to return if the user provides no input.
     * @param response     A {@link Consumer} that handles the user's response.
     * @return The user's input as a double or the default value if no input was provided.
     */
    @CheckReturnValue
    public static double addDoubleQuestion(final Consumer<Double> question, final double defaultValue, final Consumer<Double> response) {
        LOCK.lock();
        try {
            question.accept(defaultValue);
            final String input = getInput();
            final double userInput = input.isEmpty() ? defaultValue : Double.parseDouble(input);
            response.accept(userInput);
            return userInput;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Reads a line of input from the user.
     *
     * @return The user's input as a {@code String}.
     */
    private static String getInput() {
        return SCANNER.nextLine();
    }
}
