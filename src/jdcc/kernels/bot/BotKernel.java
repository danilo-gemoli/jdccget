package jdcc.kernels.bot;

import jdcc.controllers.bot.BotController;
import jdcc.ircparser.IrcMessageParser;
import jdcc.kernels.Kernel;


public interface BotKernel extends Kernel {
    /***
     * Imposta il controller del kernel.
     *
     * @param controller
     */
    void setController(BotController controller);

    /***
     * Imposta il parser dei messaggi irc.
     *
     * @param messageParser
     */
    void setMessagePaser(IrcMessageParser messageParser);

    /***
     * Si connette ad un server IRC.
     *
     * @param serverName nome del server.
     * @param serverPort porta del server, di default è 6667.
     * @param nickname il nickname dell'utente.
     * @param serverPassword password del server, di default è "".
     */
    void connectToServer(String serverName, int serverPort, String nickname, String serverPassword);

    /***
     * Se connesso, chiude la connessione con il server.
     */
    void disconnect();

    /***
     * Si unisce ad un canale.
     *
     * @param name il nome del canale.
     * @param password la password del canale, di default è assente.
     */
    void joinChannel(String name, String password);

    /***
     * Invia un messaggio ad un canale o utente.
     *
     * @param target destinatario
     * @param message
     */
    void sendMessage(String target, String message);

    /***
     * Invia un messaggio di richiesta download all'utente/bot.
     *
     * @param user l'utente/bot a cui richiedere il download.
     * @param packNumber il pack da scaricare.
     */
    void sendXdccSendMessage(String user, int packNumber);

    /***
     * Annulla la richiesta di download.
     *
     * @param user l'utente/bot a cui richiedere di cancellare il download.
     * @param packNumber il pack per cui annullare il download.
     */
    void sendXdccRemoveMessage(String user, int packNumber);
}
