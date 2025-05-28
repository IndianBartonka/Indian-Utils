package pl.indianbartonka.util.download;

import java.io.File;

public interface DownloadListener {

    void onStart(int definedBuffer, long fileSize, File outputFile);

    void onSecond(int progress, double formatedSpeed, String remainingTimeString);

    void onProgress(int progress, double formatedSpeed, String remainingTimeString);

    void onTimeout(int timeOutSeconds);

    void onEnd(File outputFile);

    void onDownloadStop();
}