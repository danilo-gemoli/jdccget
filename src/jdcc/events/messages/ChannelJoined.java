package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

/**
 * Created by danilo on 4/24/17.
 */
public class ChannelJoined extends Message {

    public String channelName = "";
    public String userNickname = "";

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
