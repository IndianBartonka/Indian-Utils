package pl.indianbartonka.util;

import java.io.File;
import java.io.IOException;
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

public final class Test {

    private static final Logger LOGGER = new Logger(new LoggerConfiguration(true, System.getProperty("user.dir") + File.separator + "logs", true)) {
    };
    //Zaleca się tworzenie nowych loggerów na podstawie głównego
    private static final Logger LOGGER2 = LOGGER.prefixed("Logger 2");

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
                .GET()
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

    public static void bufferTest() {
        final File[] files = new File(System.getProperty("user.dir")).listFiles();
        if (files != null) {
            for (final File file : files) {
                final int buffer = BufferUtil.calculateOptimalBufferSize(FileUtil.getFileSize(file));
                LOGGER.info(file.getName() + " = " + MathUtil.formatBytesDynamic(buffer, false));
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        languagesTest();
        LOGGER.print("==================");

        bufferTest();
        LOGGER.print("==================");

        downloadFileTest();
        LOGGER.print("==================");
    }
}
