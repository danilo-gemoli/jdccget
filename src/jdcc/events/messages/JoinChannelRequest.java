package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class JoinChannelRequest extends Message {

    public String channelName = "";
    public String channelPassword = null;

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
