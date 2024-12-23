package pl.indianbartonka.util.color;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import pl.indianbartonka.util.annotation.UtilityClass;

@UtilityClass
public final class AnsiColor {

    /**
     * Special formating
     */
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\033[1m";
    public static final String OBFUSCATED = "\033[8m";
    public static final String ITALIC = "\033[3m";
    public static final String UNDERLINE = "\033[4m";
    public static final String STRIKETHROUGH = "\033[9m";

    /**
     * Default colors
     */
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[0;93m";
    public static final String GOLD = "\u001B[0;33m";
    public static final String DARK_BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String LIGHT_PURPLE = "\u001B[0;95m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[0;97m";
    public static final String BLUE = "\u001B[94m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";
    public static final String BRIGHT_GRAY = "\u001B[37m";
    public static final String DARK_GRAY = "\u001B[90m";
    public static final String LIGHT_GRAY = "\u001B[37;1m";
    public static final String DARK_RED = "\u001B[31;1m";

    /**
     * Backgorund colors
     */
    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";
    public static final String BRIGHT_BLACK_BACKGROUND = "\u001B[100m";
    public static final String BRIGHT_RED_BACKGROUND = "\u001B[101m";
    public static final String BRIGHT_GREEN_BACKGROUND = "\u001B[102m";
    public static final String BRIGHT_YELLOW_BACKGROUND = "\u001B[103m";
    public static final String BRIGHT_BLUE_BACKGROUND = "\u001B[104m";
    public static final String DARK_GRAY_BACKGROUND = "\u001B[100m";
    public static final String BRIGHT_PURPLE_BACKGROUND = "\u001B[105m";
    public static final String BRIGHT_CYAN_BACKGROUND = "\u001B[106m";
    public static final String BRIGHT_WHITE_BACKGROUND = "\u001B[107m";

    /**
     * Minecraft Bedrock special colors
     */
    public static final String MINECOIN_GOLD = "\u001B[38;2;221;214;5m";
    public static final String MATERIAL_QUARTZ = "\u001B[38;2;227;212;209m";
    public static final String MATERIAL_IRON = "\u001B[38;2;206;202;202m";
    public static final String MATERIAL_NETHERITE = "\u001B[38;2;68;58;59m";
    public static final String MATERIAL_REDSTONE = "\u001B[38;2;151;22;7m";
    public static final String MATERIAL_COPPER = "\u001B[38;2;180;104;77m";
    public static final String MATERIAL_GOLD = "\u001B[38;2;222;177;45m";
    public static final String MATERIAL_EMERALD = "\u001B[38;2;17;160;54m";
    public static final String MATERIAL_DIAMOND = "\u001B[38;2;44;186;168m";
    public static final String MATERIAL_LAPIS = "\u001B[38;2;33;73;123m";
    public static final String MATERIAL_AMETHYST = "\u001B[38;2;154;92;198m";
 
    /**
     * Maps
    */
    private static final Map<String, String> COLOR_MAP = new HashMap<>();
    private static final Map<String, Color> COLORS = new HashMap<>();

    static {
        COLOR_MAP.put("&r", RESET);
        COLOR_MAP.put("&k", OBFUSCATED);
        COLOR_MAP.put("&l", BOLD);
        COLOR_MAP.put("&m", STRIKETHROUGH);
        COLOR_MAP.put("&n", UNDERLINE);
        COLOR_MAP.put("&o", ITALIC);

        COLOR_MAP.put("&0", BLACK);
        COLOR_MAP.put("&1", DARK_BLUE);
        COLOR_MAP.put("&2", GREEN);
        COLOR_MAP.put("&3", CYAN);
        COLOR_MAP.put("&4", RED);
        COLOR_MAP.put("&5", PURPLE);
        COLOR_MAP.put("&6", GOLD);
        COLOR_MAP.put("&7", LIGHT_GRAY);
        COLOR_MAP.put("&8", DARK_GRAY);
        COLOR_MAP.put("&9", BLUE);
        COLOR_MAP.put("&a", BRIGHT_GREEN);
        COLOR_MAP.put("&b", BRIGHT_CYAN);
        COLOR_MAP.put("&c", BRIGHT_RED);
        COLOR_MAP.put("&d", LIGHT_PURPLE);
        COLOR_MAP.put("&e", YELLOW);
        COLOR_MAP.put("&f", WHITE);

        COLOR_MAP.put("&g", MINECOIN_GOLD);
        COLOR_MAP.put("&h", MATERIAL_QUARTZ);
        COLOR_MAP.put("&i", MATERIAL_IRON);
        COLOR_MAP.put("&j", MATERIAL_NETHERITE);
        COLOR_MAP.put("&p", MATERIAL_GOLD);
        COLOR_MAP.put("&q", MATERIAL_EMERALD);
        COLOR_MAP.put("&s", MATERIAL_DIAMOND);
        COLOR_MAP.put("&t", MATERIAL_LAPIS);
        COLOR_MAP.put("&u", MATERIAL_AMETHYST);

        COLOR_MAP.put("^#0", BLACK_BACKGROUND);
        COLOR_MAP.put("^#1", BLUE_BACKGROUND);
        COLOR_MAP.put("^#2", GREEN_BACKGROUND);
        COLOR_MAP.put("^#3", CYAN_BACKGROUND);
        COLOR_MAP.put("^#4", RED_BACKGROUND);
        COLOR_MAP.put("^#5", PURPLE_BACKGROUND);
        COLOR_MAP.put("^#6", YELLOW_BACKGROUND);
        COLOR_MAP.put("^#7", DARK_GRAY_BACKGROUND);
        COLOR_MAP.put("^#8", BRIGHT_BLACK_BACKGROUND);
        COLOR_MAP.put("^#9", BLUE_BACKGROUND);
        COLOR_MAP.put("^#a", BRIGHT_GREEN_BACKGROUND);
        COLOR_MAP.put("^#b", BRIGHT_CYAN_BACKGROUND);
        COLOR_MAP.put("^#c", BRIGHT_RED_BACKGROUND);
        COLOR_MAP.put("^#d", BRIGHT_PURPLE_BACKGROUND);
        COLOR_MAP.put("^#e", YELLOW_BACKGROUND);
        COLOR_MAP.put("^#f", WHITE_BACKGROUND);

        COLORS.put(BLACK, Color.BLACK);
        COLORS.put(BLUE, Color.BLUE);
        COLORS.put(DARK_BLUE, new Color(0, 0, 170));
        COLORS.put(CYAN, Color.CYAN);
        COLORS.put(RED, Color.RED);
        COLORS.put(DARK_RED, new Color(170, 0, 0));
        COLORS.put(PURPLE, new Color(255, 85, 255));
        COLORS.put(LIGHT_PURPLE, Color.PINK);
        COLORS.put(GOLD, new Color(255, 170, 0));
        COLORS.put(BRIGHT_GRAY, Color.LIGHT_GRAY);
        COLORS.put(DARK_GRAY, Color.DARK_GRAY);
        COLORS.put(BRIGHT_RED, new Color(255, 85, 85));
        COLORS.put(YELLOW, Color.YELLOW);
        COLORS.put(GREEN, Color.GREEN);
        COLORS.put(BRIGHT_GREEN, new Color(85, 255, 85));

        COLORS.put(MINECOIN_GOLD, new Color(221, 214, 5));
        COLORS.put(MATERIAL_QUARTZ, new Color(227, 212, 5));
        COLORS.put(MATERIAL_IRON, new Color(206, 202, 202));
        COLORS.put(MATERIAL_NETHERITE, new Color(68, 58, 59));
        COLORS.put(MATERIAL_REDSTONE, new Color(151, 22, 7));
        COLORS.put(MATERIAL_COPPER, new Color(180, 104, 77));
        COLORS.put(MATERIAL_EMERALD, new Color(17, 160, 54));
        COLORS.put(MATERIAL_DIAMOND, new Color(44, 186, 168));
        COLORS.put(MATERIAL_LAPIS, new Color(33, 73, 123));
        COLORS.put(MATERIAL_AMETHYST, new Color(154, 92, 198));
    }

    private AnsiColor() {

    }

    public static String getMinecraftColor(final String ansi) {
        for (final Map.Entry<String, String> entry : COLOR_MAP.entrySet()) {
            if (ansi.equals(entry.getValue())) return entry.getKey();
        }
        return "";
    }

    /**
     * Robione na podstawie <a href="https://github.com/PetteriM1/DiscordChat/blob/master/src/main/java/me/petterim1/discordchat/DiscordListener.java#L165">DiscordListener.java</a>
     * <p>
     * Nadal wymaga usprawnień
     * </p>
     */
    public static String getMinecraftColor(final int r, final int g, final int b) {
        if (r <= -1 && g <= -1 && b <= -1) return getMinecraftColor(LIGHT_GRAY);

        final TreeMap<Double, String> closest = new TreeMap<>();

        COLORS.forEach((color, set) -> {
            final double distance = Math.sqrt(Math.pow(r - set.getRed(), 2) + Math.pow((g - set.getGreen()) * 1.5, 2) + Math.pow(b - set.getBlue(), 2));
            closest.put(distance, color);
        });

        return getMinecraftColor(closest.firstEntry().getValue());
    }

    public static String fromRGB(final int r, final int g, final int b) {
        return "\u001B[38;2;" + r + ";" + g + ";" + b + "m";
    }

    public static String convertMinecraftColors(final Object input) {
        if (input instanceof String in) {
            for (final Map.Entry<String, String> entry : COLOR_MAP.entrySet()) {
                in = in.replaceAll(entry.getKey(), entry.getValue());
            }
            return in + RESET;
        }
        return input.toString();
    }

    public static String removeColors(final Object input) {
        if (input instanceof String in) {
            for (final Map.Entry<String, String> entry : COLOR_MAP.entrySet()) {
                in = in.replace(entry.getValue(), "")
                        .replaceAll(entry.getKey(), "");
            }
            return in;
        }
        return input.toString();
    }

    //TODO: Dodaj metode removeMinecraftColor
    
    public static String removeAnsiColors(final Object input) {
        if (input instanceof String in) {
            for (final Map.Entry<String, String> entry : COLOR_MAP.entrySet()) {
                in = in.replace(entry.getValue(), "");
            }
            return in;
        }
        return input.toString();
    }
}
