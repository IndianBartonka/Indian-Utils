package pl.indianbartonka.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import pl.indianbartonka.util.annotation.UtilityClass;

@UtilityClass
public class Cooldown {

    private final String name;
    private final Map<String, Long> cooldown;
    private final Map<String, Long> timeMap;

    public Cooldown(final String name) {
        this.name = name;
        this.cooldown = new ConcurrentHashMap<>();
        this.timeMap = new ConcurrentHashMap<>();
    }

    /**
     * Sets a cooldown for the given name with the specified duration.
     *
     * @param name     the name to set the cooldown for
     * @param duration the duration of the cooldown
     * @param timeUnit the time unit of the duration
     */
    public void cooldown(@NotNull final String name, final long duration, @NotNull final TimeUnit timeUnit) {
        final long time = timeUnit.toMillis(duration);

        this.cooldown.put(name, System.currentTimeMillis());
        this.timeMap.put(name, time);
    }

    /**
     * Waits for the cooldown to expire for the specified name.
     * If there is already a cooldown, this method returns immediately.
     *
     * @param name     the name to set the cooldown for
     * @param duration the duration of the cooldown
     * @param timeUnit the time unit of the duration
     */
    public void cooldownWait(@NotNull final String name, final long duration, @NotNull final TimeUnit timeUnit) {
        if (this.cooldown.containsKey(name)) return;

        final long time = timeUnit.toMillis(duration);

        this.cooldown.put(name, System.currentTimeMillis());
        this.timeMap.put(name, time);

        this.waitFor(name);
    }

    /**
     * Removes the cooldown for the specified name.
     * This will clear the cooldown immediately, allowing the name to bypass any remaining cooldown time.
     *
     * @param name the name for which to remove the cooldown
     */
    public void removeCooldown(@NotNull final String name) {
        this.cooldown.remove(name);
        this.timeMap.remove(name);
    }

    /**
     * Gets the duration of the cooldown for the specified name.
     *
     * @param name the name to get the cooldown duration for
     * @return the cooldown duration in milliseconds
     */
    @CheckReturnValue
    public long getTime(@NotNull final String name) {
        return this.timeMap.getOrDefault(name, 0L);
    }

    /**
     * Gets the remaining time for the cooldown of the specified name.
     *
     * @param name the name to check the remaining cooldown for
     * @return the remaining time in milliseconds
     */
    @CheckReturnValue
    public long getRemainingTime(@NotNull final String name) {
        if (!this.cooldown.containsKey(name)) return 0;

        final long startTime = this.cooldown.get(name);
        final long cooldownDuration = this.getTime(name);
        final long elapsedTime = System.currentTimeMillis() - startTime;

        return cooldownDuration - elapsedTime;
    }

    /**
     * Waits until the cooldown expires for the specified name.
     *
     * @param name the name to wait for
     */
    public void waitFor(@NotNull final String name) {
        while (this.hasCooldown(name)) {
            final long remaining = this.getRemainingTime(name);

            if (remaining > 100) {
                ThreadUtil.sleep(100L);
            } else if (remaining > 0) {
                ThreadUtil.sleep(remaining);
            }
        }

        this.cooldown.remove(name);
        this.timeMap.remove(name);
    }

    /**
     * Checks if there is an active cooldown for the specified name.
     *
     * @param name the name to check for cooldown
     * @return true if there is an active cooldown, false otherwise
     */
    @CheckReturnValue
    public boolean hasCooldown(@NotNull final String name) {
        return this.cooldown.containsKey(name) && (System.currentTimeMillis() - this.cooldown.get(name)) < this.getTime(name);
    }

    /**
     * Giving name of current the cooldown class
     *
     * @return name of the cooldown
     */
    public String getName() {
        return this.name;
    }
}
