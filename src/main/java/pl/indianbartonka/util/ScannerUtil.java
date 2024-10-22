package pl.indianbartonka.util;

import java.util.Scanner;
import pl.indianbartonka.util.annotation.UtilityClass;

/**
 * <p>
 * Utility class for handling user input through a {@link Scanner}.
 * Provides methods to ask various types of questions and process the user's responses.
 * </p>
 * <p>
 * Documents written by ChatGPT
 * </p>
 */

@UtilityClass
public final class ScannerUtil {

    private final Scanner scanner;

    /**
     * Constructs a {@code ScannerUtil} instance with the specified {@link Scanner}.
     *
     * @param scanner The {@link Scanner} to be used for input.
     */
    public ScannerUtil(final Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Asks a string question to the user, provides a default value if no input is given, and processes the response.
     *
     * @param question     A {@link StringResponseConsumer} that handles the question prompt.
     * @param defaultValue The default value to use if the user provides no input.
     * @param response     A {@link StringResponseConsumer} that handles the user's response.
     * @return The user's input or the default value if no input was provided.
     */
    public String addStringQuestion(final StringResponseConsumer question, final String defaultValue, final StringResponseConsumer response) {
        question.accept(defaultValue);
        String input = this.getInput();
        input = input.isEmpty() ? defaultValue : input;
        response.accept(input);

        return input;
    }

    /**
     * Asks a boolean question to the user, provides a default value if no input is given, and processes the response.
     *
     * @param question     A {@link BooleanResponseConsumer} that handles the question prompt.
     * @param defaultValue The default value to use if the user provides no input.
     * @param response     A {@link BooleanResponseConsumer} that handles the user's response.
     * @return The user's input or the default value if no input was provided.
     */
    public boolean addBooleanQuestion(final BooleanResponseConsumer question, final boolean defaultValue, final BooleanResponseConsumer response) {
        question.accept(defaultValue);
        final String input = this.getInput();
        final boolean userInput = input.isEmpty() ? defaultValue : Boolean.parseBoolean(input);
        response.accept(userInput);

        return userInput;
    }

    /**
     * Asks an integer question to the user, provides a default value if no input is given, and processes the response.
     *
     * @param question     A {@link IntegerResponseConsumer} that handles the question prompt.
     * @param defaultValue The default value to use if the user provides no input.
     * @param response     A {@link IntegerResponseConsumer} that handles the user's response.
     * @return The user's input or the default value if no input was provided.
     */
    public int addIntQuestion(final IntegerResponseConsumer question, final int defaultValue, final IntegerResponseConsumer response) {
        question.accept(defaultValue);
        final String input = this.getInput();
        final int userInput = input.isEmpty() ? defaultValue : Integer.parseInt(input);
        response.accept(userInput);

        return userInput;
    }

    /**
     * Asks a double question to the user, provides a default value if no input is given, and processes the response.
     *
     * @param question     A {@link DoubleResponseConsumer} that handles the question prompt.
     * @param defaultValue The default value to use if the user provides no input.
     * @param response     A {@link DoubleResponseConsumer} that handles the user's response.
     * @return The user's input or the default value if no input was provided.
     */
    public double addDoubleQuestion(final DoubleResponseConsumer question, final double defaultValue, final DoubleResponseConsumer response) {
        question.accept(defaultValue);
        final String input = this.getInput();
        final double userInput = input.isEmpty() ? defaultValue : Double.parseDouble(input);
        response.accept(userInput);

        return userInput;
    }

    /**
     * Returns the {@link Scanner} instance used by this utility.
     *
     * @return The {@link Scanner} instance.
     */
    public Scanner getScanner() {
        return this.scanner;
    }

    /**
     * Reads a line of input from the user.
     *
     * @return The user's input as a {@code String}.
     */
    private String getInput() {
        return this.scanner.nextLine();
    }

    //TODO: Dodac też getInput używając tam nextint itp a także usunąć na interface ma rzecz "Consumer"

    /**
     * Functional interface for handling string responses.
     */
    @FunctionalInterface
    public interface StringResponseConsumer {
        void accept(final String consumer);
    }

    /**
     * Functional interface for handling boolean responses.
     */
    @FunctionalInterface
    public interface BooleanResponseConsumer {
        void accept(final boolean consumer);
    }

    /**
     * Functional interface for handling integer responses.
     */
    @FunctionalInterface
    public interface IntegerResponseConsumer {
        void accept(final int consumer);
    }

    /**
     * Functional interface for handling double responses.
     */
    @FunctionalInterface
    public interface DoubleResponseConsumer {
        void accept(final double consumer);
    }
}
