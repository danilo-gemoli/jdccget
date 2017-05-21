package jdcc.kernels.downloadmanager.output;

import java.io.IOException;

public class ConsoleOutputWriter extends AbstractDownloadOutputWriter {
    @Override
    public void write() throws IOException {
        long now = System.currentTimeMillis();
        if (pastAtLestOneSecFromLastCall(now)) {
            calculateRemainingTime();
            System.out.format("%s: %d%% - %.1f %s/s\n"
                    , fileName, downloadPercentage, downloadSpeed, sizeMeasurementUnit,
                    remainingTime);
            System.out.format("%.1f%s of %.1f%s downloaded\n"
                    , downloadedSize.size, downloadedSize.unit.toString()
                    , fileSize.size, fileSize.unit.toString());
            String remainingTime = getRemainingTime();
            System.out.format("%s left\n", remainingTime);
            downloadInfoDumpLastCallTime = now;
        }
    }
}
