package me.indian.util;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import me.indian.util.download.DownloadListener;
import me.indian.util.download.DownloadTask;
import me.indian.util.file.FileUtil;
import me.indian.util.language.Language;
import me.indian.util.language.LanguageManager;
import me.indian.util.language.storage.impl.HashMapStorageStrategy;
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

    private static void languagesTest() throws IOException {
        // Inicjalizacja LanguageManager z lokalizacją plików językowych
        // Jeśli folder nie może zostać utworzony, metoda wyrzuci IOException
        final LanguageManager languageManager = new LanguageManager(
                LOGGER, System.getProperty("user.dir") + File.separator + "langs" + File.separator
        );

        // Tworzenie instancji klas językowych dla polskiego i angielskiego i strategią zapisywania ich dostępne są `HashMapStorageStrategy` i `PropertiesStorageStrategy`, lecz możesz dodać własną implementacje
        //Każdy język powinien mieć oddzielną instancje strategi
        final Language polish = new Language("pl_PL", languageManager, new HashMapStorageStrategy()) {
        };
        final Language english = new Language("en_EN", languageManager, new HashMapStorageStrategy()) {
        };

        // Dodawanie wiadomości do języka polskiego, jeśli plik nie istnieje
        if (!polish.getLangFile().exists()) {
            // Dodanie wiadomości "Siema %s %s" do pliku językowego
            polish.addMessage("message.hello", "Siema %s %s");
            // Zapisanie języka polskiego do pliku
            languageManager.saveLanguage(polish);
        }

        // Dodawanie wiadomości do języka angielskiego, jeśli plik nie istnieje
        if (!english.getLangFile().exists()) {
            // Dodanie wiadomości "Hello %s %s" oraz "Kotek" do pliku językowego
            english.addMessage("message.hello", "Hello %s %s");
            english.addMessage("cat.exe", "Kotek");
            // Zapisanie języka angielskiego do pliku
            //Nie trzeba ręcznie ładować języka jesli jest on zapisywany bądź ładowany za pomoc LanguageManager
            languageManager.saveLanguage(english);

        } else {
            // Jeśli plik istnieje, ładujemy wiadomości z pliku
            //Nie trzeba ręcznie ładować języka jesli jest on zapisywany bądź ładowany za pomoc LanguageManager
            languageManager.loadLanguage(english);
        }

        //Jeśli chcesz dodac nowe wartości do pliku języka zrób to po załadowaniu pliku, w przeciwym razie wiadomości nie zostaną dodane jeśli plik istniał
        polish.addMessage("country.germany", "Niemcy");

        // Ustawienie języka polskiego jako domyślnego
        // Nie trzeba ręcznie ładować domyślnego języka, ponieważ zostanie on automatycznie załadowany
        languageManager.setDefaultLanguage(polish);

        // Logowanie domyślnego języka
        LOGGER.info("Default: " + languageManager.getDefaultLanguage().getLanguageCodeName());

        // Pobieranie wiadomości "message.hello" z domyślnego języka
        LOGGER.info(languageManager.getMessage("message.hello"));

        // Formatowanie wiadomości z jednym argumentem
        // Otrzymamy "Siema Neighbor %s" ponieważ "message.hello" formatuje dwie zmienne, a my podajemy jedną
        LOGGER.info(languageManager.getMessage("message.hello", "Neighbor"));

        // Formatowanie wiadomości z dwoma argumentami
        // Otrzymamy "Siema Neighbor WORD"
        LOGGER.info(languageManager.getMessage("message.hello", "Neighbor", "WORD"));

        // Pobranie wiadomości "cat.exe" z języka polskiego, zwraca wartość, jeśli istnieje
        LOGGER.info(languageManager.getMessage("cat.exe", polish));

        // Domyślny język to polski, ale zwraca wartość z języka angielskiego, jeśli polski nie zawiera klucza
        LOGGER.info(languageManager.getMessage("cat.exe"));

        // Zapis wszystkich dostępnych języków do plików
        languageManager.saveLanguages(true);

        // Dodanie nowej wiadomości do języka polskiego
        polish.addMessage("polska.message", "Polska gruom");

        // Zapisanie zmian do pliku językowego
        polish.saveToFile();
    }

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

    private static void fileUtilTest() {
        LOGGER.info("Dostępne: " + MathUtil.formatBytesDynamic(FileUtil.availableDiskSpace(), false));
        LOGGER.info("Użyte: " + MathUtil.formatBytesDynamic(FileUtil.usedDiskSpace(), false));
        LOGGER.info("Maksymalne: " + MathUtil.formatBytesDynamic(FileUtil.maxDiskSpace(), false));

        LOGGER.print();

        final File[] files = new File(System.getProperty("user.dir")).listFiles();
        if (files != null) {
            for (final File file : files) {
                LOGGER.info(file.getName() + " = " + FileUtil.getFileTypeInfo(file.getName()));
            }
        }

        //TODO: Dodać więcej przykładów użycia FileUtil
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
        LOGGER.info("Dodac encryptor Test");
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

            @Override
            public void onDownloadStop() {
                this.tempLogger.alert("Zatrzymano pobieranie!");
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
                    downloadTask.stopDownload();
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
        ZipUtil.init(LOGGER, 9);
        final File file = new File("logs");

        LOGGER.info("Aktualny poziom kompresij to:&b " + ZipUtil.getCompressionLevel());

        if (!file.exists()) return;

        final File zipFile;

        try {
            zipFile = ZipUtil.zipFolder(file.getPath(), "logs.zip");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ZipUtil.unzipFile(zipFile.getPath(), "siur", false);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void bufferTest() {

        final File[] files = new File(System.getProperty("user.dir")).listFiles();
        if (files != null) {
            for (final File file : files) {
                final int buffer = BufferUtil.calculateOptimalBufferSize(FileUtil.getFileSize(file));
                LOGGER.info(file.getName() + " = " + MathUtil.formatBytesDynamic(buffer, false) + " | " + BufferUtil.findBuffer(buffer));
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        languagesTest();
        LOGGER.print("==================");

        bufferTest();
        LOGGER.print("==================");

        loggerTest();
        LOGGER.print("==================");

        fileUtilTest();
        LOGGER.print("==================");

        mathUtilTest();
        LOGGER.print("==================");

        systemUtilTest();
        LOGGER.print("==================");

        bedrockQueryTest();
        LOGGER.print("==================");

        encryptorTest();
        LOGGER.print("==================");

        downloadFileTest();
        LOGGER.print("==================");

        zipTest();
        LOGGER.print("==================");

        dateUtilTest();
        LOGGER.print("==================");
    }
}
