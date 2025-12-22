package pl.indianbartonka.util.http.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.jetbrains.annotations.Nullable;
import pl.indianbartonka.util.BufferUtil;
import pl.indianbartonka.util.MessageUtil;
import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.http.ContentType;
import pl.indianbartonka.util.http.HttpStatusCode;
import pl.indianbartonka.util.http.connection.request.Request;
import pl.indianbartonka.util.http.connection.request.RequestBody;

public class Connection implements AutoCloseable {

    private final Request request;
    private final HttpURLConnection urlConnection;
    private final boolean https;
    private final int rawStatusCode;
    private final HttpStatusCode httpStatusCode;
    private final ContentType contentType;
    private final long contentLength;
    private final Map<String, String> headers;
    private final InputStream inputStream;
    private final String responseMessage;

    public Connection(final Request request) throws IOException {
        this.request = request;

        if (request.getUrl().startsWith("https")) {
            this.urlConnection = (HttpsURLConnection) new URL(request.getUrl()).openConnection();
            this.https = true;
        } else {
            this.urlConnection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            this.https = false;
        }

        for (final Map.Entry<String, String> headersEntry : request.getHeaders().entrySet()) {
            this.urlConnection.setRequestProperty(headersEntry.getKey(), headersEntry.getValue());
        }

        this.urlConnection.setConnectTimeout(request.getConnectTimeout());
        this.urlConnection.setReadTimeout(request.getReadTimeout());
        this.urlConnection.setRequestMethod(request.getRequestMethod());

        this.handleRequestMethod(request);

        this.headers = new HashMap<>();

        for (final Map.Entry<String, List<String>> entry : this.urlConnection.getHeaderFields().entrySet()) {
            this.headers.put(entry.getKey(), MessageUtil.listToSpacedString(entry.getValue()));
        }

        this.rawStatusCode = this.urlConnection.getResponseCode();
        this.httpStatusCode = HttpStatusCode.getByCode(this.rawStatusCode);

        if (this.httpStatusCode.isSuccess()) {
            this.contentType = ContentType.getContentType(this.getResponseContentTypeString());
            this.contentLength = this.urlConnection.getContentLength();
            this.inputStream = this.urlConnection.getInputStream();
        } else {
            this.contentType = null;
            this.contentLength = -1;
            this.inputStream = this.urlConnection.getErrorStream();
        }

        this.responseMessage = this.urlConnection.getResponseMessage();
    }

    private void handleRequestMethod(final Request request) throws IOException {
        final RequestBody requestBody = request.getRequestBody();
        final String requestMethod = request.getRequestMethod();

        if (requestMethod.equals("GET")) return;

        this.urlConnection.setDoOutput(true);

        final ContentType requestContentType = request.getContentType();
        if (requestContentType != null) {
            this.urlConnection.setRequestProperty("Content-Type", requestContentType.getMimeType());
        }

        // Obsługa metod PUT, DELETE, POST
        if (requestBody != null) {
            this.handleDataSending(requestBody);
        } else if (!requestMethod.equals("DELETE")) {
            throw new IllegalArgumentException("RequestBody cannot be null for this request method.");
        }
    }

    private void handleDataSending(final RequestBody requestBody) throws IOException {
        final byte[] body = requestBody.getBody();
        final InputStream bodyInputStream = requestBody.getInputStream();

        if (body != null) {
            try (final OutputStream outputStream = this.urlConnection.getOutputStream()) {
                outputStream.write(body);
                outputStream.flush();
            }
        } else if (bodyInputStream != null) {
            try (final OutputStream outputStream = this.urlConnection.getOutputStream(); bodyInputStream) {
                final long requestBodyContentLength = requestBody.getContentLength();
                final byte[] buffer;

                if (requestBodyContentLength > 0) {
                    buffer = new byte[BufferUtil.calculateOptimalBufferSize(requestBodyContentLength)];
                } else {
                    buffer = new byte[8192];
                }

                int bytesRead;

                while ((bytesRead = bodyInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
        } else {
            throw new IllegalArgumentException("Either body or InputStream must be provided.");
        }
    }

    public HttpURLConnection getUrlConnection() {
        return this.urlConnection;
    }

    public boolean isHttps() {
        return this.https;
    }

    @Since("0.0.9.4")
    public boolean isSuccessful() {
        return this.httpStatusCode.isSuccess();
    }

    public int getRawStatusCode() {
        return this.rawStatusCode;
    }

    public HttpStatusCode getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Nullable
    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Nullable
    public String getResponseMessage() {
        return this.responseMessage;
    }

    @Nullable
    @Since("0.0.9.4")
    public String getBodyAsString() throws IOException {
        final StringBuilder builder = new StringBuilder();

        try (final InputStreamReader streamReader = new InputStreamReader(this.inputStream); final BufferedReader reader = new BufferedReader(streamReader)) {
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }

        return builder.toString();
    }

    @Since("0.0.9.5")
    public String extractFileName() {
        String fileName = null;
        final String disposition = this.urlConnection.getHeaderField("Content-Disposition");

        if (disposition != null && disposition.contains("filename=")) {
            fileName = disposition.substring(disposition.indexOf("filename=") + 9);
            fileName = fileName.replace("\"", "").trim();
        }

        if (fileName == null || fileName.isEmpty()) {
            final String url = this.request.getUrl();
            fileName = url.substring(url.lastIndexOf('/') + 1);
            if (fileName.isEmpty()) fileName = "pobrany_plik";
        }

        return fileName;
    }

    @Since("0.0.9.5")
    @Nullable
    public String getResponseContentTypeString() {
        final String type = this.headers.get("Content-Type");
        if (type != null) {
            return type.split(";")[0].trim();
        }
        return null;
    }

    @Since("0.0.9.5")
    @Nullable
    public ContentType getResponseContentType() {
        return this.contentType;
    }

    @Override
    public void close() throws IOException {
        this.urlConnection.disconnect();
        if (this.inputStream != null) {
            this.inputStream.close();
        }
    }
}
