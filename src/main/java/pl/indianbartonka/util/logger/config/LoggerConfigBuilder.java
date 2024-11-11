package pl.indianbartonka.util.logger.config;

public class LoggerConfigBuilder {

    private final LoggerConfiguration loggerConfiguration;

    public LoggerConfigBuilder() {
        this.loggerConfiguration = new LoggerConfiguration();
        this.loggerConfiguration.setLoggingToFile(true);
        this.loggerConfiguration.setLogJULtoFile(false);
        this.loggerConfiguration.setLogsPath(System.getProperty("user.dir"));
        this.loggerConfiguration.setOneLog(true);
        this.loggerConfiguration.setLogName("Latest");
    }

    public LoggerConfigBuilder setDebug(final boolean debug) {
        this.loggerConfiguration.setDebug(debug);
        return this;
    }

    public LoggerConfigBuilder setLogsPath(final String logsPath) {
        this.loggerConfiguration.setLogsPath(logsPath);
        return this;
    }

    public LoggerConfigBuilder setLoggingToFile(final boolean logFile) {
        this.loggerConfiguration.setLoggingToFile(logFile);
        return this;
    }

    public LoggerConfigBuilder setLogJULtoFile(final boolean logJULtoFile) {
        this.loggerConfiguration.setLogJULtoFile(logJULtoFile);
        return this;
    }

    public LoggerConfigBuilder setOneLog(final boolean oneLog) {
        this.loggerConfiguration.setOneLog(oneLog);
        return this;
    }

    public LoggerConfigBuilder setLogName(final String logName) {
        this.loggerConfiguration.setLogName(logName);
        return this;
    }

    public LoggerConfiguration build() {
        return this.loggerConfiguration;
    }
}
