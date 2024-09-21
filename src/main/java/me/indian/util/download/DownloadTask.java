package me.indian.util.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;
import me.indian.util.BufferUtil;
import me.indian.util.DateUtil;
import me.indian.util.MathUtil;
import org.jetbrains.annotations.Nullable;

public class DownloadTask {

    private final InputStream inputStream;
    private final File outputFile;
    private final long fileSize;
    private final BufferUtil.DownloadBuffer downloadBuffer;
    private final int timeOutSeconds;
    private final DownloadListener downloadListener;
    private boolean canDownload, downloading, finished;

    public DownloadTask(final InputStream inputStream, final File outputFile, final long fileSize, final BufferUtil.DownloadBuffer downloadBuffer, final int timeOutSeconds, @Nullable final DownloadListener downloadListener) throws IOException, TimeoutException {
        this.inputStream = inputStream;
        this.outputFile = outputFile;
        this.fileSize = fileSize;
        this.downloadBuffer = downloadBuffer;
        this.timeOutSeconds = timeOutSeconds;
        this.downloadListener = downloadListener;
        this.canDownload = true;
        this.downloading = false;
    }

    public DownloadTask(final InputStream inputStream, final File outputFile, final long fileSize, final BufferUtil.DownloadBuffer downloadBuffer, final int timeOutSeconds) throws IOException, TimeoutException {
        this.inputStream = inputStream;
        this.outputFile = outputFile;
        this.fileSize = fileSize;
        this.downloadBuffer = downloadBuffer;
        this.timeOutSeconds = timeOutSeconds;
        this.downloadListener = null;
        this.canDownload = true;
        this.downloading = false;
    }

    public void downloadFile() throws IOException, TimeoutException {
        final long inactivityTimeoutMillis = DateUtil.secondToMillis(this.timeOutSeconds);
        long lastActivityTime;

        try (final FileOutputStream fileOutputStream = new FileOutputStream(this.outputFile)) {
            this.downloading = true;
            final int definedBuffer = BufferUtil.defineBuffer(this.downloadBuffer, this.fileSize);
            final byte[] buffer = new byte[definedBuffer];

            if (this.downloadListener != null)
                this.downloadListener.onStart(this.downloadBuffer, definedBuffer, this.outputFile);

            int bytesRead;
            long totalBytesRead = 0;

            long lastTime = System.currentTimeMillis();
            long lastBytesRead = 0;
            int lastProgress = -1;

            while ((bytesRead = this.inputStream.read(buffer)) != -1) {
                if (!this.canDownload) break;

                lastActivityTime = System.currentTimeMillis();
                fileOutputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;


                final long currentTime = System.currentTimeMillis();
                final double elapsedTime = (currentTime - lastTime) / 1000.0;
                if (elapsedTime >= 1.0) {
                    final long bytesSinceLastTime = totalBytesRead - lastBytesRead;
                    final double speedBytesPerSecond = (double) bytesSinceLastTime / elapsedTime;
                    final double speedMBps = speedBytesPerSecond / BufferUtil.DownloadBuffer.ONE_MB.getBuffer();
                    lastTime = currentTime;
                    lastBytesRead = totalBytesRead;

                    final int progress = Math.round((float) totalBytesRead / (float) this.fileSize * 100.0f);

                    final double formatedSpeed = MathUtil.format(speedMBps, 3);
                    final long remainingTimeSeconds = (long) (MathUtil.bytesToMB(this.fileSize) / formatedSpeed);
                    final String remainingTimeString = DateUtil.formatTimeDynamic(remainingTimeSeconds * 1000, true);

                    if (this.downloadListener != null)
                        this.downloadListener.onSecond(progress, formatedSpeed, remainingTimeString);

                    if (progress != lastProgress) {
                        lastProgress = progress;

                        if (this.downloadListener != null)
                            this.downloadListener.onProgress(progress, formatedSpeed, remainingTimeString);
                    }
                }

                if (this.timeOutSeconds != -1 && System.currentTimeMillis() - lastActivityTime > inactivityTimeoutMillis) {
                    if (this.downloadListener != null) this.downloadListener.onTimeout(this.timeOutSeconds);
                    throw new TimeoutException();
                }
            }

            this.inputStream.close();

            if (this.downloadListener != null) this.downloadListener.onEnd(this.outputFile);
        } finally {
            this.downloading = false;
            this.finished = true;
            try {
                this.inputStream.close();
            } catch (final IOException ignore) {

            }
        }
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public File getOutputFile() {
        return this.outputFile;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public BufferUtil.DownloadBuffer getDownloadBuffer() {
        return this.downloadBuffer;
    }

    public int getTimeOutSeconds() {
        return this.timeOutSeconds;
    }

    public DownloadListener getDownloadListener() {
        return this.downloadListener;
    }

    public boolean isCanDownload() {
        return this.canDownload;
    }

    public boolean isDownloading() {
        return this.downloading;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void stopDownload() {
        if (!this.downloading) throw new IllegalStateException("Brak aktywnego pobierania.");
        this.canDownload = false;
    }

    @Override
    public String toString() {
        return "DownloadTask (" +
                "outputFile=" + this.outputFile.getPath() +
                ", fileSize=" + this.fileSize +
                ", downloadBuffer=" + this.downloadBuffer +
                ", timeOutSeconds=" + this.timeOutSeconds +
                ", canDownload=" + this.canDownload +
                ", downloading=" + this.downloading +
                ", finished=" + this.finished +
                ')';
    }
}