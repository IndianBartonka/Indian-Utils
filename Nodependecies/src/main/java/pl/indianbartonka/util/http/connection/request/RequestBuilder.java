package pl.indianbartonka.util.http.connection.request;

import java.util.concurrent.TimeUnit;
import pl.indianbartonka.util.http.ContentType;
import pl.indianbartonka.util.http.UserAgent;

public class RequestBuilder {

    private final Request request;

    public RequestBuilder() {
        this.request = new Request();
        this.setUserAgent(UserAgent.USER_AGENT_INDIAN_UTILS);
        this.setConnectTimeout(10, TimeUnit.MINUTES);
        this.setReadTimeout(5, TimeUnit.MINUTES);
    }

    public RequestBuilder setUrl(final String url) {
        this.request.setUrl(url);
        return this;
    }

    public RequestBuilder setUserAgent(final String userAgent) {
        this.request.setUserAgent(userAgent);
        return this;
    }

    public RequestBuilder setContentType(final ContentType contentType) {
        this.request.setContentType(contentType);
        return this;
    }

    public RequestBuilder setContentLength(final long contentLength) {
        this.request.setContentLength(contentLength);
        return this;
    }

    public RequestBuilder setAuthorization(final String auth) {
        this.request.setAuthorization(auth);
        return this;
    }

    public RequestBuilder setAccept(final String accept) {
        this.request.setAccept(accept);
        return this;
    }

    public RequestBuilder setConnectTimeout(final int connectTimeout, final TimeUnit timeUnit) {
        this.request.setConnectTimeout((int) timeUnit.toMillis(connectTimeout));
        return this;
    }

    public RequestBuilder setReadTimeout(final int readTimeout, final TimeUnit timeUnit) {
        this.request.setReadTimeout((int) timeUnit.toMillis(readTimeout));
        return this;
    }

    public RequestBuilder addHeader(final String key, final String value) {
        this.request.addHeader(key, value);
        return this;
    }

    public RequestBuilder GET() {
        this.request.setRequestMethod("GET");
        return this;
    }

    public RequestBuilder POST(final RequestBody requestBody) {
        this.request.setRequestBody(requestBody);
        this.request.setRequestMethod("POST");
        return this;
    }

    public RequestBuilder PUT(final RequestBody requestBody) {
        this.request.setRequestBody(requestBody);
        this.request.setRequestMethod("PUT");
        return this;
    }

    public RequestBuilder DELETE() {
        this.request.setRequestMethod("DELETE");
        return this;
    }

    public RequestBuilder DELETE(final RequestBody requestBody) {
        this.request.setRequestBody(requestBody);
        this.request.setRequestMethod("DELETE");
        return this;
    }

    public Request build() {
        return this.request;
    }
}
