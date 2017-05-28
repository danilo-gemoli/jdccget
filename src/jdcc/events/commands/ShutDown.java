package jdcc.events.commands;

import jdcc.events.handler.EventHandler;

public class ShutDown extends Command {
    public int exitStatus;

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
