package jdcc.events;

import jdcc.events.handler.EventHandler;

public interface Event {

    /***
     * Metodo di convenienza per il multiple dispatch dell'evento.
     *
     * @param handler l'handler che deve gestire la logica di processamento del singolo evento.
     */
    void handle(EventHandler handler);

    /***
     * L'id dell'evento.
     *
     * @return id univoco per ogni evento.
     */
    String getId();

}
