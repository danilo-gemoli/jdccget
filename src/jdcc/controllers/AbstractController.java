package jdcc.controllers;

import jdcc.dispatcher.Dispatcher;
import jdcc.events.Event;
import jdcc.events.handler.EventHandlerAdapter;
import jdcc.kernels.Kernel;

public abstract class AbstractController extends EventHandlerAdapter implements Controller {
    protected Dispatcher dispatcher;
    protected Kernel kernel;

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void sendEvent(Event event) {
        dispatcher.addEvent(event);
    }

    @Override
    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }
}
