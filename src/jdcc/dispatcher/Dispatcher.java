package jdcc.dispatcher;

import jdcc.events.commands.Command;
import jdcc.events.messages.Message;

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
     * Aggiunge un evento messaggio al dispatcher.
     *
     * @param message
     */
    void addMessage(Message message);

    /***
     * Aggiunge un evento comando al dispatcher.
     *
     * @param command
     */
    void addCommand(Command command);

    /***
     * Registra un observer che viene notificato ogni volta che viene
     * inviato un comando.
     *
     * @param observer
     */
    void registerCommandsObserver(DispatcherObserver observer);

    /***
     * Annulla la registrazione di un observer di comandi.
     *
     * @param observer
     */
    void unregisterCommandsObserver(DispatcherObserver observer);

    /***
     * Registra un observer che viene notificato ogni volta che viene
     * inviato un messaggio.
     *
     * @param observer
     */
    void registerMessagesObserver(DispatcherObserver observer);

    /***
     * Annulla la registrazione di un observer di messaggi.
     *
     * @param observer
     */
    void unregisterMessagesObserver(DispatcherObserver observer);

}
