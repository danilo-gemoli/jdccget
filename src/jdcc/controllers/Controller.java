package jdcc.controllers;

import jdcc.dispatcher.Dispatcher;
import jdcc.dispatcher.DispatcherObserver;
import jdcc.events.Event;
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
     * Invia un evento.
     *
     * @param event l'evento da inviare
     */
    void sendEvent(Event event);
}
