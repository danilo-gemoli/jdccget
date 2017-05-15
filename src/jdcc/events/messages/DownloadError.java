package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class DownloadError extends ErrorMessage {

    public DownloadError() { }

    public DownloadError(Exception ex) {
        super(ex);
    }

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
