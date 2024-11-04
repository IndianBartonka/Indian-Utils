package pl.indianbartonka.util.logger.config;

public class LoggerConfiguration {

    private boolean debug;
    private String logsPath;
    private boolean fileLogging;
    private boolean logJULtoFile;
    private boolean oneLog;
    private String logName;

    public static LoggerConfigBuilder builder() {
        return new LoggerConfigBuilder();
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    public String getLogsPath() {
        return this.logsPath;
    }

    public void setLogsPath(final String logsPath) {
        this.logsPath = logsPath;
    }

    public boolean isFileLogging() {
        return this.fileLogging;
    }

    public void setFileLogging(final boolean fileLogging) {
        this.fileLogging = fileLogging;
    }

    public boolean isLogJULtoFile() {
        return this.logJULtoFile;
    }

    public void setLogJULtoFile(final boolean logJULtoFile) {
        this.logJULtoFile = logJULtoFile;
    }

    public boolean isOneLog() {
        return this.oneLog;
    }

    public void setOneLog(final boolean oneLog) {
        this.oneLog = oneLog;
    }

    public String getLogName() {
        return this.logName;
    }

    public void setLogName(final String logName) {
        this.logName = logName;
    }
}