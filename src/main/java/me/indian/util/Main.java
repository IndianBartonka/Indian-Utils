package me.indian.util;

import java.io.File;
import java.util.Random;
import me.indian.util.logger.Logger;
import me.indian.util.logger.LoggerConfiguration;
import me.indian.util.system.SystemUtil;

public final class Main {

    private static final Logger LOGGER = new Logger(new LoggerConfiguration(true, System.getProperty("user.dir") + File.separator + "logs", true)) {
    };
    private static final Logger LOGGER2 = LOGGER.prefixed("Logger 2");
//    private static final Logger LOGGER = new Logger(new LoggerConfiguration(true,
//            System.getProperty("user.dir") + File.separator + "logs", DateUtil.getFixedDate())) {};
    private static final long START_TIME = System.currentTimeMillis();
    private static final Random RANDOM = new Random(Integer.MAX_VALUE);

    public static void loggerTest() {
        LOGGER.info("Logger Test");
        LOGGER.print("1");
        LOGGER.info("2");
        LOGGER.info("2");

        LOGGER.print();

        LOGGER2.print("3");
        LOGGER2.info(4);

        LOGGER2.print();

        LOGGER.warning(LOGGER2.getLogFile());
        LOGGER2.warning(LOGGER.getLogFile());

        LOGGER.print();

        LOGGER.alert("Rodzic: " + LOGGER.getParent() + " Dzieci: " + LOGGER.getChildren());
        LOGGER2.alert("Rodzic: " + LOGGER2.getParent() + " Dzieci: " + LOGGER2.getChildren());
        LOGGER2.getParent().info("Uzywam rodzica");
    }

    public static void dateUtilTest() {
        LOGGER.info("Date Util Test");
        LOGGER.info(DateUtil.getDate());
        LOGGER.info(DateUtil.getFixedDate());
        LOGGER.print();
        LOGGER.info("Wszytkie testy wraz z tym zajeły:&b " + DateUtil.formatTimeDynamic(System.currentTimeMillis() - START_TIME));
    }

    public static void mathUtilTest() {
        LOGGER.info("Math Util Test");
        final int randomNumber = RANDOM.nextInt();
        final double randomDouble = RANDOM.nextDouble();
        final int bigNumber = MathUtil.getCorrectNumber(randomNumber, 1, 900000);

        LOGGER.info("Randomowa liczba: " + randomNumber);
        LOGGER.info("Wielka liczba: " + bigNumber);
        LOGGER.print();
        LOGGER.info("Randomowy double: " + randomDouble);
        LOGGER.info(MathUtil.getCorrectNumber(randomDouble, 1, 30));
        LOGGER.print();
        LOGGER.info(MathUtil.formatBytesDynamic(bigNumber, true));
        LOGGER.info(MathUtil.formatKiloBytesDynamic(bigNumber, true));
    }

    private static void systemUtilTest() {
        LOGGER.info("System Util Test");
        LOGGER.print();
        LOGGER.info(SystemUtil.getFullOsNameWithDistribution());
        LOGGER.info(SystemUtil.getCurrentArch() + " (" + SystemUtil.getFullyArchCode() + ")");
    }


    public static void bedrockQueryTest() {
        LOGGER.info("BedrockQuery Test");
        final BedrockQuery query = BedrockQuery.create("play.skyblockpe.com", 19132);

        LOGGER.info(query);
    }


    public static void main(final String[] args) {
        loggerTest();
        LOGGER.print("==================");

        mathUtilTest();
        LOGGER.print("==================");

        systemUtilTest();
        LOGGER.print("==================");

        bedrockQueryTest();
        LOGGER.print("==================");

        dateUtilTest();
        LOGGER.print("==================");
    }

}
