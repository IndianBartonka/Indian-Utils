package pl.indianbartonka.util.http.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import pl.indianbartonka.util.MessageUtil;
import pl.indianbartonka.util.http.HttpStatusCode;

public class Connection implements AutoCloseable {

    private final HttpURLConnection connection;
    private final boolean https;
    private final HttpStatusCode httpStatusCode;
    private final long contentLength;
    private final Map<String, String> headers;
    private final InputStream inputStream;

    public Connection(final Request request) throws IOException {
        if (request.getUrl().startsWith("https")) {
            this.connection = (HttpsURLConnection) new URL(request.getUrl()).openConnection();
            this.https = true;
        } else {
            this.connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            this.https = false;
        }

        for (final Map.Entry<String, String> headersEntry : request.getHeaders().entrySet()) {
            this.connection.setRequestProperty(headersEntry.getKey(), headersEntry.getValue());
        }

        this.connection.setConnectTimeout(request.getConnectTimeout());
        this.connection.setReadTimeout(request.getReadTimeout());
        this.connection.setRequestMethod(request.getRequestMethod());

        //TODO: Zainplementuj put, delete, patch
        if (request.getRequestMethod().equals("POST")) {
            this.connection.setDoOutput(true);

            if (request.getBody() != null) {
                try (final OutputStream outputStream = this.connection.getOutputStream()) {
                    outputStream.write(request.getBody());
                    outputStream.flush();
                }
            }
        }

        this.httpStatusCode = HttpStatusCode.getStatus(this.connection.getResponseCode());
        this.contentLength = this.connection.getContentLength();
        this.headers = new HashMap<>();

        for (final Map.Entry<String, List<String>> entry : this.connection.getHeaderFields().entrySet()) {
            this.headers.put(entry.getKey(), MessageUtil.listToSpacedString(entry.getValue()));
        }

        this.inputStream = this.connection.getInputStream();
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public HttpStatusCode getHttpCode() {
        return this.httpStatusCode;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public HttpURLConnection getConnection() {
        return this.connection;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public boolean isHttps() {
        return this.https;
    }

    @Override
    public void close() {
        this.connection.disconnect();
    }
}
