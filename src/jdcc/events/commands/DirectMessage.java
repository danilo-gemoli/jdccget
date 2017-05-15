package jdcc.events.commands;

import jdcc.events.handler.EventHandler;

public class DirectMessage extends Command {

    // Un utente o un canale.
    public String target = "";
    public String message = "";

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
