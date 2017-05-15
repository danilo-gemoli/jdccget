package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class ServerDisconnected extends Message {
    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
