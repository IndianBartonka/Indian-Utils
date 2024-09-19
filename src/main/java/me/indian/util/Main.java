package me.indian.util;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import me.indian.util.download.DownloadListener;
import me.indian.util.download.DownloadTask;
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

    public static void encryptorTest() {
        //TODO: Dodać tak owy
    }

    public static void downloadFileTest() throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL("https://minecraft.azureedge.net/bin-win/bedrock-server-1.21.23.01.zip").openConnection();
        connection.setRequestMethod("GET");

        final String fileName = "Bedrock.zip";
        final int responseCode = connection.getResponseCode();

        final DownloadListener downloadListener = new DownloadListener() {
            final Logger tempLogger = LOGGER.tempLogger(fileName);

            @Override
            public void onStart(final BufferUtil.DownloadBuffer downloadBuffer, final int definedBuffer, final File outputFile) {
                this.tempLogger.info("Pobieranie:&a " + outputFile.getName());
                this.tempLogger.info("Ustalony buffer dla naszego pliku to:&a " + BufferUtil.findBuffer(definedBuffer));
                this.tempLogger.info("Domyślny buffer to:&a " + downloadBuffer);
            }

            @Override
            public void onSecond(final int progress, final double formatedSpeed, final String remainingTimeString) {
                //Kod wykonuje się co każdą sekunde
                this.tempLogger.info(progress + "%&a " + formatedSpeed + "MB/s &c" + remainingTimeString);
            }

            @Override
            public void onProgress(final int progress, final double formatedSpeed, final String remainingTimeString) {
//                tempLogger.info(progress + "%&a " + formatedSpeed + "MB/s &c" + remainingTimeString);
            }

            @Override
            public void onTimeout(final int timeOutSeconds) {
                this.tempLogger.info("TimeOut");
            }

            @Override
            public void onEnd(final File outputFile) {
                this.tempLogger.info("Pobrano:&a " + outputFile.getName());
            }
        };

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try {
                final long start = System.currentTimeMillis();

                final DownloadTask downloadTask = new DownloadTask(connection.getInputStream(), new File(fileName),
                        connection.getContentLength(),
                        BufferUtil.DownloadBuffer.DYNAMIC,
                        30,
                        downloadListener
                );

                LOGGER.info(downloadTask);

                new Thread(() -> {
                    ThreadUtil.sleep(5);
                    LOGGER.info("Zatrzymywanie pobierania:&b " + fileName);
                    try {
                        downloadTask.stopDownload();
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

                downloadTask.downloadFile();

                LOGGER.info("Pobrano w:&b " + DateUtil.formatTimeDynamic(System.currentTimeMillis() - start, true));
            } catch (final TimeoutException e) {
                throw new RuntimeException(e);
            }

        } else {
            LOGGER.info("Server zwrócił kod:&a " + responseCode);
        }
    }

    public static void zipTest() {
        ZipUtil.init(LOGGER);
        final File file = new File("logs");

        if (!file.exists()) return;

        final File zipFile;

        try {
            zipFile = ZipUtil.zipFiles(file.listFiles(), "logs.zip");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ZipUtil.unzipFile(zipFile.getPath(), "siur", true);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(final String[] args) throws IOException {
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

        encryptorTest();
        LOGGER.print("==================");

        downloadFileTest();
        LOGGER.print("==================");

        zipTest();
        LOGGER.print("==================");
    }
}
