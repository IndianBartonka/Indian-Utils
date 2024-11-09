package pl.indianbartonka.util;

import java.io.File;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.file.FileUtil;
import pl.indianbartonka.util.logger.Logger;
import pl.indianbartonka.util.logger.config.LoggerConfiguration;
import pl.indianbartonka.util.system.SystemUtil;

public class BufferUtilTest {

    private static final LoggerConfiguration loggerConfiguration = LoggerConfiguration.builder()
            .setLoggingToFile(false)
            .build();

    private final Logger logger = new Logger(loggerConfiguration) {
    };

    @Test
    public void test(){
        this.logger.info("&aFree Ram:&b " + MathUtil.formatBytesDynamic(SystemUtil.getFreeRam(), false));
        this.logger.info("&aFree SWAP:&b" + MathUtil.formatBytesDynamic(SystemUtil.getFreeSwap(), false));
        this.logger.info("&aFree Ram + SWAP:&b " + MathUtil.formatBytesDynamic(SystemUtil.getFreeRam() + SystemUtil.getFreeSwap(), false));
        this.logger.println();

        final File[] files = new File(System.getProperty("user.dir")).listFiles();
        if (files != null) {
            for (final File file : files) {
                final int buffer = BufferUtil.calculateOptimalBufferSize(FileUtil.getFileSize(file));
                this.logger.info(file.getName() + " = " + MathUtil.formatBytesDynamic(buffer, false));
            }
        }
    }
}