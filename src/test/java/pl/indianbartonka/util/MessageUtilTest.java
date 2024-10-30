package pl.indianbartonka.util;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageUtilTest {

    @Test
    public void testGenerateCode() {
        final String code = MessageUtil.generateCode(10);
        assertEquals(10, code.length());
    }

    @Test
    public void testBuildMessageFromArgs() {
        final String[] args = {"Hello", "world", "!"};
        final String result = MessageUtil.buildMessageFromArgs(args);
        assertEquals("Hello world !", result);
    }

    @Test
    public void testBuildMessageFromArgsWithIncludeArgs() {
        final String[] args = {"Hello", "world", "!"};
        final String[] includeArgs = {"world"};
        final String result = MessageUtil.buildMessageFromArgs(args, includeArgs);
        assertEquals("Hello !", result);
    }

    @Test
    public void testFormatMessage() {
        final String message = "Hello %s!";
        final String result = MessageUtil.formatMessage(message, "world");
        assertEquals("Hello world!", result);
    }

    @Test
    public void testStringToArgs() {
        final String input = "Hello world!";
        final String[] result = MessageUtil.stringToArgs(input);
        assertArrayEquals(new String[]{"Hello", "world!"}, result);
    }

    @Test
    public void testRemoveFirstArgs() {
        final String[] args = {"a", "b", "c"};
        final String[] result = MessageUtil.removeFirstArgs(args);
        assertArrayEquals(new String[]{"b", "c"}, result);
    }

    @Test
    public void testRemoveArgsFromStart() {
        final String[] args = {"a", "b", "c"};
        final String[] result = MessageUtil.removeArgs(args, 1);
        assertArrayEquals(new String[]{"b", "c"}, result);
    }

    @Test
    public void testRemoveArgs() {
        final String[] args = {"a", "b", "c"};
        final String[] argsToRemove = {"b"};
        final String[] result = MessageUtil.removeArgs(args, argsToRemove);
        assertArrayEquals(new String[]{"a", "c"}, result);
    }

    @Test
    public void testListToSpacedString() {
        final String result = MessageUtil.listToSpacedString(Arrays.asList("Hello", "world"));
        assertEquals("Hello world", result);
    }

    @Test
    public void testListToNewLineString() {
        final String result = MessageUtil.listToNewLineString(Arrays.asList("Hello", "world"));
        assertEquals("Hello\nworld", result);
    }

    @Test
    public void testStringListToString() {
        final String result = MessageUtil.stringListToString(Arrays.asList("Hello", "world"), ", ");
        assertEquals("Hello, world", result);
    }

    @Test
    public void testObjectListToString() {
        final String result = MessageUtil.objectListToString(Arrays.asList(1, 2, 3), ", ");
        assertEquals("1, 2, 3", result);
    }

    @Test
    public void testEnumSetToString() {
        final EnumSet<SampleEnum> enumSet = EnumSet.of(SampleEnum.VALUE1, SampleEnum.VALUE2);
        final String result = MessageUtil.enumSetToString(enumSet, ", ");
        assertEquals("VALUE1, VALUE2", result);
    }

    @Test
    public void testStringToStringList() {
        final List<String> result = MessageUtil.stringToStringList("Hello world", " ");
        assertEquals(Arrays.asList("Hello", "world"), result);
    }

    @Test
    public void testGetStackTraceAsString() {
        final Exception exception = new Exception("Test exception");
        final String result = MessageUtil.getStackTraceAsString(exception);
        assertTrue(result.contains("Test exception"));
    }

    private enum SampleEnum {
        VALUE1, VALUE2
    }
}
