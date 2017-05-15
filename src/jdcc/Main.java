package jdcc;

import jdcc.builder.ApplicationBuilder;
import jdcc.builder.ConfigBuilder;
import jdcc.dispatcher.Dispatcher;
import jdcc.events.messages.DirectMessageRequest;
import jdcc.events.messages.JoinChannelRequest;
import jdcc.events.messages.ServerConnectionRequest;
import jdcc.events.messages.XdccSendRequest;
import jdcc.exceptions.BuildErrorException;
import jdcc.exceptions.NoFileSettingsFound;
import jdcc.exceptions.SettingsParsingException;
import jdcc.logger.JdccLogger;
import jdcc.settings.Settings;
import jdcc.settings.SettingsLoader;

public class Main {
    private Settings settings;
    private Dispatcher dispatcher;

    public static void main(String[] argv) {
        new Main().start();
    }

    public int start() {
        JdccLogger.logger.info("Main: start");
        try {
            settings = SettingsLoader.load();
        } catch (NoFileSettingsFound noFileSettingsFound) {
            JdccLogger.logger.error("Main: noFileSettingsFound", noFileSettingsFound);
            return 1;
        } catch (SettingsParsingException settingsParsingException) {
            JdccLogger.logger.error("Main: settingsParsingException", settingsParsingException);
            return 1;
        }

        ApplicationBuilder builder = new ConfigBuilder();
        builder.setConfiguration(settings);

        try {
            builder.build();
        } catch (BuildErrorException e) {
            JdccLogger.logger.error("Main: buildErrorException", e);
            return 1;
        }

        dispatcher = builder.getDispatcher();
        sendStartMessages();
        dispatcher.start();
        return 0;
    }

    private void sendStartMessages() {
        sendConnectRequest(dispatcher);
        sendJoinChannelRequest(dispatcher);
        sendMessageRequest(dispatcher, settings.HI_CHANNEL_MSG);
        sendXdccRequest(dispatcher);
    }

    private void sendXdccRequest(Dispatcher dispatcher) {
        XdccSendRequest xdccRequest = new XdccSendRequest();
        xdccRequest.botName = settings.BOT_NAME;
        xdccRequest.packNumber = settings.PACK_NUMBER;
        dispatcher.addMessage(xdccRequest);
    }

    private void sendMessageRequest(Dispatcher dispatcher, String messageContent) {
        DirectMessageRequest directMessage = new DirectMessageRequest();
        directMessage.target = settings.CHANNEL;
        directMessage.message = messageContent;
        dispatcher.addMessage(directMessage);
    }

    private void sendConnectRequest(Dispatcher dispatcher) {
        ServerConnectionRequest connectionRequest = new ServerConnectionRequest();
        connectionRequest.nickname = settings.NICKNAME;
        connectionRequest.serverName = settings.SERVER_HOSTNAME;
        connectionRequest.port = settings.SERVER_PORT;
        dispatcher.addMessage(connectionRequest);
    }

    private void sendJoinChannelRequest(Dispatcher dispatcher) {
        JoinChannelRequest join = new JoinChannelRequest();
        join.channelName = settings.CHANNEL;
        dispatcher.addMessage(join);
    }
}