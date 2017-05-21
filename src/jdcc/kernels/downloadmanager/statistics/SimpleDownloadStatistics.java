package jdcc.kernels.downloadmanager.statistics;

import java.util.concurrent.TimeUnit;

public class SimpleDownloadStatistics implements DownloadStatistics {
    private Size bytesReceived;
    private Size fileSize;
    private long downloadStartTime;
    private long fileStartLength = 0;
    private Size totBytesTransferred;

    public SimpleDownloadStatistics() {
        bytesReceived = new Size();
        fileSize = new Size();
        totBytesTransferred = new Size();
    }

    @Override
    public void setDownloadStartTime(long startTime) {
        this.downloadStartTime = startTime;
    }

    @Override
    public void setDownloadStartPosition(long bytes) {
        this.fileStartLength = bytes;
    }

    @Override
    public void setTotalBytesToDownload(long bytes) {
        fileSize.size = bytes;
    }

    @Override
    public void setCurrentDownloadedBytes(long bytes) {
        bytesReceived.size = bytes;
        totBytesTransferred.size += bytesReceived.size;
    }

    @Override
    public int getDownloadCompletingPercentage() {
        int percentage = (int)(((totBytesTransferred.size + fileStartLength)
                / fileSize.size) * 100);
        return percentage;
    }

    @Override
    public long getRemainingTimeInSeconds() {
        float remainingBytes = getRemainingBytes();
        Size currentSpeed = getCurrentDownloadSpeed();
        long remainingTime = (long) (remainingBytes / currentSpeed.toRawSize(SizeUnitMeasurement.BYTES));
        return remainingTime;
    }

    @Override
    public Size getDownloadedSize() {
        return roundToNearestSizeUnit(totBytesTransferred.size + fileStartLength);
    }

    @Override
    public Size getTotalSizeToDownload() {
        return roundToNearestSizeUnit(fileSize.size);
    }

    @Override
    public Size getCurrentDownloadSpeed() {
        long milliDiff = System.currentTimeMillis() - downloadStartTime;
        long secondsDiff = TimeUnit.SECONDS.convert(milliDiff, TimeUnit.MILLISECONDS);
        float unitsPerSecs = 0.0f;
        SizeUnitMeasurement currentSizeUnit = SizeUnitMeasurement.BYTES;
        for (SizeUnitMeasurement unit : SizeUnitMeasurement.values()) {
            currentSizeUnit = unit;
            unitsPerSecs = totBytesTransferred.toRawSize(unit) / (float) secondsDiff;
            if (unitsPerSecs < 1024.0)
                break;
        }
        return new Size(unitsPerSecs, currentSizeUnit);
    }

    private float getRemainingBytes() {
        return fileSize.size - (fileStartLength + totBytesTransferred.size);
    }

    private Size roundToNearestSizeUnit(float bytes) {
        SizeUnitMeasurement currentSizeUnit = SizeUnitMeasurement.BYTES;
        Size size = new Size(bytes, SizeUnitMeasurement.BYTES);
        float rawSize = 0;
        for (SizeUnitMeasurement unit : SizeUnitMeasurement.values()) {
            currentSizeUnit = unit;
            rawSize = size.toRawSize(unit);
            if (rawSize < 1024.0)
                break;
        }
        return new Size(rawSize, currentSizeUnit);
    }
}
