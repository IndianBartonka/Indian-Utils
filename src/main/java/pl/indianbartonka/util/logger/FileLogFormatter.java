package pl.indianbartonka.util.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import pl.indianbartonka.util.DateUtil;

public final class FileLogFormatter extends Formatter {

    @Override
    public String format(final LogRecord logRecord) {
        return "[" + DateUtil.getTimeHMSMS() + "] " +
                "[" + Thread.currentThread().getName() + "] " +
                "(" + logRecord.getLoggerName() + ") " +
                logRecord.getLevel() + " " +
                this.formatMessage(logRecord) + "\n";
    }
}