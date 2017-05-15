package jdcc.kernels.botmanager;

import jdcc.controllers.botmanager.ManagerController;
import jdcc.events.commands.*;
import jdcc.exceptions.NoChannelJoinedException;
import jdcc.exceptions.NoServerConnectionException;
import jdcc.logger.JdccLogger;

/***
 * Questa classe implementa la logica per gestire un singolo download dcc da un server irc;
 * si DEVE quindi fare una sola connessione ad un server, unirsi ad un solo canale e richiedere
 * un solo file.
 * La classe non è thread safe.
 */
public class DccDownloaderKernel implements BotKernelManager {

    private String userNickname;
    private ManagerController controller;
    // Indica se c'è stata una richiesta di connessione ad un server;
    private boolean serverConnectionRequestSent;
    private boolean channelJoinRequestSent;
    // Indica se è stata è stata stabilita la connessione al server;
    private boolean isConnected;
    // Richiesta pending di join a canale.
    private boolean isInChannel;
    private JoinChannel joinChannelCommand;
    private DirectMessage messageCommand;
    private XdccSend xdccSendCommand;
    private boolean hasDownloadFinished;
    private boolean downloadIsStarting;

    public DccDownloaderKernel() {
        userNickname = "";
        serverConnectionRequestSent = false;
        channelJoinRequestSent = false;
        hasDownloadFinished = false;
        isConnected = false;
        isInChannel = false;
        downloadIsStarting = false;
    }

    @Override
    public void setController(ManagerController controller) {
        this.controller = controller;
    }

    @Override
    public void onServerConnected(String serverName) {
        sendPendingCommandsOnServerConnect();
        isConnected = true;
    }

    @Override
    public void onServerConnectRequest(String serverName, String serverPassword, int serverPort
            , String nickname) {
        Connect connect = new Connect();
        connect.serverName = serverName;
        connect.port = serverPort;
        connect.serverPassword = serverPassword;
        connect.nickname = nickname;
        userNickname = nickname;
        controller.sendCommand(connect);
        serverConnectionRequestSent = true;
    }

    @Override
    public void onJoinChannelRequest(String channelName, String channelPassword) throws NoServerConnectionException {
        if (!serverConnectionRequestSent) {
            throw new NoServerConnectionException();
        }

        JoinChannel joinChannel = new JoinChannel();
        joinChannel.name = channelName;
        joinChannel.password = channelPassword;

        if (isConnected) {
            controller.sendCommand(joinChannel);
        } else {
            storeJoinChannelRequest(joinChannel);
        }
        channelJoinRequestSent = true;
    }

    @Override
    public void onChannelJoin(String channelName, String nickname) {
        if (nickname.equals(userNickname)) {
            isInChannel = true;
            sendPendingCommandsOnChannelJoined();
        }
    }

    @Override
    public void onSendMessageRequest(String target, String message)
            throws NoServerConnectionException, NoChannelJoinedException {
        throwExceptionIfCannotSendMessage();

        DirectMessage msg = new DirectMessage();
        msg.target = target;
        msg.message = message;

        if (isInChannel) {
            controller.sendCommand(msg);
        } else {
            storeMessageRequest(msg);
        }
    }

    @Override
    public void onXdccSendRequest(String botname, int packNumber) throws NoServerConnectionException
            , NoChannelJoinedException {
        throwExceptionIfCannotSendMessage();

        XdccSend xdccSend = new XdccSend();
        xdccSend.botName = botname;
        xdccSend.packNumber = packNumber;

        if (isInChannel) {
            controller.sendCommand(xdccSend);
        } else {
            storeXdccRequest(xdccSend);
        }
    }

    @Override
    public void onDonwloadIsStarting() {
        downloadIsStarting = true;
    }

    @Override
    public void onDownloadComplete() {
        hasDownloadFinished = true;
        sendDisconnectMessage();
    }

    @Override
    public void onDownloadInQueue(String botname, int packNumber) {
        XdccRemove removeCmd = new XdccRemove();
        removeCmd.botName = botname;
        removeCmd.packNumber = packNumber;
        controller.sendCommand(removeCmd);
    }

    @Override
    public void onDownloadQueueFull(String botname, int packNumber) {
        sendDisconnectMessage();
    }

    @Override
    public void onRemovedFromQueue(String botname) {
        sendDisconnectMessage();
    }

    @Override
    public void onUnknownXdccMessage(String botname, String message) {
        JdccLogger.logger.info("DccDownloaderKernel: bot message \"{}\" not recognized.", message);
        if (downloadIsStarting) {
            // TODO: ???
        } else {
            sendDisconnectMessage();
        }
    }

    @Override
    public void onDownloadError(Exception e) {
        // Si tenta una disconnessione corretta.
        sendDisconnectMessage();
    }

    @Override
    public void onServerDisconnected() {
        if (hasDownloadFinished) {
            JdccLogger.logger.info("DccDownloaderKernel: ending app gracefully.");
            exitApp(0);
        } else {
            JdccLogger.logger.info("DccDownloaderKernel: ending app abruptly.");
            exitApp(1);
        }
    }

    @Override
    public void onFatalError(Exception e) {
        JdccLogger.logger.info("DccDownloaderKernel: ending app with errors.");
        JdccLogger.logger.error("DccDownloaderKernel: fatal error.", e);
        exitApp(1);
    }

    @Override
    public void onKick(String reason) {
        // TODO
    }

    @Override
    public void dispose() {

    }

    // METODI PRIVATI
    private void exitApp(int status) {
        System.exit(status);
    }

    private void storeJoinChannelRequest(JoinChannel joinChannel) {
        joinChannelCommand = joinChannel;
    }

    private void storeMessageRequest(DirectMessage message) {
        messageCommand = message;
    }

    private void storeXdccRequest(XdccSend xdccSend) {
        xdccSendCommand = xdccSend;
    }

    private void sendDisconnectMessage() {
        Disconnect disconnect = new Disconnect();
        controller.sendCommand(disconnect);
    }

    private void sendPendingCommandsOnServerConnect() {
        if (joinChannelCommand != null) {
            controller.sendCommand(joinChannelCommand);
        }
    }

    private void sendPendingCommandsOnChannelJoined() {
        if (messageCommand != null) {
            controller.sendCommand(messageCommand);
        }
        if (xdccSendCommand != null) {
            controller.sendCommand(xdccSendCommand);
        }
    }

    private void throwExceptionIfCannotSendMessage() throws NoChannelJoinedException
            , NoServerConnectionException {
        if (!channelJoinRequestSent) {
            throw new NoChannelJoinedException();
        }
        if (!serverConnectionRequestSent) {
            throw new NoServerConnectionException();
        }
    }


}
