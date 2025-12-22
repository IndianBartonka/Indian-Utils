package pl.indianbartonka.util.exception;

public class ThreadException extends RuntimeException {

    private final String threadName;

    public ThreadException() {
        super();
        this.threadName = Thread.currentThread().getName();
    }

    public ThreadException(final String message) {
        super(message);
        this.threadName = Thread.currentThread().getName();
    }

    public ThreadException(final String message, final Throwable cause) {
        super(message, cause);
        this.threadName = Thread.currentThread().getName();
    }

    public ThreadException(final Throwable cause) {
        super(cause);
        this.threadName = Thread.currentThread().getName();
    }

    protected ThreadException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.threadName = Thread.currentThread().getName();
    }

    public String getThreadName() {
        return this.threadName;
    }
}
