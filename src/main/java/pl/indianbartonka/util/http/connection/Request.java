package pl.indianbartonka.util.http.connection;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private final Map<String, String> headers;
    private String url;
    private String userAgent;
    private String contentType;
    private String requestMethod;
    private String authorization;
    private String accept;
    private byte[] body;
    private int connectTimeout;
    private int readTimeout;

    public Request() {
        this.headers = new HashMap<>();
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public void setRequestMethod(final String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public byte[] getBody() {
        return this.body;
    }

    public void setBody(final byte[] body) {
        this.body = body;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setHeader(final String key, final String value) {
        this.headers.put(key, value);
    }

    public void addHeader(final String key, final String value) {
        this.headers.put(key, value);
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
        this.headers.put("User-Agent", userAgent);
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
        this.headers.put("Content-Type", contentType);
    }

    public String getAuthorization() {
        return this.authorization;
    }

    public void setAuthorization(final String auth) {
        this.authorization = auth;
        this.headers.put("Authorization", auth);
    }

    public String getAccept() {
        return this.accept;
    }

    public void setAccept(final String accept) {
        this.accept = accept;
        this.headers.put("Accept", accept);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }
}
