package pl.indianbartonka.util;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CooldownUtilTest {

    @Test
    void cooldown() {
        final long startTime = System.currentTimeMillis();

//        do {
//            System.out.println(cooldown.hasCooldown("siur"));
//        } while (cooldown.cooldown("siur", 20, TimeUnit.SECONDS));

        System.out.println("Nadawanie cooldownu i czekanie...");
        CooldownUtil.cooldownWait("siur", 10, TimeUnit.SECONDS);

        System.out.println(DateUtil.formatTimeDynamic((System.currentTimeMillis() - startTime)));

        Assertions.assertFalse(CooldownUtil.hasCooldown("siur"));
    }

    @Test
    void cooldownMultipleTimes() {
        for (int i = 0; i < 5; i++) {
            final long startTime = System.currentTimeMillis();

            System.out.println(i + " Nadawanie cooldownu i czekanie...");
            CooldownUtil.cooldownWait("siur", 5, TimeUnit.SECONDS);

            System.out.println(DateUtil.formatTimeDynamic((System.currentTimeMillis() - startTime)));

            Assertions.assertFalse(CooldownUtil.hasCooldown("siur"));
            System.out.println();
        }
    }
}
