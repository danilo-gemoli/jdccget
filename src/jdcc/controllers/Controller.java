package jdcc.controllers;

import jdcc.dispatcher.Dispatcher;
import jdcc.dispatcher.DispatcherObserver;
import jdcc.events.commands.Command;
import jdcc.events.messages.Message;
import jdcc.kernels.Kernel;

public interface Controller extends DispatcherObserver {

    /***
     * Imposta il dispatcher di questo controller.
     *
     * @param dispatcher
     */
    void setDispatcher(Dispatcher dispatcher);

    /***
     * Imposta il kernel di questo controller.
     *
     * @param kernel
     */
    void setKernel(Kernel kernel);

    /***
     * Invia un comando.
     *
     * @param command
     */
    void sendCommand(Command command);

    /***
     * Invia un messaggio.
     *
     * @param message
     */
    void sendMessage(Message message);

}
