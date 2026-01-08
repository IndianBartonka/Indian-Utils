package pl.indianbartonka.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import pl.indianbartonka.util.download.DownloadListener;
import pl.indianbartonka.util.download.DownloadTask;
import pl.indianbartonka.util.http.HttpStatusCode;
import pl.indianbartonka.util.http.UserAgent;
import pl.indianbartonka.util.http.connection.Connection;
import pl.indianbartonka.util.http.connection.request.Request;
import pl.indianbartonka.util.http.connection.request.RequestBuilder;
import pl.indianbartonka.util.language.Language;
import pl.indianbartonka.util.language.LanguageManager;
import pl.indianbartonka.util.language.storage.impl.HashMapStorageStrategy;
import pl.indianbartonka.util.language.storage.impl.PropertiesStorageStrategy;
import pl.indianbartonka.util.logger.LogState;
import pl.indianbartonka.util.logger.Logger;
import pl.indianbartonka.util.logger.config.LoggerConfiguration;
import pl.indianbartonka.util.swing.panel.ProgressPanel;
import pl.indianbartonka.util.swing.panel.TextPanelWithComponent;

public final class Test {

    private static final JFrame frame = new JFrame();
    private static final LoggerConfiguration loggerConfiguration = LoggerConfiguration.builder()
            .setLogsPath(System.getProperty("user.dir") + File.separator + "logs")
            .setLoggingToFile(true)
            .build();

    private static final Logger LOGGER = new Logger(loggerConfiguration) {
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
        final DownloadListener downloadListener = new DownloadListener() {
            Logger tempLogger;
            final ProgressPanel panel = new ProgressPanel();

            @Override
            public void onStart(final int definedBuffer, final long fileSize, final File outputFile) {
                tempLogger = LOGGER.tempLogger(outputFile.getName());

                this.tempLogger.info("Pobieranie:&a " + outputFile.getName());
                this.tempLogger.info("Rozmiar pliku to:&a " + MathUtil.formatBytesDynamic(fileSize));
                this.tempLogger.info("Ustalony buffer dla naszego pliku to:&a " + MathUtil.formatBytesDynamic(definedBuffer, false));

                frame.add(this.panel);
                this.panel.setText("Rozpoczynanie");
                this.panel.setValue(0);
            }

            @Override
            public void onSecond(final int progress, final double formatedSpeed, final String remainingTimeString) {
                this.tempLogger.print(progress + "%&a " + formatedSpeed + "MB/s &c" + remainingTimeString, LogState.INFO);
                this.panel.setText(progress + "% " + formatedSpeed + "MB/s " + remainingTimeString);
                this.panel.setValue(progress);
            }

            @Override
            public void onProgress(final int progress, final double formatedSpeed, final String remainingTimeString) {

            }

            @Override
            public void onTimeout(final int timeOutSeconds) {
                this.tempLogger.info("TimeOut");
            }

            @Override
            public void onEnd(final File outputFile) {
                this.tempLogger.println();
                this.tempLogger.info("Pobrano:&a " + outputFile.getName());
                this.panel.removePanel();
            }

            @Override
            public void onDownloadStop(File outputFile) {
                this.tempLogger.println();
                this.tempLogger.alert("Zatrzymano pobieranie!");
                this.panel.removePanel();
            }
        };

        final Request request = new RequestBuilder()
                .setUrl("https://ftp.icm.edu.pl/pub/Linux/dist/linuxmint/isos/stable/22.2/linuxmint-22.2-cinnamon-64bit.iso")
                .setUserAgent(UserAgent.USER_AGENT_EDGE)
                .GET()
                .build();

        try (final Connection connection = new Connection(request)) {
            final HttpStatusCode statusCode = connection.getHttpStatusCode();

            LOGGER.info(connection.getResponseContentType());
            LOGGER.info(connection.getResponseContentTypeString());

            if (statusCode == HttpStatusCode.OK) {
                try {
                    final long start = System.currentTimeMillis();

                    final DownloadTask downloadTask = new DownloadTask(connection.getInputStream(), new File(connection.extractFileName()),
                            connection.getContentLength(),
                            30,
                            downloadListener
                    );

                    LOGGER.info(downloadTask);

//                    final ThreadUtil threadUtil = new ThreadUtil("DownloadTest");

//                    threadUtil.newThread(() -> {
//                        ThreadUtil.sleep(10);
//                        LOGGER.println();
//                        LOGGER.info("Zatrzymywanie pobierania:&b " + fileName);
//                        downloadTask.stopDownload();
//                    }).start();

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

    public static String calculateHashBase64(final String input, final String algorithm) {
        try {
            // Tworzymy instancję algorytmu
            final MessageDigest digest = MessageDigest.getInstance(algorithm);

            // Obliczamy hash
            final byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Kodujemy hash w Base64
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (final Exception e) {
            throw new RuntimeException("Błąd podczas obliczania hasha w Base64", e);
        }
    }

    public static void main(String[] args) {
        try {
            // Komenda sprawdzająca klucz rejestru dla zainstalowanych aplikacji (64-bit)
            String command = "reg query HKLM\\Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall /s /f \"DisplayName\"";

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = process.inputReader();

            String line;
            System.out.println("Lista zainstalowanych programów:");
            System.out.println("--------------------------------");

            while ((line = reader.readLine()) != null) {
                if (line.contains("DisplayName")) {
                    // Wyciąganie samej nazwy programu z wiersza rejestru
                    String programName = line.split("REG_SZ")[1].trim();
                    System.out.println(programName);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void maind(final String[] args) throws IOException {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 250);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);

        final JButton jButton = new JButton("Wyłącz");

        jButton.addActionListener(l -> System.exit(0));

        final JPanel content = new JPanel();
        content.add(new TextPanelWithComponent("Wyłacz", jButton));

        frame.setContentPane(content);

        // Pobieramy dostępne algorytmy MessageDigest
        final Set<String> algorithms = Security.getAlgorithms("MessageDigest");

        // Iterujemy przez wszystkie dostępne algorytmy
        for (final String algorithm : algorithms) {
            try {
                // Obliczamy hash i wypisujemy w formacie Base64
                final String hashBase64 = calculateHashBase64("Siur", algorithm);
                System.out.println(algorithm + " (Base64): " + hashBase64);
            } catch (final Exception e) {
                // W przypadku błędu (np. niezainstalowanego algorytmu) ignorujemy
                System.out.println(algorithm + " (Base64): Błąd");
            }
        }

        // Wyświetlamy dostępne algorytmy
        System.out.println("Dostępne algorytmy " + algorithms.size() + " : " + algorithms);

        downloadFileTest();
        LOGGER.println("==================");

        languagesTest();
        LOGGER.println("==================");
    }
}