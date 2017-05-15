package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class XdccDownloadQueueFull extends XdccMessage {
    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
