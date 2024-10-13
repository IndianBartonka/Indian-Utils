package pl.indianbartonka.util.http.connection;

import java.util.concurrent.TimeUnit;
import pl.indianbartonka.util.http.UserAgentUtil;

public class RequestBuilder {

    private final Request request;

    public RequestBuilder() {
        this.request = new Request();
        this.setUserAgent(UserAgentUtil.USER_AGENT_INDIAN_UTILS);
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

    public RequestBuilder setContentType(final String contentType) {
        this.request.setContentType(contentType);
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

    public RequestBuilder post(final byte[] body) {
        this.request.setBody(body);
        this.request.setRequestMethod("POST");
        return this;
    }

    public RequestBuilder get() {
        this.request.setRequestMethod("GET");
        return this;
    }

    public RequestBuilder put() {
        this.request.setRequestMethod("PUT");
        return this;
    }

    public RequestBuilder delete() {
        this.request.setRequestMethod("DELETE");
        return this;
    }

    public RequestBuilder patch() {
        this.request.setRequestMethod("PATCH");
        return this;
    }

    public Request build() {
        return this.request;
    }
}
