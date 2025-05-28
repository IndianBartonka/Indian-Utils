package pl.indianbartonka.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.logger.Logger;
import pl.indianbartonka.util.logger.config.LoggerConfiguration;
import pl.indianbartonka.util.system.SystemUtil;

public class BufferUtilTest {

    private static final LoggerConfiguration loggerConfiguration = LoggerConfiguration.builder().setOneLog(true).build();

    private final Logger logger = new Logger(loggerConfiguration) {
    };

    @Test
    public void test() {
        this.logger.info("&aFree Ram:&b " + MathUtil.formatBytesDynamic(SystemUtil.getFreeRam()));
        this.logger.info("&aFree JVM Ram:&b " + MathUtil.formatBytesDynamic(BufferUtil.getJvmUsableRam()));
        this.logger.println();

        final List<File> files = FileUtil.listAllFiles(new File(System.getProperty("user.dir")));
        for (int i = 1; i < 10; i++) {
            BufferUtil.setRamUsageDivisor(i);

            this.logger.println();
            this.logger.info("&aRam divisor:&b " + i);
            for (final File file : files) {
                final int buffer = BufferUtil.calculateOptimalBufferSize(FileUtil.getFileSize(file));
                this.logger.info(file.getName() + " = " + MathUtil.formatBytesDynamic(buffer, false));
            }
        }
    }

    @Test
    public void testFile() {
        this.logger.info("&aFree Ram:&b " + MathUtil.formatBytesDynamic(SystemUtil.getFreeRam()));
        this.logger.info("&aFree JVM Ram:&b " + MathUtil.formatBytesDynamic(BufferUtil.getJvmUsableRam()));
        this.logger.println();

        final File file = this.createBigFile();

        this.logger.info(file.getAbsolutePath());
        this.logger.info("&aRozmiar pliku:&b " + MathUtil.formatBytesDynamic(FileUtil.getFileSize(file)));

        for (int i = 1; i < 20; i++) {
            BufferUtil.setRamUsageDivisor(i);
            BufferUtil.setFileSizePercent(MathUtil.RANDOM.nextInt(100));

            this.logger.println();
            this.logger.info("&aRam divisor:&b " + BufferUtil.getRamUsageDivisor());
            this.logger.info("&aFile size percent:&b " + BufferUtil.getFileSizePercent());

            final int buffer = BufferUtil.calculateOptimalBufferSize(FileUtil.getFileSize(file));
            this.logger.info(file.getName() + " = " + MathUtil.formatBytesDynamic(buffer, true));
        }

        this.logger.println();
        this.logger.info("&4Domyślny Buffor");

        BufferUtil.setRamUsageDivisor(5);
        BufferUtil.setFileSizePercent(100);

        this.logger.println();
        this.logger.info("&aRam divisor:&b " + BufferUtil.getRamUsageDivisor());
        this.logger.info("&aFile size percent:&b " + BufferUtil.getFileSizePercent());

        final int buffer = BufferUtil.calculateOptimalBufferSize(FileUtil.getFileSize(file));
        this.logger.info(file.getName() + " = " + MathUtil.formatBytesDynamic(buffer, true));

    }

    private File createBigFile() {
        final File file = new File("big.file");

        file.deleteOnExit();

        if (!file.exists()) {
            try (final FileWriter writer = new FileWriter(file)) {
                final byte[] buffer = new byte[Math.toIntExact(MemoryUnit.MEGABYTES.to(60, MemoryUnit.BYTES))];
                writer.write(Arrays.toString(buffer));
            } catch (final IOException ioException) {
                ioException.printStackTrace();
            }
        }

        return file;
    }
}