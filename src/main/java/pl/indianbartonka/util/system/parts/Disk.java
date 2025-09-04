package pl.indianbartonka.util.system.parts;

import java.io.File;

public record Disk(String name, String model, File diskFile, String type, long blockSize, boolean readOnly) {
}
