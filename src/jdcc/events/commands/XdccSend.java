package jdcc.events.commands;

import jdcc.events.handler.EventHandler;

public class XdccSend extends XdccCommand {
    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
