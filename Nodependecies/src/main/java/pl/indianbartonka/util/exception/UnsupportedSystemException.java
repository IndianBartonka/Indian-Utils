package pl.indianbartonka.util.exception;

public class UnsupportedSystemException extends RuntimeException {

    public UnsupportedSystemException(final String message) {
        super(message);
    }

    public UnsupportedSystemException(final String message, final Throwable cause) {
        super(message, cause);
    }
}