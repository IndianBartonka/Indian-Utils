package pl.indianbartonka.util.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;
import org.jetbrains.annotations.Nullable;
import pl.indianbartonka.util.BufferUtil;
import pl.indianbartonka.util.DateUtil;
import pl.indianbartonka.util.MathUtil;

public class DownloadTask {

    private final InputStream inputStream;
    private final File outputFile;
    private final long fileSize;
    private final int timeOutSeconds;
    private final DownloadListener downloadListener;
    private boolean stopped;
    private boolean downloading;
    private boolean finished;

    public DownloadTask(final InputStream inputStream, final File outputFile, final long fileSize, final int timeOutSeconds, @Nullable final DownloadListener downloadListener) {
        this.inputStream = inputStream;
        this.outputFile = outputFile;
        this.fileSize = fileSize;
        this.timeOutSeconds = timeOutSeconds;
        this.downloadListener = downloadListener;
        this.stopped = false;
        this.downloading = false;
    }

    public DownloadTask(final InputStream inputStream, final File outputFile, final long fileSize, final int timeOutSeconds) {
        this.inputStream = inputStream;
        this.outputFile = outputFile;
        this.fileSize = fileSize;
        this.timeOutSeconds = timeOutSeconds;
        this.downloadListener = null;
        this.stopped = false;
        this.downloading = false;
    }

    public void downloadFile() throws IOException, TimeoutException {
        final long inactivityTimeoutMillis = DateUtil.secondToMillis(this.timeOutSeconds);
        long lastActivityTime;

        try (final FileOutputStream fileOutputStream = new FileOutputStream(this.outputFile)) {
            this.downloading = true;

            final int definedBuffer = BufferUtil.calculateOptimalBufferSize(this.fileSize);
            final byte[] buffer = new byte[definedBuffer];

            if (this.downloadListener != null) {
                this.downloadListener.onStart(definedBuffer, this.outputFile);
            }

            int bytesRead;
            long totalBytesRead = 0;

            long lastTime = System.currentTimeMillis();
            long lastBytesRead = 0;
            int lastProgress = -1;

            while ((bytesRead = this.inputStream.read(buffer)) != -1) {
                if (this.stopped) {
                    if (this.downloadListener != null) this.downloadListener.onDownloadStop();
                    break;
                }

                lastActivityTime = System.currentTimeMillis();
                fileOutputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                final long currentTime = System.currentTimeMillis();
                final double elapsedTime = (currentTime - lastTime) / 1000.0;
                if (elapsedTime >= 1.0) {
                    final long bytesSinceLastTime = totalBytesRead - lastBytesRead;
                    final double speedBytesPerSecond = bytesSinceLastTime / elapsedTime;
                    final double speedMBps = speedBytesPerSecond / 1_048_576;
                    lastTime = currentTime;
                    lastBytesRead = totalBytesRead;

                    final int progress = Math.round((float) totalBytesRead / (float) this.fileSize * 100.0f);

                    final double formatedSpeed = MathUtil.format(speedMBps, 3);
                    final long remainingTimeSeconds = (long) (MathUtil.bytesToMB(this.fileSize) / formatedSpeed);
                    final String remainingTimeString = DateUtil.formatTimeDynamic(remainingTimeSeconds * 1000, true);

                    if (this.downloadListener != null) {
                        this.downloadListener.onSecond(progress, formatedSpeed, remainingTimeString);
                    }

                    if (progress != lastProgress) {
                        lastProgress = progress;

                        if (this.downloadListener != null) {
                            this.downloadListener.onProgress(progress, formatedSpeed, remainingTimeString);
                        }
                    }
                }

                if (this.timeOutSeconds != -1 && System.currentTimeMillis() - lastActivityTime > inactivityTimeoutMillis) {
                    if (this.downloadListener != null) this.downloadListener.onTimeout(this.timeOutSeconds);
                    throw new TimeoutException();
                }
            }

            if (this.downloadListener != null && !this.stopped) {
                this.downloadListener.onEnd(this.outputFile);
            }
        } finally {
            this.downloading = false;
            this.finished = true;
            this.inputStream.close();
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

    public int getTimeOutSeconds() {
        return this.timeOutSeconds;
    }

    public DownloadListener getDownloadListener() {
        return this.downloadListener;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public boolean isDownloading() {
        return this.downloading;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void stopDownload() {
        if (!this.downloading) throw new IllegalStateException("Brak aktywnego pobierania.");
        this.stopped = true;
    }

    @Override
    public String toString() {
        return "DownloadTask (" +
                "outputFile=" + this.outputFile.getPath() +
                ", fileSize=" + this.fileSize +
                ", timeOutSeconds=" + this.timeOutSeconds +
                ", stopped=" + this.stopped +
                ", downloading=" + this.downloading +
                ", finished=" + this.finished +
                ')';
    }
}