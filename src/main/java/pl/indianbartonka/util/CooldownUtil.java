package pl.indianbartonka.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import pl.indianbartonka.util.annotation.UtilityClass;

@UtilityClass
public final class CooldownUtil {

    private static final Map<String, Long> COOLDOWN = new ConcurrentHashMap<>();
    private static final Map<String, Long> TIME_MAP = new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private CooldownUtil() {
        // Prevent instantiation
    }

    /**
     * Sets a cooldown for the given name with the specified duration.
     *
     * @param name     the name to set the cooldown for
     * @param duration the duration of the cooldown
     * @param timeUnit the time unit of the duration
     * @return true if the cooldown was successfully set, false if it was already active
     */
    public static boolean cooldown(@NotNull final String name, final long duration, @NotNull final TimeUnit timeUnit) {
        final long time = timeUnit.toMillis(duration);

        COOLDOWN.put(name, System.currentTimeMillis());
        TIME_MAP.put(name, time);

        return hasCooldown(name);
    }

    /**
     * Waits for the cooldown to expire for the specified name.
     * If there is already a cooldown, this method returns immediately.
     *
     * @param name     the name to set the cooldown for
     * @param duration the duration of the cooldown
     * @param timeUnit the time unit of the duration
     */
    public static void cooldownWait(@NotNull final String name, final long duration, @NotNull final TimeUnit timeUnit) {
        if (COOLDOWN.containsKey(name)) return;

        final long time = timeUnit.toMillis(duration);

        COOLDOWN.put(name, System.currentTimeMillis());
        TIME_MAP.put(name, time);

        waitFor(name);
    }

    /**
     * Gets the duration of the cooldown for the specified name.
     *
     * @param name the name to get the cooldown duration for
     * @return the cooldown duration in milliseconds
     */
    public static long getTime(@NotNull final String name) {
        return TIME_MAP.getOrDefault(name, 0L);
    }

    /**
     * Gets the remaining time for the cooldown of the specified name.
     *
     * @param name the name to check the remaining cooldown for
     * @return the remaining time in milliseconds
     */
    public static long getRemainingTime(@NotNull final String name) {
        if (!COOLDOWN.containsKey(name)) return 0;

        final long startTime = COOLDOWN.get(name);
        final long cooldownDuration = getTime(name);
        final long elapsedTime = System.currentTimeMillis() - startTime;

        return cooldownDuration - elapsedTime;
    }

    /**
     * Waits until the cooldown expires for the specified name.
     *
     * @param name the name to wait for
     */
    public static void waitFor(@NotNull final String name) {
        while (hasCooldown(name)) {
            final long remaining = getRemainingTime(name);

            if (remaining > 100) {
                ThreadUtil.sleep(100L);
            } else if (remaining > 0) {
                ThreadUtil.sleep(remaining);
            }
        }

        COOLDOWN.remove(name);
        TIME_MAP.remove(name);
    }

    /**
     * Checks if there is an active cooldown for the specified name.
     *
     * @param name the name to check for cooldown
     * @return true if there is an active cooldown, false otherwise
     */
    public static boolean hasCooldown(@NotNull final String name) {
        return COOLDOWN.containsKey(name) && (System.currentTimeMillis() - COOLDOWN.get(name)) < getTime(name);
    }
}
