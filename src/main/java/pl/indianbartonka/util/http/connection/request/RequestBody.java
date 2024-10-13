package pl.indianbartonka.util.http.connection.request;

import java.io.InputStream;

public class RequestBody {

    private final byte[] body;
    private final InputStream inputStream;
    private final long contentLength;

    public RequestBody(final byte[] body, final long contentLength) {
        this.body = body;
        this.contentLength = contentLength;
        this.inputStream = null;
    }

    public RequestBody(final byte[] body) {
        this.body = body;
        this.contentLength = 0;
        this.inputStream = null;
    }

    public RequestBody(final InputStream inputStream, final long contentLength) {
        this.inputStream = inputStream;
        this.contentLength = contentLength;
        this.body = null;
    }

    public RequestBody(final InputStream inputStream) {
        this.inputStream = inputStream;
        this.contentLength = 0;
        this.body = null;
    }

    public byte[] getBody() {
        return this.body;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public long getContentLength() {
        return this.contentLength;
    }
}
