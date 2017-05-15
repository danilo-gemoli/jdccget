package jdcc.kernels.downloadmanager;

import jdcc.events.messages.DownloadIsStarting;
import jdcc.kernels.bot.FileTransferCallback;
import jdcc.kernels.bot.FileTransferConnection;
import jdcc.controllers.download.DownloadController;
import jdcc.kernels.downloadmanager.output.DownloadOutputWriter;
import jdcc.events.messages.DownloadCompleted;
import jdcc.events.messages.DownloadError;
import jdcc.logger.JdccLogger;

import java.io.IOException;

public class ThreadedDownload implements DownloadKernel, Runnable, FileTransferCallback {
    private DownloadController controller;
    private String downloadPath;
    private FileTransferConnection transferConnection;
    private boolean resumeDownload;
    private DownloadOutputWriter outputWriter;

    public ThreadedDownload() { }

    @Override
    public void run() {
        try {
            setOutputWriterStartTime(System.currentTimeMillis());
            transferConnection.acceptDownload(resumeDownload);
            setOutputWriterFileStartLength();
            transferConnection.startDownload();
        } catch (Exception e) {
            JdccLogger.logger.error("ThreadedDownload: acceptDownload error", e);
            controller.sendMessage(new DownloadError(e));
            return;
        }
        DownloadCompleted downloadCompleted = new DownloadCompleted();
        controller.sendMessage(downloadCompleted);
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
        if (outputWriter != null) {
            outputWriter.setDownloadStatus(bytesReceived, fileSize);
            try {
                outputWriter.write();
            } catch (IOException e) {
                JdccLogger.logger.error("ThreadedDownload: outputWriter i/o error", e);
            }
        }
    }

    @Override
    public void dispose() {

    }

    protected void setDownloadFullpath(String fullpath) {
        transferConnection.setDestinationFilepath(fullpath);
    }

    private void setOutputWriterStartTime(long time) {
        if (outputWriter != null) {
            outputWriter.setStartTime(time);
        }
    }

    private void setOutputWriterFileStartLength() {
        if (outputWriter != null) {
            if (transferConnection.hasBeenResumed()) {
                outputWriter.setPartialFileStartLenght(transferConnection.getPartialFileStartLength());
            }
        }
    }

    private void sendDownloadIsStartingMessage() {
        DownloadIsStarting startingMsg = new DownloadIsStarting();
        controller.sendMessage(startingMsg);
    }
}
