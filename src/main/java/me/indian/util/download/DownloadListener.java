package me.indian.util.download;

import java.io.File;

public interface DownloadListener {

    void onStart(int definedBuffer, File outputFile);

    void onSecond(int progress, double formatedSpeed, String remainingTimeString);

    void onProgress(int progress, double formatedSpeed, String remainingTimeString);

    void onTimeout();

    void onEnd(File outputFile);

}