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

import java.io.IOException;

public class ThreadedDownload implements DownloadKernel, Runnable, FileTransferCallback {
    private DownloadController controller;
    private DownloadStatistics statistics;
    private String downloadPath;
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
    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    @Override
    public void setOutputWriter(DownloadOutputWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

    @Override
    public void onNewFileTransferConnection(FileTransferConnection fileTransferConnection) {
        transferConnection = fileTransferConnection;
        String filename = fileTransferConnection.getFilename();
        String fullDownloadPath = downloadPath + filename;
        Thread downloadThread = new Thread(this);
        setDownloadFullpath(fullDownloadPath);
        fileTransferConnection.setCallback(this);
        sendDownloadIsStartingMessage();
        outputWriter.setFileName(filename);
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

    protected void setDownloadFullpath(String fullpath) {
        transferConnection.setDestinationFilepath(fullpath);
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
