package pl.indianbartonka.util.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import pl.indianbartonka.util.DateUtil;
import pl.indianbartonka.util.color.AnsiColor;
import pl.indianbartonka.util.exception.LoggerException;
import pl.indianbartonka.util.file.FileUtil;
import pl.indianbartonka.util.logger.config.FileLogFormatter;
import pl.indianbartonka.util.logger.config.LoggerConfiguration;

public abstract class Logger {

    protected final LoggerConfiguration configuration;
    private final List<Logger> children;
    protected File logFile;
    protected String prefix;
    protected LogState logState;
    protected PrintStream printStream;
    private Logger parent;
    private boolean debug;

    protected Logger(final Logger parent) {
        this.parent = parent;
        this.configuration = parent.configuration;
        this.children = new ArrayList<>();
        this.logState = LogState.NONE;
        this.updatePrefix();
        this.initializeLogFile();

        parent.children.add(this);
        this.debug = parent.configuration.isDebug();
    }

    protected Logger(final LoggerConfiguration loggerConfiguration) {
        this.configuration = loggerConfiguration;
        this.children = new ArrayList<>();
        this.logState = LogState.NONE;
        this.updatePrefix();
        this.initializeLogFile();
        this.debug = loggerConfiguration.isDebug();
    }

    public Logger prefixed(final String loggerPrefix) {
        return new Logger(this) {
            @Override
            protected void updatePrefix() {
                final String logStateColor = this.logState.getColorCode();
                this.prefix = "&a[" + DateUtil.getTimeHMSMS() + "] &e[&7" +
                        Thread.currentThread().getName() + "&r&e] (&f" + loggerPrefix + "&e) "
                        + logStateColor + this.logState.name().toUpperCase() + " &r";
            }
        };
    }

    public Logger tempLogger(final String loggerPrefix) {
        final Logger parent = this;
        return new Logger(Logger.this.configuration) {

            @Override
            protected void initializeLogFile() {
                this.logFile = parent.getLogFile();
                this.printStream = parent.printStream;
            }

            @Override
            protected void updatePrefix() {
                final String logStateColor = this.logState.getColorCode();
                this.prefix = "&a[" + DateUtil.getTimeHMSMS() + "] &e[&7" +
                        Thread.currentThread().getName() + "&r&e] (&f" + loggerPrefix + "&e) "
                        + logStateColor + this.logState.name().toUpperCase() + " &r";
            }
        };
    }

    protected void updatePrefix() {
        final String logStateColor = this.logState.getColorCode();
        this.prefix = "&a[" + DateUtil.getTimeHMSMS() + "] &e[&7" +
                Thread.currentThread().getName() + "&r&e]&r "
                + logStateColor + this.logState.name().toUpperCase() + " &r";
    }

    protected void initializeLogFile() {
        if(!this.configuration.isFileLogging()) return;

        final Logger parent = this.getParent();
        if (parent != null) {
            this.logFile = parent.getLogFile();
            this.printStream = parent.printStream;
            return;
        }

        final File logsDir = new File(this.configuration.getLogsPath());

        if (!logsDir.exists() && !logsDir.mkdir() && !logsDir.mkdirs()) {
            throw new LoggerException("Nie można utworzyć miejsca na logi");
        }

        try {
            if (this.configuration.isOneLog()) {
                this.logFile = new File(logsDir, "Latest.log");

                if (this.logFile.exists() && !this.logFile.delete()) {
                    FileUtil.writeText(this.logFile, List.of(""));
                }

            } else {
                this.logFile = new File(logsDir, this.configuration.getLogName() + ".log");
            }

            final FileOutputStream fileOutputStream = new FileOutputStream(this.logFile, true);
            this.printStream = new PrintStream(fileOutputStream);
            this.initLoggerFile(this.logFile);
        } catch (final Exception exception) {
            this.error("Nie można utworzyć&1 PrintStreamu&r aby zapisywać logi do pliku ", exception);
        }
    }

