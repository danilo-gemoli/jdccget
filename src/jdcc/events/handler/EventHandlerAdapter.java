package jdcc.events.handler;

import jdcc.events.commands.*;
import jdcc.events.commands.XdccSend;
import jdcc.events.messages.*;

/***
 * Implementazione "vuota" dell'interfaccia.
 */
public class EventHandlerAdapter implements EventHandler {
    @Override
    public void handle(ChannelJoined event) { }

    @Override
    public void handle(DirectMessageRequest event) { }

    @Override
    public void handle(DownloadCompleted event) { }

    @Override
    public void handle(DownloadConnection event) { }

    @Override
    public void handle(DownloadIsStarting event) { }

    @Override
    public void handle(DownloadError event) { }

    @Override
    public void handle(JoinChannelRequest event) { }

    @Override
    public void handle(jdcc.events.messages.XdccSendRequest event) { }

    @Override
    public void handle(ServerConnectionRequest event) { }

    @Override
    public void handle(ServerConnected event) { }

    @Override
    public void handle(Connect event) { }

    @Override
    public void handle(DirectMessage event) { }

    @Override
    public void handle(Disconnect event) { }

    @Override
    public void handle(JoinChannel event) { }

    @Override
    public void handle(XdccSend event) { }

    @Override
    public void handle(XdccDownloadInQueue event) { }

    @Override
    public void handle(XdccDownloadQueueFull event) { }

    @Override
    public void handle(XdccRemove event) { }

    @Override
    public void handle(XdccRemovedFromQueue event) { }

    @Override
    public void handle(XdccUnknown event) { }

    @Override
    public void handle(FatalError event) { }

    @Override
    public void handle(Kick event) { }

    @Override
    public void handle(ServerDisconnected event) { }

    @Override
    public void handle(ShutDown event) { }
}
