package pl.indianbartonka.util;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.logger.Logger;
import pl.indianbartonka.util.logger.LoggerConfiguration;

public class LoggerTest {

    private Logger logger;
    private Logger logger2;

    @BeforeEach
    void setUp() {
        this.logger = new Logger(new LoggerConfiguration(true, System.getProperty("user.dir") + File.separator + "logs", true)) {
        };

        this.logger2 = new Logger(this.logger) {
        };
    }

    @Test
    public void testBasicLogging() {
        this.logger.info("Logger Test");
        this.logger.print("1");
        this.logger.info("2");
        this.logger.info("2");
        this.logger.print();
    }

    @Test
    public void testSecondaryLogger() {
        this.logger2.print("3");
        this.logger2.info(4);
        this.logger2.print();
    }

    @Test
    public void testWarningLogFiles() {
        this.logger.warning(this.logger2.getLogFile());
        this.logger2.warning(this.logger.getLogFile());
    }

    @Test
    public void testParentAndChildrenLoggers() {
        this.logger.alert("Rodzic: " + this.logger.getParent() + " Dzieci: " + this.logger.getChildren());
        this.logger2.alert("Rodzic: " + this.logger2.getParent() + " Dzieci: " + this.logger2.getChildren());
        this.logger2.getParent().info("Uzywam rodzica");
    }

    @Test
    public void testJavaUtilLogging() {
        final java.util.logging.Logger jul = java.util.logging.Logger.getLogger("JUL");
        jul.info("okejjj");
    }

    @Test
    public void testTempLoggers() {
        this.logger.tempLogger("temp1").info("spokoo " + MathUtil.RANDOM.nextInt(5));
        this.logger.tempLogger("temp2").info("spokoo " + MathUtil.RANDOM.nextInt(8));
    }

    @Test
    public void testJavaUtilLoggingWithRandom() {
        final java.util.logging.Logger jul = java.util.logging.Logger.getLogger("JUL");
        jul.info("okejjj " + MathUtil.RANDOM.nextInt(5));
    }
}
