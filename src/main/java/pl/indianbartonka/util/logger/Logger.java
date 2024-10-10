package pl.indianbartonka.util.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import pl.indianbartonka.util.DateUtil;
import pl.indianbartonka.util.file.FileUtil;

public abstract class Logger {

    protected final LoggerConfiguration configuration;
    private final List<Logger> children;
    protected File logFile;
    protected String prefix;
    protected LogState logState;
    protected PrintStream printStream;
    protected boolean temporary;
    private Logger parent;

    public Logger(final Logger parent) {
        this.parent = parent;
        this.configuration = parent.configuration;
        this.children = new ArrayList<>();
        this.logState = LogState.NONE;
        this.updatePrefix();
        this.initializeLogFile();

        parent.children.add(this);
    }

    public Logger(final LoggerConfiguration loggerConfiguration) {
        this.configuration = loggerConfiguration;
        this.children = new ArrayList<>();
        this.logState = LogState.NONE;
        this.updatePrefix();
        this.initializeLogFile();
    }

    public Logger prefixed(final String loggerPrefix) {
        //TODO: Szukaj w dzieciach już istniejącego loggeru x takim ptefixem
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
        return new Logger(Logger.this.configuration) {

            @Override
            protected void initializeLogFile() {
                this.temporary = true;
                super.initializeLogFile();
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
        final Logger parent = this.getParent();
        if (parent != null) {
            this.logFile = parent.getLogFile();
            this.printStream = parent.printStream;
            return;
        }

        final File logsDir = new File(this.configuration.getLogsPath());

        if (!logsDir.exists()) {
            if (!logsDir.mkdir()) if (logsDir.mkdirs()) {
                throw new RuntimeException("Nie można utworzyć miejsca na logi");
            }
        }

        try {
            if (this.configuration.isOneLog()) {
                this.logFile = new File(logsDir, "Latest.log");

                if (this.logFile.exists() && !this.temporary && !this.logFile.delete()) {
                    FileUtil.writeText(this.logFile, List.of(""));
                }

            } else {
                this.logFile = new File(logsDir, this.configuration.getLogName() + ".log");
            }

            final FileOutputStream fileOutputStream = new FileOutputStream(this.logFile, true);
            this.printStream = new PrintStream(fileOutputStream);
        } catch (final Exception exception) {
            this.error("Nie można utworzyć&1 PrintStreamu&r aby zapisywać logi do pliku ", exception);
        }
    }

    private void initLoggerFile(){
        //TODO: Przekieruj inne loggery do naszego pliku
        java.util.logging.Logger rootLogger = Logger.getLogger("");
        FileHandler fileHandler = new FileHandler("app.log", true);
        fileHandler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(fileHandler);

    }
    
    public void print() {
        this.print("");
    }

    public void print(final Object log) {
        this.logState = LogState.NONE;
        this.instantLogToFile(log);

        System.out.println(ConsoleColors.convertMinecraftColors(log));
    }

    public void print(final Object log, final Throwable throwable) {
        this.print(log);
        this.logThrowable(throwable);
    }

    public void info(final Object log) {
        this.logState = LogState.INFO;
        this.updatePrefix();
        System.out.println(ConsoleColors.convertMinecraftColors(this.prefix + log));
        this.logToFile(log);
    }

    public void info(final Object log, final Throwable throwable) {
        this.info(log);
        this.logThrowable(throwable);
    }

    public void warning(final Object log) {
        this.logState = LogState.WARNING;
        this.updatePrefix();
        System.out.println(ConsoleColors.convertMinecraftColors(this.prefix + log));
        this.logToFile(log);
    }

    public void warning(final Object log, final Throwable throwable) {
        this.warning(log);
        this.logThrowable(throwable);
    }

    public void alert(final Object log) {
        this.logState = LogState.ALERT;
        this.updatePrefix();
        System.out.println(ConsoleColors.convertMinecraftColors(this.prefix + log));
        this.logToFile(log);
    }

    public void alert(final Object log, final Throwable throwable) {
        this.alert(log);
        this.logThrowable(throwable);
    }

    public void critical(final Object log) {
        this.logState = LogState.CRITICAL;
        this.updatePrefix();
        System.out.println(ConsoleColors.convertMinecraftColors(this.prefix + log));
        this.logToFile(log);
    }

    public void critical(final Object log, final Throwable throwable) {
        this.critical(log);
        this.logThrowable(throwable);
    }

    public void error(final Object log) {
        this.logState = LogState.ERROR;
        this.updatePrefix();
        System.out.println(ConsoleColors.convertMinecraftColors(this.prefix + log));
        this.logToFile(log);
    }

    public void error(final Object log, final Throwable throwable) {
        this.error(log);
        this.logThrowable(throwable);
    }

    public void debug(final Object log) {
        if (this.configuration.isDebug()) {
            this.logState = LogState.DEBUG;
            this.updatePrefix();
            this.logToFile(log);
            System.out.println(ConsoleColors.convertMinecraftColors(this.prefix + log));
        }
    }

    public void debug(final Object log, final Throwable throwable) {
        if (this.configuration.isDebug()) {
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
            this.printStream.println(ConsoleColors.removeColors(log));
        }
    }

    private void logToFile(final Object log) {
        if (this.printStream != null) {
            this.printStream.println(ConsoleColors.removeColors(this.prefix + log));
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

    public Logger getParent() {
        return this.parent;
    }

    public List<Logger> getChildren() {
        return this.children;
    }

    public File getLogFile() {
        return this.logFile;
    }
}
