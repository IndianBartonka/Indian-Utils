package pl.indianbartonka.util.system.parts;

import java.io.File;
import pl.indianbartonka.util.annotation.Since;

public record Disk(String name, @Since("0.0.9.5") String model, File diskFile, String type, long blockSize, boolean readOnly) {
}
