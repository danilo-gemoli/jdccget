package jdcc.events.commands;

import jdcc.events.handler.EventHandler;

public class Connect extends Command {

    public String serverName = "";
    public int port = 6667;
    public String serverPassword = "";
    public String nickname = "";
    public String realname = "";
    public String loginname = "";

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
