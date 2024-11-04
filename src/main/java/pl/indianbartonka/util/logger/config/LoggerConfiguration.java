package pl.indianbartonka.util.logger.config;

public class LoggerConfiguration {

    private final boolean debug;
    private final String logsPath;
    private final boolean logJULtoFile;
    private boolean oneLog;
    private String logName;

    public LoggerConfiguration(final boolean debug, final String logsPath, final boolean logJULtoFile, final String logName) {
        this.debug = debug;
        this.logsPath = logsPath;
        this.logJULtoFile = logJULtoFile;
        this.logName = logName;
    }

    public LoggerConfiguration(final boolean debug, final String logsPath, final String logName) {
        this.debug = debug;
        this.logsPath = logsPath;
        this.logJULtoFile = true;
        this.logName = logName;
    }

    public LoggerConfiguration(final boolean debug, final String logsPath, final boolean oneLog, final boolean logJULtoFile) {
        this.debug = debug;
        this.logsPath = logsPath;
        this.oneLog = oneLog;
        this.logJULtoFile = logJULtoFile;
    }

    public LoggerConfiguration(final boolean debug, final String logsPath, final boolean oneLog) {
        this.debug = debug;
        this.logsPath = logsPath;
        this.oneLog = oneLog;
        this.logJULtoFile = true;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public String getLogsPath() {
        return this.logsPath;
    }

    public boolean isOneLog() {
        return this.oneLog;
    }

    public boolean isLogJULtoFile() {
        return this.logJULtoFile;
    }

    public String getLogName() {
        return this.logName;
    }
}