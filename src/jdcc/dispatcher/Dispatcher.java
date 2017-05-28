package jdcc.dispatcher;

import jdcc.events.Event;

public interface Dispatcher {
    /***
     * Avvia il dispatcher.
     */
    void start();

    /***
     * Ferma il dispatcher.
     */
    void stop();

    /***
     * Aggiunge un evento al dispatcher.
     * @param e
     */
    void addEvent(Event e);

    /***
     * Registra un observer che viene notificato ogni volta che viene inviato un evento.
     *
     * @param observer l'observer da registrare.
     */
    void registerObserver(DispatcherObserver observer);

    /***
     * Annulla la registrazione di un observer.
     *
     * @param observer l'observer da de-registrare.
     */
    void unregisterObserver(DispatcherObserver observer);
}
