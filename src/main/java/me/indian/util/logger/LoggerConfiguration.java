package me.indian.util.logger;

import java.nio.file.Path;

public record LoggerConfiguration(boolean debug, Path logsPath, String appStartDate) {
}
