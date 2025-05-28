package pl.indianbartonka.util.system;

public record Ram(long size, long basicSpeed, long configuredSpeed, String memoryType, String partNumber,
                  String bankLabel) {
}
