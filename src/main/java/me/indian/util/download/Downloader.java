package me.indian.util.download;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeoutException;
import me.indian.util.DateUtil;
import me.indian.util.MathUtil;
import org.jetbrains.annotations.Nullable;

public final class Downloader {

    private Downloader() {

    }

    public static void downloadFile(final InputStream inputStream, final File outputFile, final long fileSize, final DownloadBuffer downloadBuffer,
                                    final int timeOutSeconds, @Nullable final DownloadListener downloadListener) throws IOException, TimeoutException {

        final long inactivityTimeoutMillis = DateUtil.secondToMillis(timeOutSeconds);
        long lastActivityTime;

        try (final FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            final int definedBuffer = defineBuffer(downloadBuffer, fileSize);
            final byte[] buffer = new byte[definedBuffer];

            if (downloadListener != null) downloadListener.onStart(downloadBuffer, definedBuffer, outputFile);

            int bytesRead;
            long totalBytesRead = 0;

            long lastTime = System.currentTimeMillis();
            long lastBytesRead = 0;
            int lastProgress = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                lastActivityTime = System.currentTimeMillis();
                fileOutputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                final long currentTime = System.currentTimeMillis();
                final double elapsedTime = (currentTime - lastTime) / 1000.0;
                if (elapsedTime >= 1.0) {
                    final long bytesSinceLastTime = totalBytesRead - lastBytesRead;
                    final double speedBytesPerSecond = (double) bytesSinceLastTime / elapsedTime;
                    final double speedMBps = speedBytesPerSecond / (1024.0 * 1024.0);
                    lastTime = currentTime;
                    lastBytesRead = totalBytesRead;

                    final int progress = Math.round((float) totalBytesRead / (float) fileSize * 100.0f);

                    final double formatedSpeed = MathUtil.format(speedMBps, 3);
                    final long remainingTimeSeconds = (long) (MathUtil.bytesToMB(fileSize) / formatedSpeed);
                    final String remainingTimeString = DateUtil.formatTimeDynamic(remainingTimeSeconds * 1000, true);

                    if (downloadListener != null)
                        downloadListener.onSecond(progress, formatedSpeed, remainingTimeString);

                    if (progress != lastProgress) {
                        lastProgress = progress;

                        if (downloadListener != null)
                            downloadListener.onProgress(progress, formatedSpeed, remainingTimeString);
                    }
                }

                if (timeOutSeconds != -1 && System.currentTimeMillis() - lastActivityTime > inactivityTimeoutMillis) {
                    if (downloadListener != null) downloadListener.onTimeout(timeOutSeconds);
                    throw new TimeoutException();
                }
            }

            inputStream.close();

            if (downloadListener != null) downloadListener.onEnd(outputFile);
        }
    }

    private static int defineBuffer(final DownloadBuffer downloadBuffer, final long fileSize) {
        if (downloadBuffer == DownloadBuffer.DYNAMIC) return calculateOptimalBufferSize(fileSize);

        return downloadBuffer.getBuffer();
    }

    private static int calculateOptimalBufferSize(final long fileSize) {
        final int maxBufferSize = DownloadBuffer.SIXTEEN_MB.getBuffer();
        final long calculatedBufferSize = (long) (fileSize *  0.1);
        final long bufferPerRequest = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemorySize() / 5;
        final long bufferSize = Math.min(calculatedBufferSize, bufferPerRequest);

        if (bufferSize > maxBufferSize) {
            return maxBufferSize;
        } else if (bufferSize < DownloadBuffer.ONE_MB.getBuffer()) {
            return DownloadBuffer.ONE_MB.getBuffer();
        }

        return (int) bufferSize;
    }
}