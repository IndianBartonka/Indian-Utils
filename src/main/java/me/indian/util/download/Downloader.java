package me.indian.util.download;

public class Downloader{
//TODO: Dodać w IndianUtils util do zapisywania pliku i ustalania buforu, wraz z opcją timeouta, i z opcją pokazania szybkości pobierania 
            try (final FileOutputStream fileOutputStream = new FileOutputStream(donwloadedFile)) {
               final byte[] buffer = new byte[8_388_608];
                int bytesRead;
                long totalBytesRead = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    lastActivityTime = System.currentTimeMillis();
                    fileOutputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;

                    if (System.currentTimeMillis() - lastActivityTime > inactivityTimeoutMillis) {
                        this.logger.error("Pobieranie przerwane z powodu bezczynności!");
                        context.status(HttpStatus.REQUEST_TIMEOUT).result("Download timeout due to inactivity.");
                        return;
                    }
                }

}
