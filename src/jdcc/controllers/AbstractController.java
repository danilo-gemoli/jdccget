package jdcc.controllers;

import jdcc.dispatcher.Dispatcher;
import jdcc.events.commands.Command;
import jdcc.events.handler.EventHandlerAdapter;
import jdcc.events.messages.Message;
import jdcc.kernels.Kernel;

public abstract class AbstractController extends EventHandlerAdapter implements Controller {

    protected Dispatcher dispatcher;
    protected Kernel kernel;

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void sendCommand(Command command) {
        dispatcher.addCommand(command);
    }

    @Override
    public void sendMessage(Message message) {
        dispatcher.addMessage(message);
    }

    @Override
    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }
}
