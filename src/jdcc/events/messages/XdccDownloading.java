package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class XdccDownloading extends XdccMessage {
    public boolean resumeSupported;

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
