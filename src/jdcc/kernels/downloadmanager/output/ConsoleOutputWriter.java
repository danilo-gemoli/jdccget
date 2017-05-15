package jdcc.kernels.downloadmanager.output;

import java.io.IOException;

public class ConsoleOutputWriter extends AbstractDownloadOutputWriter {
    @Override
    public void write() throws IOException {
        long now = System.currentTimeMillis();
        if (pastAtLestOneSecFromLastCall(now)) {
            int downloadPercentage = getDownloadCompletingPercentage(fileSize);
            String downloadSpeed = getCurrentDownloadSpeed();
            System.out.format("%s: %d%% - %s\n", fileName, downloadPercentage, downloadSpeed);
            downloadInfoDumpLastCallTime = now;
        }
    }
}
