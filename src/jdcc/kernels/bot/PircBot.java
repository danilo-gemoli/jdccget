package jdcc.kernels.bot;

import jdcc.controllers.bot.BotController;
import jdcc.events.messages.*;
import jdcc.ircparser.IrcMessageParser;
import jdcc.ircparser.XdccMessageType;
import jdcc.logger.JdccLogger;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputIRC;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PircBot extends ListenerAdapter implements BotKernel {
    private PircBotX pircBot;
    private BotController controller;
    private Configuration.Builder pircBotConfigurationBuilder;
    private Configuration pircBotConfiguration;
    private List<String> botContacted;
    private IrcMessageParser messageParser;

    public PircBot() {
        botContacted = new LinkedList<>();
        pircBotConfigurationBuilder = new Configuration.Builder();
    }

    // KERNEL BOT
    @Override
    public void setController(BotController controller) {
        this.controller = controller;
    }

    @Override
    public void setMessagePaser(IrcMessageParser messageParser) {
        this.messageParser = messageParser;
    }

    @Override
    public void connectToServer(String serverName, int serverPort, String nickname, String serverPassword)
    {
        JdccLogger.logger.debug("PircBot: join server request. name: {} password: {} port: {} nickname {}",
                 serverName, serverPassword, serverPort, nickname);

        configure(nickname, serverName, serverPassword);
        setListener();
        startBot();
    }

    @Override
    public void joinChannel(String channelName, String channelPassword) {
        JdccLogger.logger.debug("PircBot: join channel request. name: {} password: {}", channelName, channelPassword);
        OutputIRC outputIrc = new OutputIRC(pircBot);

        if (channelPassword != null && !channelPassword.equals("")) {
            outputIrc.joinChannel(channelName, channelPassword);
        } else {
            outputIrc.joinChannel(channelName);
        }
    }

    @Override
    public void sendMessage(String target, String message) {
        OutputIRC outputIrc = new OutputIRC(pircBot);
        outputIrc.action(target, message);
    }

    @Override
    public void sendXdccSendMessage(String user, int packNumber) {
        OutputIRC outputIrc = new OutputIRC(pircBot);
        outputIrc.message(user, "xdcc send #" + packNumber);
        botContacted.add(user);
    }

    @Override
    public void sendXdccRemoveMessage(String user, int packNumber) {
        OutputIRC outputIrc = new OutputIRC(pircBot);
        outputIrc.message(user, "xdcc remove #" + packNumber);
    }

    @Override
    public void disconnect() {
        if (pircBot.isConnected()) {
            JdccLogger.logger.debug("PircBot: disconnecting...");
            OutputIRC outputIrc = new OutputIRC(pircBot);
            outputIrc.quitServer();
            JdccLogger.logger.debug("PircBot: disconnected");
        }
    }

    @Override
    public void dispose() {

    }
    // KERNEL BOT

    // IRC EVENT HANDLERS
    @Override
    public void onConnect(ConnectEvent event) throws Exception {
        ServerConnected connectedMessage = new ServerConnected();
        controller.sendEvent(connectedMessage);
    }

    @Override
    public void onJoin(JoinEvent event) throws Exception {
        ChannelJoined channelJoined = new ChannelJoined();
        channelJoined.channelName = event.getChannel().getName();
        channelJoined.userNickname = event.getUser().getNick();
        controller.sendEvent(channelJoined);
    }

    @Override
    public void onIncomingFileTransfer(IncomingFileTransferEvent event) throws Exception {
        FileTransferConnection transferConnection = new PircFileTransfer(event);
        DownloadConnection downloadConnection = new DownloadConnection();
        downloadConnection.fileTransferConnection = transferConnection;
        controller.sendEvent(downloadConnection);
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) throws Exception {
        handleMessage(event);
    }

    @Override
    public void onDisconnect(DisconnectEvent event) throws Exception {
        ServerDisconnected serverDisconnected = new ServerDisconnected();
        controller.sendEvent(serverDisconnected);
    }

    @Override
    public void onKick(KickEvent event) throws Exception {
        Kick kickMsg = new Kick();
        kickMsg.reason = event.getReason();
        controller.sendEvent(kickMsg);
    }
    // IRC EVENT HANDLERS

    // METODI PRIVATI
    private void configure(String nickname, String serverName, String serverPassword) {
        pircBotConfigurationBuilder = new Configuration.Builder()
                .setName(nickname)
                .setServerPassword(serverPassword)
                .addServer(serverName);
        pircBotConfigurationBuilder.setBotFactory(new MyBotFactory());
        pircBotConfiguration = pircBotConfigurationBuilder.buildConfiguration();
    }

    private void setListener() {
        pircBotConfigurationBuilder.addListener(this);
    }

    private void startBot() {
        pircBot = new PircBotX(pircBotConfiguration);
        PircBot me = this;
        Thread botThread = new Thread(() -> {
            try {
                JdccLogger.logger.debug("PircBot: starting...");
                pircBot.startBot();
                JdccLogger.logger.debug("PircBot: endend");
            } catch (IOException e) {
                JdccLogger.logger.error("PircBot: start IOError", e);
                me.sendFatalError(e);
            } catch (IrcException e) {
                JdccLogger.logger.error("PircBot: start IRCError", e);
                me.sendFatalError(e);
            } catch (Exception e) {
                JdccLogger.logger.error("PircBot: generic error", e);
                me.sendFatalError(e);
            }
        });
        botThread.start();
    }

    private void sendFatalError(Exception ex) {
        FatalError fatalError = new FatalError(ex);
        controller.sendEvent(fatalError);
    }

    private void handleMessage(GenericMessageEvent event) {
        if (isInBotCotactList(event.getUser().getNick())) {
            handleBotMessage(event);
        }
    }

    private boolean isInBotCotactList(String botName) {
        return botContacted.contains(botName);
    }

    private void handleBotMessage(GenericMessageEvent event) {
        String message = event.getMessage();
        XdccMessageType msgType = messageParser.parseXdccMessage(message);
        String botName = event.getUser().getNick();

        if (msgType == XdccMessageType.DOWNLOADING
                || msgType == XdccMessageType.BANDWIDTH_LIMIT
                || msgType == XdccMessageType.DOWNLOAD_RESUME_SUPPORTED) {
            // Tutto bene, non fare niente qui.
        } else if (msgType == XdccMessageType.IN_QUEUE) {
            int packNumber = messageParser.getPackNumberFromMessage(message);
            XdccDownloadInQueue inQueueMsg = new XdccDownloadInQueue();
            inQueueMsg.botName = botName;
            inQueueMsg.packNumber = packNumber;
            controller.sendEvent(inQueueMsg);
        } else if (msgType == XdccMessageType.QUEUE_IS_FULL) {
            int packNumber = messageParser.getPackNumberFromMessage(message);
            XdccDownloadQueueFull queueFullMsg = new XdccDownloadQueueFull();
            queueFullMsg.botName = botName;
            queueFullMsg.packNumber = packNumber;
            controller.sendEvent(queueFullMsg);
        } else if (msgType == XdccMessageType.REMOVED_FROM_QUEUE) {
            XdccRemovedFromQueue removedFromQueueMsg = new XdccRemovedFromQueue();
            removedFromQueueMsg.botName = botName;
            controller.sendEvent(removedFromQueueMsg);
        } else if (msgType == XdccMessageType.UNKNOWN) {
            XdccUnknown unknownMsg = new XdccUnknown();
            unknownMsg.botName = botName;
            unknownMsg.rawMessage = message;
            controller.sendEvent(unknownMsg);
        }
    }
}

