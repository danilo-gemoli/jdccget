package jdcc.events.messages;

import jdcc.events.handler.EventHandler;

public class FatalError extends ErrorMessage {

    public FatalError() { }

    public FatalError(Exception ex) {
        super(ex);
    }

    @Override
    public void handle(EventHandler handler) {
        handler.handle(this);
    }
}
