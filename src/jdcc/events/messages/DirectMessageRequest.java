package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class DirectMessageRequest extends Message {

    public String target;
    public String message;

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
