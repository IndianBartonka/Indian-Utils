package pl.indianbartonka.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.http.HttpStatusCode;
import pl.indianbartonka.util.http.connection.Connection;
import pl.indianbartonka.util.http.connection.request.Request;
import pl.indianbartonka.util.http.connection.request.RequestBuilder;

public class ConnectionTest {

    @Test
    void testConnection() throws IOException {
        final Request request = new RequestBuilder()
                .setUrl("https://indianbartonka.pl/userInfo")
                .GET()
                .build();

        System.out.println("Headery Requestu:");
        request.getHeaders().forEach((key, value) -> System.out.println(key + " : " + value));
        System.out.println();

        final HttpStatusCode statusCode;
        try (final Connection connection = new Connection(request)) {
            statusCode = connection.getHttpStatusCode();

            System.out.println("Czy jest zabezpieczone? " + connection.isHttps());
            System.out.println("Wiadomość: " + connection.getResponseMessage());
            System.out.println("Kod odpowiedzi: " + statusCode + " (" + statusCode.getCode() + ")");
            System.out.println();

            System.out.println("Headery Odpowiedzi:");
            connection.getHeaders().forEach((key, value) -> System.out.println(key + " : " + value));
            System.out.println();

            if (connection.getInputStream() != null) {
                try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            }
        }

        Assertions.assertTrue(statusCode.isSuccess(), "Odpowiedź powinna być udana.");
    }
}
