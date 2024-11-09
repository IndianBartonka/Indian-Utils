package pl.indianbartonka.util.system;

import java.io.File;

public record Disk(String name, File diskFile, String type, long blockSize, boolean readOnly) {
}
