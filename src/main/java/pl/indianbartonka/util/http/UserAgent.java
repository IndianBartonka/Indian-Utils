package pl.indianbartonka.util.http;

import java.util.List;
import pl.indianbartonka.util.IndianUtils;
import pl.indianbartonka.util.MathUtil;
import pl.indianbartonka.util.MessageUtil;
import pl.indianbartonka.util.annotation.UtilityClass;
import pl.indianbartonka.util.system.SystemUtil;

/**
 * Utility class for generating User-Agent strings for various browsers and platforms.
 * Provides predefined User-Agent strings and methods to build them dynamically.
 * <p>
 * Documents written by ChatGPT
 * </p>
 */

@UtilityClass
public final class UserAgent {
//TODO: Update versions
    /**
     * Predefined User-Agent strings for various tools and browsers
     */
    public static final String USER_AGENT_CURL = "curl/8.11.1";
    public static final String USER_AGENT_OKHTTP = "okhttp/5.0.0-alpha.14";
    public static final String USER_AGENT_WINDOWS_POWER_SHELL = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) PowerShell/7.5.0";

    /**
     * Browser version strings
     */
    private static final String CHROME_VERSION = "110.0.5481.100";
private static final String FIREFOX_VERSION = "109.0";
private static final String SAFARI_VERSION = "17.1";
private static final String EDGE_VERSION = "110.0.1587.49";
private static final String OPERA_VERSION = "95.0.4635.37";

    
    /**
     * Predefined User-Agent strings for mobile applications
     */
    public static final String USER_AGENT_ANDROID = "Mozilla/5.0 (Linux; Android 14; K) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/" + CHROME_VERSION + " Mobile Safari/" + SAFARI_VERSION;
    public static final String USER_AGENT_IOS = "Mozilla/5.0 (iPhone; CPU iPhone OS 18_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1 Mobile/15E148 Safari/" + SAFARI_VERSION;
   
    /**
     * Base User agent
     */
    private static final String BASE_USER_AGENT = MessageUtil.formatMessage("Mozilla/5.0 (%s; %s; %s; %s)",
            SystemUtil.getFullyOSName(), SystemUtil.getOSVersion(), SystemUtil.getFullyArchCode(), SystemUtil.LOCALE.toLanguageTag());
    public static final String USER_AGENT_INDIAN_UTILS = BASE_USER_AGENT + " IndianUtils/" + IndianUtils.version;

    /**
     * Predefined User-Agent strings for browsers
     */
    public static final String USER_AGENT_CHROME = BASE_USER_AGENT + " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + CHROME_VERSION + " Safari/" + SAFARI_VERSION;
    public static final String USER_AGENT_SAFARI = BASE_USER_AGENT + " AppleWebKit/537.36 (KHTML, like Gecko) Version/14.1 Safari/" + SAFARI_VERSION;
    public static final String USER_AGENT_EDGE = BASE_USER_AGENT + " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + CHROME_VERSION + " Safari/" + SAFARI_VERSION + " Edg/" + EDGE_VERSION;
    public static final String USER_AGENT_OPERA = BASE_USER_AGENT + " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + CHROME_VERSION + " OPR/" + OPERA_VERSION;
 public static final String USER_AGENT_FIREFOX = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:" + FIREFOX_VERSION + ") Gecko/20100101 Firefox/" + FIREFOX_VERSION;
   
    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private UserAgent() {
    }

    /**
     * Returns a random User-Agent string from predefined options.
     *
     * @return a random User-Agent string
     */
    public static String randomUserAgent() {
        return MathUtil.getRandomElement(
                List.of(USER_AGENT_CHROME, USER_AGENT_FIREFOX, USER_AGENT_SAFARI, USER_AGENT_EDGE, USER_AGENT_OPERA, BASE_USER_AGENT,
                        USER_AGENT_ANDROID, USER_AGENT_IOS,
                        USER_AGENT_CURL, USER_AGENT_OKHTTP, USER_AGENT_WINDOWS_POWER_SHELL, USER_AGENT_INDIAN_UTILS,
                        BASE_USER_AGENT
                ));
    }
}
