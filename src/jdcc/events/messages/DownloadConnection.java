package jdcc.events.messages;

import jdcc.kernels.bot.FileTransferConnection;
import jdcc.events.handler.EventHandler;

public class DownloadConnection extends Message {

    public FileTransferConnection fileTransferConnection;

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
