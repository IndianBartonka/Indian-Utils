package pl.indianbartonka.util.system;

import java.io.File;

public record Disk(String name, File diskFile, String type, long blockSize, boolean readOnly) {
}

//TODO: Przetestuj:
for (FileStore store : FileSystems.getDefault().getFileStores()) {
            try {
                System.out.println("Urządzenie: " + store);
                System.out.println("Typ: " + store.type());
                System.out.println("Wolne miejsce: " + store.getUsableSpace() / (1024 * 1024) + " MB");
                System.out.println("Całkowita pojemność: " + store.getTotalSpace() / (1024 * 1024) + " MB");
                System.out.println("-------");
            } catch (Exception e) {
                System.out.println("Błąd przy dostępie do urządzenia: " + e.getMessage());
            }
}
