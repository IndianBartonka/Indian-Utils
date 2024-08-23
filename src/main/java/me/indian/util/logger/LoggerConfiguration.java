package me.indian.util.logger;

public class LoggerConfiguration {

    private final boolean debug;
    private final String logsPath;
    private boolean oneLog;
    private String logName;

    public LoggerConfiguration(final boolean debug, final String logsPath, final String logName) {
        this.debug = debug;
        this.logsPath = logsPath;
        this.logName = logName;
    }

    public LoggerConfiguration(final boolean debug, final String logsPath, final boolean oneLog) {
        this.debug = debug;
        this.logsPath = logsPath;
        this.oneLog = oneLog;
    }

    public boolean isOneLog() {
        return this.oneLog;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public String getLogsPath() {
        return this.logsPath;
    }

    public String getLogName() {
        return this.logName;
    }
}