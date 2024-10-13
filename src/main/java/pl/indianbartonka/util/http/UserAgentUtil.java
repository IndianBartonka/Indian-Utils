package pl.indianbartonka.util.http;

import java.util.List;
import pl.indianbartonka.util.IndianUtils;
import pl.indianbartonka.util.MathUtil;
import pl.indianbartonka.util.MessageUtil;
import pl.indianbartonka.util.system.SystemUtil;

/**
 * Utility class for generating User-Agent strings for various browsers and platforms.
 * Provides predefined User-Agent strings and methods to build them dynamically.
 * <p>
 * Documents written by ChatGPT
 * </p>
 */
public final class UserAgentUtil {

    // Predefined User-Agent strings for various tools and browsers
    public static final String USER_AGENT_CURL = "curl/7.64.1";
    public static final String USER_AGENT_OKHTTP = "okhttp/3.12.1";
    public static final String USER_AGENT_WINDOWS_POWER_SHELL = "Mozilla/5.0 (Windows NT; Windows NT 10.0; pl-PL) WindowsPowerShell/5.1.22621.4249";
    public static final String USER_AGENT_INDIAN_UTILS = "indianUtils/" + IndianUtils.VERSION;

    // Browser version strings
    private static final String CHROME_VERSION = "91.0.4472.114";
    private static final String FIREFOX_VERSION = "89.0";
    public static final String USER_AGENT_FIREFOX =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:" + FIREFOX_VERSION + ") Gecko/20100101 Firefox/" + FIREFOX_VERSION;
    private static final String SAFARI_VERSION = "537.36";

    // Predefined User-Agent strings for browsers
    public static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + CHROME_VERSION + " Safari/" + SAFARI_VERSION;
    public static final String USER_AGENT_SAFARI = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Version/14.1 Safari/" + SAFARI_VERSION;
    // Predefined User-Agent strings for mobile applications
    public static final String USER_AGENT_ANDROID = "Mozilla/5.0 (Linux; Android 13; K) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/" + CHROME_VERSION + " Mobile Safari/" + SAFARI_VERSION;
    public static final String USER_AGENT_IOS = "Mozilla/5.0 (iPhone; CPU iPhone OS 18_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1 Mobile/15E148 Safari/" + SAFARI_VERSION;
    private static final String EDGE_VERSION = "91.0.864.64";
    public static final String USER_AGENT_EDGE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + CHROME_VERSION + " Safari/" + SAFARI_VERSION + " Edg/" + EDGE_VERSION;
    private static final String OPERA_VERSION = "77.0.4054.90";
    public static final String USER_AGENT_OPERA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + CHROME_VERSION + " OPR/" + OPERA_VERSION;

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private UserAgentUtil() {
        // Prevents instantiation of utility class
    }

    /**
     * Builds a random User-Agent string based on a randomly selected browser.
     *
     * @return A randomly generated User-Agent string.
     */
    public static String buildUserAgent() {
        return buildUserAgent(MathUtil.getRandomElement(List.of("chrome", "firefox", "safari", "edge", "opera")));
    }

    /**
     * Builds a User-Agent string for the specified browser.
     *
     * @param browser The name of the browser (e.g., "chrome", "firefox").
     * @return A User-Agent string for the specified browser.
     */
    public static String buildUserAgent(final String browser) {
        final String osName = SystemUtil.getFullyOsName();
        final String osVersion = SystemUtil.getOsVersion();
        final String osArch = SystemUtil.getFullyArchCode();

        final String baseUserAgent = MessageUtil.formatMessage("Mozilla/5.0 (%s; %s; %s)", osName, osVersion, osArch);
        final String appleWebKit = " AppleWebKit/537.36 (KHTML, like Gecko)";

        return switch (browser.toLowerCase()) {
            case "chrome" -> baseUserAgent + appleWebKit + " Chrome/" + CHROME_VERSION + " Safari/" + SAFARI_VERSION;
            case "firefox" -> baseUserAgent + " Gecko/20100101 Firefox/" + FIREFOX_VERSION;
            case "safari" -> baseUserAgent + appleWebKit + " Version/14.0 Safari/" + SAFARI_VERSION;
            case "edge" ->
                    baseUserAgent + appleWebKit + " Chrome/" + CHROME_VERSION + " Safari/" + SAFARI_VERSION + " Edg/" + EDGE_VERSION;
            case "opera" -> baseUserAgent + appleWebKit + " Chrome/" + CHROME_VERSION + " OPR/" + OPERA_VERSION;
            default -> baseUserAgent;  // Default User-Agent if browser not recognized
        };
    }
}
