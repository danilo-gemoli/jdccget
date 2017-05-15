package jdcc.events.commands;

import jdcc.events.handler.EventHandler;

public class Disconnect extends Command {
    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
