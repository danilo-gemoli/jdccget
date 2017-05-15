package jdcc.kernels.bot;

import jdcc.logger.JdccLogger;
import org.pircbotx.dcc.FileTransfer;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PircFileTransfer implements FileTransferConnection {
    private IncomingFileTransferEvent fileTransferEvent;
    private FileTransfer fileTransfer;
    private boolean resumeDownload;
    private String destinationPath;
    private FileTransferCallback fileTransferCallback;
    private boolean hasBeenResumed;
    private long partialFileStartLenght;

    public PircFileTransfer(IncomingFileTransferEvent fileTransferEvent) {
        this.fileTransferEvent = fileTransferEvent;
    }

    @Override
    public String getFilename() {
        return fileTransferEvent.getSafeFilename();
    }

    @Override
    public void setDestinationFilepath(String filepath){
        destinationPath = filepath;
    }

    @Override
    public void acceptDownload(boolean resumeDownload) throws IOException, InterruptedException {
        this.resumeDownload = resumeDownload;
        if (resumeDownload) {
            try {
                fileTransfer = doAcceptResume();
                hasBeenResumed = true;
            } catch (FileNotFoundException e) {
                JdccLogger.logger.warn(
                        "PircFileTransfer: start download resume file {} not found, create new file."
                        , destinationPath);
                fileTransfer = doAccept();
            }
        } else {
            fileTransfer = doAccept();
        }
    }

    @Override
    public void startDownload() throws IOException {
        ((MyReceiveFileTransfer) fileTransfer).setTransferCallback(fileTransferCallback);
        fileTransfer.transfer();
    }

    @Override
    public boolean hasBeenResumed() {
        return hasBeenResumed;
    }

    @Override
    public long getPartialFileStartLength() {
        return partialFileStartLenght;
    }

    @Override
    public boolean isFinished() {
        // TODO: è thread-safe?
        return fileTransfer.isFinished();
    }

    @Override
    public void setCallback(FileTransferCallback callback) {
        fileTransferCallback = callback;
    }

    // METODI PRIVATI
    private FileTransfer doAcceptResume() throws IOException, InterruptedException {
        File partialFile = new File(destinationPath);
        if (!partialFile.exists()) {
            throw new FileNotFoundException();
        }
        long fileLength = partialFile.length();
        partialFileStartLenght = fileLength;
        return fileTransferEvent.acceptResume(partialFile, fileLength);
    }

    private FileTransfer doAccept() throws IOException {
        return fileTransferEvent.accept(new File(destinationPath));
    }
}
