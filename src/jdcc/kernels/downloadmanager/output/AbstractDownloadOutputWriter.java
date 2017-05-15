package jdcc.kernels.downloadmanager.output;

import java.util.concurrent.TimeUnit;

abstract class AbstractDownloadOutputWriter implements DownloadOutputWriter {
    protected String networkName;
    protected String channelName;
    protected String botName;
    protected String fileName = "";
    protected int packNum;
    protected long bytesReceived;
    protected long fileSize;
    protected long downloadStartTime;
    protected long fileStartLength = 0;

    /***
     * Totale bytes trasferiti fino ad ora.
     */
    protected long totBytesTransferred = 0;
    /***
     * L'ultima volta che è stato stampato lo stato del download.
     */
    protected long downloadInfoDumpLastCallTime = 0;
    /***
     * La frequenza di aggiornamento della stampa.
     */
    protected long downloadInfoDumpRefreshRate = 1000;

    @Override
    public void setStartTime(long startTime) {
        this.downloadStartTime = startTime;
    }

    @Override
    public void setPartialFileStartLenght(long startLenght) {
        this.fileStartLength = startLenght;
    }

    @Override
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    @Override
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public void setBotName(String botname) {
        this.botName = botname;
    }

    @Override
    public void setPackNum(int packNum) {
        this.packNum = packNum;
    }

    @Override
    public void setFileName(String filename) {
        this.fileName = filename;
    }

    @Override
    public void setDownloadStatus(long bytesReceived, long fileSize) {
        this.bytesReceived = bytesReceived;
        this.fileSize = fileSize;

        totBytesTransferred += bytesReceived;
    }

    protected boolean pastAtLestOneSecFromLastCall(long now) {
        return ((now - downloadInfoDumpLastCallTime) / downloadInfoDumpRefreshRate) > 1;
    }

    protected int getDownloadCompletingPercentage(long fileSize) {
        int percentage = (int)(((float) (totBytesTransferred + fileStartLength)/ (float) fileSize)
                * 100);
        return percentage;
    }

    protected String getCurrentDownloadSpeed() {
        long milliDiff = System.currentTimeMillis() - downloadStartTime;
        long secondsDiff = TimeUnit.SECONDS.convert(milliDiff, TimeUnit.MILLISECONDS);
        float kbPerSecs = ((float) totBytesTransferred / 1024) / (float) secondsDiff;
        String result = String.format("%.1f KB/s", kbPerSecs);
        return result;
    }
}
