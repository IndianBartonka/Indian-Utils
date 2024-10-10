package pl.indianbartonka.util.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import pl.indianbartonka.util.DateUtil;

public final class FileLogFormatter extends Formatter {

    @Override
    public String format(final LogRecord record) {
        return "[" + DateUtil.getTimeHMSMS() + "] " +
                "[" + Thread.currentThread().getName() + "] " +
                "(" + record.getLoggerName() + ") " +
                record.getLevel() + " " +
                this.formatMessage(record) + "\n";
    }
}