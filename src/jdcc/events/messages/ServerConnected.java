package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class ServerConnected extends  Message {

    public String serverName = "";

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
