package jdcc.events.commands;

import jdcc.events.handler.EventHandler;

public class JoinChannel extends Command {

    public String name = "";
    public String password = null;

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
