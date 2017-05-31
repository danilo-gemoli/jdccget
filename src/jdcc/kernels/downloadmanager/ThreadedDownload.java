package jdcc.kernels.downloadmanager;

import jdcc.events.messages.DownloadIsStarting;
import jdcc.kernels.bot.FileTransferCallback;
import jdcc.kernels.bot.FileTransferConnection;
import jdcc.controllers.download.DownloadController;
import jdcc.kernels.downloadmanager.output.DownloadOutputWriter;
import jdcc.events.messages.DownloadCompleted;
import jdcc.events.messages.DownloadError;
import jdcc.kernels.downloadmanager.statistics.DownloadStatistics;
import jdcc.kernels.downloadmanager.statistics.Size;
import jdcc.logger.JdccLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ThreadedDownload implements DownloadKernel, Runnable, FileTransferCallback {
    private DownloadController controller;
    private DownloadStatistics statistics;
    private Path downloadPath;
    private FileTransferConnection transferConnection;
    private boolean resumeDownload;
    private DownloadOutputWriter outputWriter;

    public ThreadedDownload() { }

    @Override
    public void run() {
        try {
            setStatisticsStartTime(System.currentTimeMillis());
            transferConnection.acceptDownload(resumeDownload);
            setStatisticsFileStartLength();
            transferConnection.startDownload();
        } catch (Exception e) {
            JdccLogger.logger.error("ThreadedDownload: acceptDownload error", e);
            controller.sendEvent(new DownloadError(e));
            return;
        }
        DownloadCompleted downloadCompleted = new DownloadCompleted();
        controller.sendEvent(downloadCompleted);
    }

    @Override
    public void setDownloadStatistics(DownloadStatistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public void setController(DownloadController controller) {
        this.controller = controller;
    }

    @Override
    public void setDownloadPath(Path downloadPath) {
        this.downloadPath = downloadPath;
    }

    @Override
    public void setOutputWriter(DownloadOutputWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

    @Override
    public void onNewFileTransferConnection(FileTransferConnection fileTransferConnection) {
        transferConnection = fileTransferConnection;
        fileTransferConnection.setCallback(this);

        String filename = fileTransferConnection.getFilename();
        makeDownloadPathIfNotExists();
        Path fullDownloadPath = getFullDownloadPath(filename);
        setDownloadFullpath(fullDownloadPath);
        outputWriter.setFileName(filename);

        Thread downloadThread = new Thread(this);
        sendDownloadIsStartingMessage();
        downloadThread.start();
    }

    @Override
    public void setResumeDownload(boolean resume) {
        resumeDownload = resume;
    }

    @Override
    public void onBytesReceived(int bytesReceived, long fileSize) {
        dumpDownloadStatus(bytesReceived, fileSize);
    }

    @Override
    public void dispose() { }

    private void dumpDownloadStatus(int bytesReceived, long fileSize) {
        if (outputWriter != null && statistics != null) {
            statistics.setTotalBytesToDownload(fileSize);
            statistics.setCurrentDownloadedBytes(bytesReceived);
            Size downloadSpeed = statistics.getCurrentDownloadSpeed();
            int downloadPercentage = statistics.getDownloadCompletingPercentage();
            long remainingTime = statistics.getRemainingTimeInSeconds();
            Size downloadedSize = statistics.getDownloadedSize();
            Size totFileSize = statistics.getTotalSizeToDownload();
            outputWriter.setDownloadPercentage(downloadPercentage);
            outputWriter.setDownloadSpeed(downloadSpeed.size);
            outputWriter.setDownloadSpeedSizeMeasurementUnit(downloadSpeed.unit.toString());
            outputWriter.setRemainingMillis(remainingTime * 1000);
            outputWriter.setDownloadedSize(downloadedSize);
            outputWriter.setFileSize(totFileSize);
            try {
                outputWriter.write();
            } catch (IOException e) {
                JdccLogger.logger.error("ThreadedDownload: outputWriter i/o error", e);
            }
        }
    }

    protected void setDownloadFullpath(Path fullpath) {
        transferConnection.setDestinationFilepath(fullpath);
    }

    private Path getFullDownloadPath(String filename) {
        return Paths.get(downloadPath.toString(), filename);
    }

    private void makeDownloadPathIfNotExists() {
        if (Files.notExists(downloadPath, LinkOption.NOFOLLOW_LINKS)) {
            JdccLogger.logger.info(
                    "ThreadedDownload: download folder \"{}\" does not exists: creating."
                    , downloadPath);
            File f = downloadPath.toFile();
            f.mkdirs();
        }
    }

    private void setStatisticsStartTime(long time) {
        if (statistics != null) {
            statistics.setDownloadStartTime(time);
        }
    }

    private void setStatisticsFileStartLength() {
        if (statistics != null) {
            if (transferConnection.hasBeenResumed()) {
                statistics.setDownloadStartPosition(transferConnection
                        .getPartialFileStartLength());
            }
        }
    }

    private void sendDownloadIsStartingMessage() {
        DownloadIsStarting startingMsg = new DownloadIsStarting();
        controller.sendEvent(startingMsg);
    }
}
