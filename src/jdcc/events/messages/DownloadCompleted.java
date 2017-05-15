package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class DownloadCompleted extends Message {
    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
