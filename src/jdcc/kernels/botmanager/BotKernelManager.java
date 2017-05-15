package jdcc.kernels.botmanager;

import jdcc.controllers.botmanager.ManagerController;
import jdcc.exceptions.NoChannelJoinedException;
import jdcc.exceptions.NoServerConnectionException;
import jdcc.kernels.Kernel;

// TODO: rendere più generica l'interfaccia (connessione a più network)
public interface BotKernelManager extends Kernel {
    /***
     * Imposta il controller del kernel.
     *
     * @param controller
     */
    void setController(ManagerController controller);

    /***
     * Chiamato quando è stata stabilita la connessione al server irc.
     *
     * @param serverName
     */
    void onServerConnected(String serverName);

    /***
     * Richiesta di una nuova connessione a server.
     *
     * @param serverName
     * @param serverPassword
     * @param serverPort
     * @param nickname
     */
    void onServerConnectRequest(String serverName, String serverPassword, int serverPort, String
            nickname);

    /***
     * Richiede di unirsi ad un canale.
     *
     * @param channelName
     * @param channelPassword
     * @throws NoServerConnectionException se non è stata mai inoltrata la richiesta di connessione
     * al server del canale.
     */
    void onJoinChannelRequest(String channelName, String channelPassword)
            throws NoServerConnectionException;

    /***
     * Chiamato quando è stata stabilita la connessione al canale da parte di qualche utente.
     *
     * @param channelName
     * @param nickname il nickname dell'utente che è entrato nel canale.
     */
    void onChannelJoin(String channelName, String nickname);

    /***
     * Invia un messaggio.
     *
     * @param target canale o utente a cui inviare il messaggio.
     * @param message messaggio da inviare.
     * @throws NoServerConnectionException se non è stata mai inoltrata la richiesta di connessione
     * al server.
     * @throws NoServerConnectionException se non è stata mai inoltrata la richiesta di join al
     * canale.
     */
    void onSendMessageRequest(String target, String message)
            throws NoServerConnectionException, NoChannelJoinedException;

    /***
     * Manda una richiesta xdcc send.
     *
     * @param botname il bot a cui mandare la richiesta.
     * @param packNumber il numero del pack da farsi mandare.
     * @throws NoServerConnectionException
     * @throws NoChannelJoinedException
     */
    void onXdccSendRequest(String botname, int packNumber) throws NoServerConnectionException
            , NoChannelJoinedException;

    /***
     * Chiamato quando il download sta iniziando.
     */
    void onDonwloadIsStarting();

    /***
     * Chiamato quando un download viene completato correttamento.
     */
    void onDownloadComplete();

    /***
     * Chiamato quando la richiesta di download è messa in coda dal bot.
     *
     * @param botname il bot a cui si era mandata la richiesta.
     * @param packNumber il numero del pack richiesto.
     */
    void onDownloadInQueue(String botname, int packNumber);

    /***
     * Chiamato quando la richiesta di download è rifiutata perchè la coda è piena.
     *
     * @param botname il bot a cui si era mandata la richiesta.
     * @param packNumber il numero del pack richiesto.
     */
    void onDownloadQueueFull(String botname, int packNumber);

    /***
     * Chimato quando il bot ci ha rimosso dalla lista di attesa.
     *
     * @param botname il bot a cui era stata mandata la richiesta.
     */
    void onRemovedFromQueue(String botname);

    /***
     * Chimato quando il bot ci ha mandato un messaggio che non è stato riconosciuto.
     *
     * @param botname il bot a cui era stata mandata la richiesta.
     * @param message il messaggio non riconosciuto.
     */
    void onUnknownXdccMessage(String botname, String message);

    /***
     * Chiamato quando il download va in errore per qualche motivo.
     *
     * @param e l'eccezione causata dal download in errore.
     */
    void onDownloadError(Exception e);

    /***
     * Chiamato quando il server irc ha eseguito una disconessione per svariati motivi:
     * nostra richiesta, timeout della rete, ecc. .
     */
    void onServerDisconnected();

    /***
     * Errore fatale per cui non è più possibile continuare.
     *
     * @param e
     */
    void onFatalError(Exception e);

    /***
     *
     * @param reason
     */
    void onKick(String reason);
}
