package pl.indianbartonka.util.system;

import pl.indianbartonka.util.annotation.Since;

@Since("0.0.9.3")
public record Ram(long size, long basicSpeed, long configuredSpeed, String memoryType, String partNumber,
                  String bankLabel) {
}
