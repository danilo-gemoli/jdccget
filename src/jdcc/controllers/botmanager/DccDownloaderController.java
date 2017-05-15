package jdcc.controllers.botmanager;

import jdcc.kernels.Kernel;
import jdcc.kernels.botmanager.BotKernelManager;
import jdcc.controllers.AbstractController;
import jdcc.dispatcher.DispatcherObserver;
import jdcc.events.Event;
import jdcc.events.messages.*;
import jdcc.exceptions.NoChannelJoinedException;
import jdcc.exceptions.NoServerConnectionException;
import jdcc.logger.JdccLogger;

public class DccDownloaderController extends AbstractController implements ManagerController,
    DispatcherObserver {

    private BotKernelManager kernel;

    public DccDownloaderController() {
    }

    @Override
    public void setKernel(Kernel kernel) {
        this.kernel = (BotKernelManager) kernel;
    }

    @Override
    public void notify(Event event) {
        JdccLogger.logger.info("event: {}", event.getId());
        event.handle(this);
    }

    @Override
    public void handle(DownloadCompleted event) {
        kernel.onDownloadComplete();
    }

    @Override
    public void handle(DownloadIsStarting event) {
        kernel.onDonwloadIsStarting();
    }

    @Override
    public void handle(XdccSendRequest event) {
        try {
            kernel.onXdccSendRequest(event.botName, event.packNumber);
        } catch (NoServerConnectionException e) {
            JdccLogger.logger.error("handleXdccSend error", e);
        } catch (NoChannelJoinedException e) {
            JdccLogger.logger.error("handleXdccSend error", e);
        }
    }

    @Override
    public void handle(ChannelJoined event) {
        kernel.onChannelJoin(event.channelName, event.userNickname);
    }

    @Override
    public void handle(DirectMessageRequest directMessage) {
        try {
            kernel.onSendMessageRequest(directMessage.target, directMessage.message);
        } catch (NoServerConnectionException e) {
            JdccLogger.logger.error("handleDirectMessage error", e);
        } catch (NoChannelJoinedException e) {
            JdccLogger.logger.error("handleDirectMessage error", e);
        }
    }

    @Override
    public void handle(JoinChannelRequest join) {
        try {
            kernel.onJoinChannelRequest(join.channelName, join.channelPassword);
        } catch (NoServerConnectionException e) {
            JdccLogger.logger.error("join channel request error", e);
        }
    }

    @Override
    public void handle(ServerConnected event) {
        kernel.onServerConnected(event.serverName);
    }

    @Override
    public void handle(ServerConnectionRequest connReq) {
        kernel.onServerConnectRequest(connReq.serverName, connReq.serverPassword
            , connReq.port, connReq.nickname);
    }

    @Override
    public void handle(XdccDownloadQueueFull queueFull) {
        kernel.onDownloadQueueFull(queueFull.botName, queueFull.packNumber);
    }

    @Override
    public void handle(XdccDownloadInQueue inQueue) {
        kernel.onDownloadInQueue(inQueue.botName, inQueue.packNumber);
    }

    @Override
    public void handle(XdccRemovedFromQueue removedFromQueue) {
        kernel.onRemovedFromQueue(removedFromQueue.botName);
    }

    @Override
    public void handle(XdccUnknown unknown) {
        kernel.onUnknownXdccMessage(unknown.botName, unknown.rawMessage);
    }

    @Override
    public void handle(ServerDisconnected event) {
        kernel.onServerDisconnected();
    }

    @Override
    public void handle(DownloadError event) {
        kernel.onDownloadError(event.exception);
    }

    @Override
    public void handle(FatalError event) {
        kernel.onFatalError(event.exception);
    }

    @Override
    public void handle(Kick event) {
        kernel.onKick(event.reason);
    }
}
