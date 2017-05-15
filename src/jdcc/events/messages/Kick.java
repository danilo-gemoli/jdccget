package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class Kick extends Message {
    public String reason;

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
