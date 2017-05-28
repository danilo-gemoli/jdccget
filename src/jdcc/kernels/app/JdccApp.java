package jdcc.kernels.app;

import jdcc.controllers.app.AppController;
import jdcc.controllers.bot.BotController;
import jdcc.controllers.botmanager.ManagerController;
import jdcc.controllers.download.DownloadController;
import jdcc.dispatcher.Dispatcher;
import jdcc.events.messages.DirectMessageRequest;
import jdcc.events.messages.JoinChannelRequest;
import jdcc.events.messages.ServerConnectionRequest;
import jdcc.events.messages.XdccSendRequest;
import jdcc.ircparser.IrcMessageParser;
import jdcc.kernels.bot.BotKernel;
import jdcc.kernels.botmanager.BotKernelManager;
import jdcc.kernels.downloadmanager.DownloadKernel;
import jdcc.kernels.downloadmanager.output.DownloadOutputWriter;
import jdcc.kernels.downloadmanager.statistics.DownloadStatistics;

public class JdccApp implements Application {
    private Dispatcher dispatcher;
    private BotKernel botKernel;
    private BotController botController;
    private BotKernelManager kernelManager;
    private ManagerController managerController;
    private DownloadKernel downloadKernel;
    private DownloadController downloadController;
    private IrcMessageParser messageParser;
    private DownloadOutputWriter downloadOutputWriter;
    private DownloadStatistics downloadStatistics;

    private String hiMsg;
    private String botName;
    private int packNumber;
    private String channel;
    private String nickname;
    private String serverHostName;
    private int serverPort;

    private int exitStatus;
    private AppController appController;

    public JdccApp() {
        exitStatus = 0;
    }

    @Override
    public void start() throws Exception {
        sendStartMessages();
        dispatcher.start();
    }

    @Override
    public void setExitStatus(int status) {
        exitStatus = status;
    }

    @Override
    public void setController(AppController controller) {
        this.appController = controller;
    }

    @Override
    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void setBotKernel(BotKernel botKernel) {
        this.botKernel = botKernel;
    }

    @Override
    public void setBotController(BotController botController) {
        this.botController = botController;
    }

    @Override
    public void setKernelManager(BotKernelManager botKernelManager) {
        this.kernelManager = botKernelManager;
    }

    @Override
    public void setManagerController(ManagerController managerController) {
        this.managerController = managerController;
    }

    @Override
    public void setDownloadKernel(DownloadKernel downloadKernel) {
        this.downloadKernel = downloadKernel;
    }

    @Override
    public void setDownloadController(DownloadController downloadController) {
        this.downloadController = downloadController;
    }

    @Override
    public void setMessageParser(IrcMessageParser ircMessageParser) {
        this.messageParser = ircMessageParser;
    }

    @Override
    public void setDownloadOutputWriter(DownloadOutputWriter downloadOutputWriter) {
        this.downloadOutputWriter = downloadOutputWriter;
    }

    @Override
    public void setDownloadStatistics(DownloadStatistics downloadStatistics) {
        this.downloadStatistics = downloadStatistics;
    }

    @Override
    public void setHiChannelMessage(String message) {
        this.hiMsg = message;
    }

    @Override
    public void setBotName(String botName) {
        this.botName = botName;
    }

    @Override
    public void setPackNumber(int packNumber) {
        this.packNumber = packNumber;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void setServerHostname(String serverHostname) {
        this.serverHostName = serverHostname;
    }

    @Override
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void dispose() {
        doDispose();
        exit();
    }

    private void doDispose() {
        dispatcher.unregisterObserver(botController);
        dispatcher.unregisterObserver(downloadController);
        dispatcher.unregisterObserver(managerController);
        dispatcher.unregisterObserver(appController);
        dispatcher.stop();
    }

    private void exit() {
        System.exit(exitStatus);
    }

    private void sendStartMessages() {
        sendConnectRequest();
        sendJoinChannelRequest();
        sendMessageRequest();
        sendXdccRequest();
    }

    private void sendXdccRequest() {
        XdccSendRequest xdccRequest = new XdccSendRequest();
        xdccRequest.botName = botName;
        xdccRequest.packNumber = packNumber;
        dispatcher.addEvent(xdccRequest);
    }

    private void sendMessageRequest() {
        DirectMessageRequest directMessage = new DirectMessageRequest();
        directMessage.target = channel;
        directMessage.message = hiMsg;
        dispatcher.addEvent(directMessage);
    }

    private void sendConnectRequest() {
        ServerConnectionRequest connectionRequest = new ServerConnectionRequest();
        connectionRequest.nickname = nickname;
        connectionRequest.serverName = serverHostName;
        connectionRequest.port = serverPort;
        dispatcher.addEvent(connectionRequest);
    }

    private void sendJoinChannelRequest() {
        JoinChannelRequest join = new JoinChannelRequest();
        join.channelName = channel;
        dispatcher.addEvent(join);
    }
}