    private void initLoggerFile(final File file) {
        if (!this.configuration.isLogJULtoFile()) return;
        try {
            final java.util.logging.Logger rootLogger = java.util.logging.Logger.getLogger("");

            boolean hasFileHandler = false;

            for (final Handler handler : rootLogger.getHandlers()) {
                if (handler instanceof FileHandler) {
                    hasFileHandler = true;
                    break;
                }
            }

            if (!hasFileHandler) {
                final FileHandler fileHandler = new FileHandler(file.getAbsolutePath(), true);
                fileHandler.setFormatter(new FileLogFormatter());
                rootLogger.addHandler(fileHandler);
            }
        } catch (final IOException exception) {
            this.debug("&cNie udało przekazac się logowania do pliku:&b " + file.getName(), exception);
        }
    }

    public void print() {
        this.print("");
    }

    public void print(final Object log) {
        this.logState = LogState.NONE;
        this.instantLogToFile(log);

        System.out.println(AnsiColor.convertMinecraftColors(log));
    }

    public void print(final Object log, final Throwable throwable) {
        this.print(log);
        this.logThrowable(throwable);
    }

    public void info(final Object log) {
        this.logState = LogState.INFO;
        this.updatePrefix();
        System.out.println(AnsiColor.convertMinecraftColors(this.prefix + log));
        this.logToFile(log);
    }

    public void info(final Object log, final Throwable throwable) {
        this.info(log);
        this.logThrowable(throwable);
    }

    public void warning(final Object log) {
        this.logState = LogState.WARNING;
        this.updatePrefix();
        System.out.println(AnsiColor.convertMinecraftColors(this.prefix + log));
        this.logToFile(log);
    }

    public void warning(final Object log, final Throwable throwable) {
        this.warning(log);
        this.logThrowable(throwable);
    }

    public void alert(final Object log) {
        this.logState = LogState.ALERT;
        this.updatePrefix();
        System.out.println(AnsiColor.convertMinecraftColors(this.prefix + log));
        this.logToFile(log);
    }

    public void alert(final Object log, final Throwable throwable) {
        this.alert(log);
        this.logThrowable(throwable);
    }

    public void critical(final Object log) {
        this.logState = LogState.CRITICAL;
        this.updatePrefix();
        System.out.println(AnsiColor.convertMinecraftColors(this.prefix + log));
        this.logToFile(log);
    }

    public void critical(final Object log, final Throwable throwable) {
        this.critical(log);
        this.logThrowable(throwable);
    }

    public void error(final Object log) {
        this.logState = LogState.ERROR;
        this.updatePrefix();
        System.out.println(AnsiColor.convertMinecraftColors(this.prefix + log));
        this.logToFile(log);
    }

    public void error(final Object log, final Throwable throwable) {
        this.error(log);
        this.logThrowable(throwable);
    }

    public void debug(final Object log) {
        if (this.debug) {
            this.logState = LogState.DEBUG;
            this.updatePrefix();
            this.logToFile(log);
            System.out.println(AnsiColor.convertMinecraftColors(this.prefix + log));
        }
    }

    public void debug(final Object log, final Throwable throwable) {
        if (this.debug) {
            this.debug(log);
            this.logThrowable(throwable);
        }
    }

    public void logByState(final Object log, final LogState logState) {
        this.logByState(log, null, logState);
    }

    public void logByState(final Object log, final Throwable throwable, final LogState logState) {
        switch (logState) {
            case NONE -> this.print(log, throwable);
            case INFO -> this.info(log, throwable);
            case ALERT -> this.alert(log, throwable);
            case CRITICAL -> this.critical(log, throwable);
            case ERROR -> this.error(log, throwable);
            case WARNING -> this.warning(log, throwable);
            case DEBUG -> this.debug(log, throwable);
        }
    }

    public void instantLogToFile(final Object log) {
        if (this.printStream != null) {
            this.printStream.println(AnsiColor.removeColors(log));
        }
    }

    private void logToFile(final Object log) {
        if (this.printStream != null) {
            this.printStream.println(AnsiColor.removeColors(this.prefix + log));
        }
    }

    public void logThrowable(final Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
            if (this.printStream != null) {
                throwable.printStackTrace(this.printStream);
            }
        }
    }

    public List<Logger> getChildren() {
        return this.children;
    }

    public File getLogFile() {
        return this.logFile;
    }

    public Logger getParent() {
        return this.parent;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(final boolean debug) {
        this.debug = debug;
    }
}
