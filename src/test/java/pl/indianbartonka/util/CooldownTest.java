package pl.indianbartonka.util;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CooldownTest {

    private final Cooldown test = new Cooldown("test");

    @Test
    public void cooldown() {
        final long startTime = System.currentTimeMillis();

        System.out.println("Nadawanie cooldownu i czekanie...");
        this.test.cooldownWait("siur", 10, TimeUnit.SECONDS);

        System.out.println(DateUtil.formatTimeDynamic((System.currentTimeMillis() - startTime)));

        Assertions.assertFalse(this.test.hasCooldown("siur"));
    }


    @Test
    public void cooldownRemove() {
        final long startTime = System.currentTimeMillis();

        this.test.cooldown("siur", 10, TimeUnit.SECONDS);

        int tries = 0;

        while (this.test.hasCooldown("siur")) {
            if (tries == 10) {
                this.test.removeCooldown("siur");
            }

            System.out.println("Próba nr: " + tries);
            tries++;
        }

        System.out.println(DateUtil.formatTimeDynamic((System.currentTimeMillis() - startTime)));

        Assertions.assertFalse(this.test.hasCooldown("siur"));
    }

    @Test
    public void cooldownMultipleTimes() {
        for (int i = 0; i < 5; i++) {
            final long startTime = System.currentTimeMillis();

            System.out.println(i + " Nadawanie cooldownu i czekanie...");
            this.test.cooldownWait("siur", 5, TimeUnit.SECONDS);

            System.out.println(DateUtil.formatTimeDynamic((System.currentTimeMillis() - startTime)));

            Assertions.assertFalse(this.test.hasCooldown("siur"));
            System.out.println();
        }
    }
}
