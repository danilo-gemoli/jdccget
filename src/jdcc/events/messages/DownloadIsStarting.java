package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class DownloadIsStarting extends Message {
    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
