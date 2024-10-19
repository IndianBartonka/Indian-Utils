package pl.indianbartonka.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class CooldownUtil {

    private static final Map<String, Long> COOLDOWN = new ConcurrentHashMap<>();
    private static final Map<String, Long> TIME_MAP = new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private CooldownUtil() {

    }

    public static boolean cooldown(final String name, final long duration, final TimeUnit timeUnit) {
        final long time = timeUnit.toMillis(duration);

        COOLDOWN.put(name, System.currentTimeMillis());
        TIME_MAP.put(name, time);

        return hasCooldown(name);
    }

    public static void cooldownWait(final String name, final long duration, final TimeUnit timeUnit) {
        if (COOLDOWN.containsKey(name)) return;

        final long time = timeUnit.toMillis(duration);

        COOLDOWN.put(name, System.currentTimeMillis());
        TIME_MAP.put(name, time);

        waitFor(name);
    }

    public static long getTime(final String name) {
        return TIME_MAP.getOrDefault(name, 0L);
    }

    public static long getRemainingTime(final String name) {
        if (!COOLDOWN.containsKey(name)) return 0;

        final long startTime = COOLDOWN.get(name);
        final long cooldownDuration = getTime(name);
        final long elapsedTime = System.currentTimeMillis() - startTime;

        return cooldownDuration - elapsedTime;
    }

    public static void waitFor(final String name) {
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

    public static boolean hasCooldown(final String name) {
        return COOLDOWN.containsKey(name) && (System.currentTimeMillis() - COOLDOWN.get(name)) < getTime(name);
    }
}