package pl.indianbartonka.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import pl.indianbartonka.util.download.DownloadListener;
import pl.indianbartonka.util.download.DownloadTask;
import pl.indianbartonka.util.file.FileUtil;
import pl.indianbartonka.util.http.HttpStatusCode;
import pl.indianbartonka.util.http.connection.Connection;
import pl.indianbartonka.util.http.connection.request.Request;
import pl.indianbartonka.util.http.connection.request.RequestBuilder;
import pl.indianbartonka.util.language.Language;
import pl.indianbartonka.util.language.LanguageManager;
import pl.indianbartonka.util.language.storage.impl.HashMapStorageStrategy;
import pl.indianbartonka.util.language.storage.impl.PropertiesStorageStrategy;
import pl.indianbartonka.util.logger.Logger;
import pl.indianbartonka.util.logger.LoggerConfiguration;
import pl.indianbartonka.util.system.SystemUtil;

public final class Test {

    private static final Logger LOGGER = new Logger(new LoggerConfiguration(true, System.getProperty("user.dir") + File.separator + "logs", true)) {
    };
    //Zaleca się tworzenie nowych loggerów na podstawie głównego
    private static final Logger LOGGER2 = LOGGER.prefixed("Logger 2");

    private static final long START_TIME = System.currentTimeMillis();

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
        final Language english = new Language("en_EN", languageManager, new PropertiesStorageStrategy()) {
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

        //Ładowanie Pliku jezykowego ręcznie
        final File rusFile = new File(languageManager.getLanguagesDir() + "ru_Ru.lang");
        final Language rusLang = languageManager.loadFromFile(rusFile, new HashMapStorageStrategy());
        LOGGER.info("&aZaładowano język z pliku:&b " + rusFile.getPath());
        LOGGER.info("LangaugeCodeName: " + rusLang.getLanguageCodeName());

        LOGGER.info(languageManager.getMessage("russia.message"));

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

        //Info z JUL tez jest wypisywane do naszego pliku z logami
        //Można to wyłączyc tworząc odpowiadnią instancije `LoggerConfiguration` w głównym logerze np:
        // new LoggerConfiguration(true, System.getProperty("user.dir") + File.separator + "logs", true, false)
        final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("JUL");

        logger.info("okejjj");

        LOGGER.tempLogger("temp1").info("spokoo " + MathUtil.RANDOM.nextInt(5));
        LOGGER2.tempLogger("temp2").info("spokoo " + MathUtil.RANDOM.nextInt(8));

        logger.info("okejjj " + MathUtil.RANDOM.nextInt(5));
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
        final int randomNumber = MathUtil.RANDOM.nextInt();
        final double randomDouble = MathUtil.RANDOM.nextDouble();
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

        final List<String> ipList = List.of("play.skyblockpe.com", "play.inpvp.net", "mco.cubecraft.net", "geo.hivebedrock.network", "51.83.32.139");

        for (final String ip : ipList) {
            final BedrockQuery query = BedrockQuery.create(ip, 19132);
            LOGGER.print();
            LOGGER.info("MOTD: " + query.motd());
            LOGGER.info("Server Address: " + query.serverAddress());
            LOGGER.info("Host Address: " + query.hostAddress());
            LOGGER.info("Online: " + query.online());
            LOGGER.info("Response Time: " + query.responseTime());
            LOGGER.info("Edition: " + query.edition());
            LOGGER.info("Protocol: " + query.protocol());
            LOGGER.info("Minecraft Version: " + query.minecraftVersion());
            LOGGER.info("Player Count: " + query.playerCount());
            LOGGER.info("Max Players: " + query.maxPlayers());
            LOGGER.info("Server ID: " + query.serverId());
            LOGGER.info("Map Name: " + query.mapName());
            LOGGER.info("Game Mode: " + query.gameMode());
            LOGGER.info("Port V4: " + query.portV4());
            LOGGER.info("Port V6: " + query.portV6());
        }
    }

    public static void encryptorTest() {
        //TODO: Dodać tak owy
        LOGGER.info("Dodac encryptor Test");
    }

    public static void downloadFileTest() throws IOException {
        final String fileName = "Bedrock.zip";

        final DownloadListener downloadListener = new DownloadListener() {
            final Logger tempLogger = LOGGER.tempLogger(fileName);

            @Override
            public void onStart(final int definedBuffer, final File outputFile) {
                this.tempLogger.info("Pobieranie:&a " + outputFile.getName());
                this.tempLogger.info("Ustalony buffer dla naszego pliku to:&a " + MathUtil.formatBytesDynamic(definedBuffer, false));
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

        final Request request = new RequestBuilder()
                .setUrl("https://minecraft.azureedge.net/bin-win/bedrock-server-1.21.23.01.zip")
                .get()
                .build();

        try (final Connection connection = new Connection(request)) {
            final HttpStatusCode statusCode = connection.getHttpStatusCode();

            if (statusCode == HttpStatusCode.OK) {
                try {
                    final long start = System.currentTimeMillis();

                    final DownloadTask downloadTask = new DownloadTask(connection.getInputStream(), new File(fileName),
                            connection.getContentLength(),
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
                LOGGER.info("Server zwrócił kod:&a " + statusCode.getCode());
            }
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
                LOGGER.info(file.getName() + " = " + MathUtil.formatBytesDynamic(buffer, false));
            }
        }
    }

    private static void connectionTest() throws IOException {
        final Request request = new RequestBuilder()
                .setUrl("https://indianbartonka.pl/userInfo")
                .get()
                .build();

        try (final Connection connection = new Connection(request)) {
            final HttpStatusCode statusCode = connection.getHttpStatusCode();

            LOGGER.info("Czy jest zabezpieczone? " + connection.isHttps());
            LOGGER.info("Wiadomość: " + connection.getResponseMessage());
            LOGGER.info("Kod odpowiedzi: " + statusCode + " (" + statusCode.getCode() + ")");
            LOGGER.print();

            for (final Map.Entry<String, String> headers : connection.getHeaders().entrySet()) {
                LOGGER.print(headers.getKey() + " : " + headers.getValue());
            }

            LOGGER.print();

            if (connection.getInputStream() == null) return;
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    LOGGER.print(line);
                }
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

        try {
            zipTest();
        } catch (final Exception exception){
            exception.printStackTrace();
        }
        LOGGER.print("==================");

        connectionTest();
        LOGGER.print("==================");

        dateUtilTest();
        LOGGER.print("==================");
    }
}
