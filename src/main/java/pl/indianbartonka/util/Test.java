package pl.indianbartonka.util;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import pl.indianbartonka.util.download.DownloadListener;
import pl.indianbartonka.util.download.DownloadTask;
import pl.indianbartonka.util.http.HttpStatusCode;
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

public final class Test {

    private static final LoggerConfiguration loggerConfiguration = LoggerConfiguration.builder()
            .setLogsPath(System.getProperty("user.dir") + File.separator + "logs")
            .setLoggingToFile(true)
            .setOneLog(true)
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
//                this.tempLogger.print(progress + "%&a " + formatedSpeed + "MB/s &c" + remainingTimeString);
            }

            @Override
            public void onProgress(final int progress, final double formatedSpeed, final String remainingTimeString) {
//                tempLogger.info(progress + "%&a " + formatedSpeed + "MB/s &c" + remainingTimeString);
                this.tempLogger.print(progress + "%&a " + formatedSpeed + "MB/s &c" + remainingTimeString, LogState.INFO);
            }

            @Override
            public void onTimeout(final int timeOutSeconds) {
                this.tempLogger.info("TimeOut");
            }

            @Override
            public void onEnd(final File outputFile) {
                this.tempLogger.println();
                this.tempLogger.info("Pobrano:&a " + outputFile.getName());
            }

            @Override
            public void onDownloadStop() {
                this.tempLogger.println();
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

                    final ThreadUtil threadUtil = new ThreadUtil("DownloadTest");

                    threadUtil.newThread(() -> {
                        ThreadUtil.sleep(10);
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

    public static void main(final String[] args) throws IOException {
        languagesTest();
        LOGGER.println("==================");

        downloadFileTest();
        LOGGER.println("==================");
    }
}

//TODO: Dodja to w jakimś info o pliku 

import java.nio.file.*;
import java.nio.file.attribute.DosFileAttributes;
import java.io.IOException;

public class DosFileAttributesExample {
    public static void main(String[] args) {
        Path path = Paths.get("example.txt");

        try {
            DosFileAttributes dosAttrs = Files.readAttributes(path, DosFileAttributes.class);
            
            System.out.println("Jest ukryty: " + dosAttrs.isHidden());
            System.out.println("Tylko do odczytu: " + dosAttrs.isReadOnly());
            System.out.println("Atrybut archiwalny: " + dosAttrs.isArchive());
            System.out.println("Atrybut systemowy: " + dosAttrs.isSystem());
            
            // Przykład ustawienia pliku jako ukrytego
            Files.setAttribute(path, "dos:hidden", true);
            System.out.println("Plik ustawiono jako ukryty.");
            
        } catch (IOException e) {
            System.err.println("Błąd dostępu do atrybutów pliku: " + e.getMessage());
        }
    }
}

import java.nio.file.*;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.io.IOException;
import java.util.Set;

public class PosixFileAttributesExample {
    public static void main(String[] args) {
        Path path = Paths.get("example.txt");

        try {
            PosixFileAttributes posixAttrs = Files.readAttributes(path, PosixFileAttributes.class);

            System.out.println("Właściciel: " + posixAttrs.owner().getName());
            System.out.println("Grupa: " + posixAttrs.group().getName());
            System.out.println("Uprawnienia: " + PosixFilePermissions.toString(posixAttrs.permissions()));

            // Ustawienie uprawnień pliku na rwxr-xr--
            Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr--");
            Files.setPosixFilePermissions(path, perms);
            System.out.println("Zaktualizowano uprawnienia pliku do rwxr-xr--.");
            
        } catch (UnsupportedOperationException e) {
            System.err.println("Atrybuty POSIX nie są obsługiwane w tym systemie operacyjnym.");
        } catch (IOException e) {
            System.err.println("Błąd dostępu do atrybutów pliku: " + e.getMessage());
        }
    }
}


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class DesktopExample {
    public static void main(String[] args) {
        // Sprawdzanie, czy klasa Desktop jest obsługiwana na tym systemie
        if (!Desktop.isDesktopSupported()) {
            System.out.println("Klasa Desktop nie jest obsługiwana na tym systemie.");
            return;
        }

        Desktop desktop = Desktop.getDesktop();

        // Przykłady użycia funkcji klasy Desktop
        try {
            // 1. Otwieranie przeglądarki internetowej i przechodzenie na stronę
            URI uri = new URI("https://www.openai.com");
            desktop.browse(uri);
            System.out.println("Otworzono przeglądarkę z adresem URL: " + uri);

            // 2. Otwieranie pliku
            File file = new File("example.txt");
            if (file.exists()) {
                desktop.open(file);
                System.out.println("Otworzono plik: " + file.getAbsolutePath());
            } else {
                System.out.println("Plik nie istnieje: " + file.getAbsolutePath());
            }

            // 3. Uruchamianie domyślnego edytora tekstu
            File editableFile = new File("editable.txt");
            desktop.edit(editableFile);
            System.out.println("Otworzono plik do edycji: " + editableFile.getAbsolutePath());

            // 4. Wysyłanie e-maila za pomocą domyślnego klienta poczty
            URI mailto = new URI("mailto:someone@example.com?subject=Hello%20World");
            desktop.mail(mailto);
            System.out.println("Uruchomiono klienta poczty z adresem: " + mailto);

        } catch (IOException e) {
            System.err.println("Błąd operacji na plikach lub aplikacjach: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Wystąpił inny błąd: " + e.getMessage());
        }
    }
}

