package me.indian.util.download;

import java.io.File;
import me.indian.util.BufferUtil;

public interface DownloadListener {

    void onStart(BufferUtil.DownloadBuffer downloadBuffer, int definedBuffer, File outputFile);

    void onSecond(int progress, double formatedSpeed, String remainingTimeString);

    void onProgress(int progress, double formatedSpeed, String remainingTimeString);

    void onTimeout(int timeOutSeconds);

    void onEnd(File outputFile);

    void onDownloadStop();
}