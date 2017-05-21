package jdcc.kernels.downloadmanager.output;

import jdcc.kernels.downloadmanager.statistics.Size;

import java.util.concurrent.TimeUnit;

abstract class AbstractDownloadOutputWriter implements DownloadOutputWriter {
    protected String networkName;
    protected String channelName;
    protected String botName;
    protected String fileName = "";
    protected int packNum;
    protected int downloadPercentage;
    protected float downloadSpeed;
    protected String sizeMeasurementUnit;
    protected long remainingTime;
    protected Size downloadedSize;
    protected Size fileSize;
    /***
     * L'ultima volta che è stato stampato lo stato del download.
     */
    protected long downloadInfoDumpLastCallTime = 0;
    /***
     * La frequenza di aggiornamento della stampa.
     */
    protected long downloadInfoDumpRefreshRate = 1000;

    protected long remainingDays;
    protected long remainingHours;
    protected long remainingMinutes;
    protected long remainingSeconds;

    @Override
    public void setDownloadedSize(Size remainingSize) {
        this.downloadedSize = remainingSize;
    }

    @Override
    public void setFileSize(Size fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public void setRemainingMillis(long millis) {
        this.remainingTime = millis;
    }

    @Override
    public void setDownloadSpeedSizeMeasurementUnit(String sizeMeasurementUnit) {
        this.sizeMeasurementUnit = sizeMeasurementUnit;
    }

    @Override
    public void setDownloadPercentage(int downloadPercentage) {
        this.downloadPercentage = downloadPercentage;
    }

    @Override
    public void setDownloadSpeed(float downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
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

    protected void calculateRemainingTime() {
        long daysInMillis;
        long hoursInMillis;
        remainingDays = TimeUnit.MILLISECONDS.toDays(remainingTime);
        daysInMillis = TimeUnit.DAYS.toMillis(remainingDays);
        remainingHours = TimeUnit.MILLISECONDS.toHours(remainingTime
                - daysInMillis);
        hoursInMillis = TimeUnit.HOURS.toMillis(remainingHours);
        remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime
                - daysInMillis - hoursInMillis);
        remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime
                - daysInMillis - hoursInMillis - TimeUnit.MINUTES.toMillis(remainingMinutes));
    }

    protected String getRemainingTime() {
        if (remainingDays > 0) {
            return String.format("%2d days %02d hours %02d mins %02d secs", remainingDays,
                    remainingHours, remainingMinutes, remainingSeconds);
        } else if (remainingHours > 0) {
            String.format("%2d hours %02d mins %02d secs", remainingHours, remainingMinutes
                    , remainingSeconds);
        }
        return String.format("%2d mins %02d secs", remainingMinutes, remainingSeconds);
    }

    protected boolean pastAtLestOneSecFromLastCall(long now) {
        return ((now - downloadInfoDumpLastCallTime) / downloadInfoDumpRefreshRate) > 1;
    }
}
