package jdcc.events.handler;

import jdcc.events.commands.*;
import jdcc.events.commands.XdccSend;
import jdcc.events.messages.*;

/***
 * Classe di convenienza per gestire il multiple dispatch per la logica degli eventi
 * invece di avere uno switch per ognuno.
 */
public interface EventHandler {
    void handle(ChannelJoined event);

    void handle(DirectMessageRequest event);

    void handle(DownloadCompleted event);

    void handle(DownloadConnection event);

    void handle(DownloadIsStarting event);

    void handle(DownloadError event);

    void handle(JoinChannelRequest event);

    void handle(jdcc.events.messages.XdccSendRequest event);

    void handle(ServerConnectionRequest event);

    void handle(ServerConnected event);

    void handle(ServerDisconnected event);

    void handle(Connect event);

    void handle(DirectMessage event);

    void handle(Disconnect event);

    void handle(JoinChannel event);

    void handle(XdccSend event);

    void handle(XdccDownloadInQueue event);

    void handle(XdccDownloadQueueFull event);

    void handle(XdccRemove event);

    void handle(XdccRemovedFromQueue event);

    void handle(XdccUnknown event);

    void handle(FatalError event);

    void handle(Kick event);

    void handle(ShutDown event);
}
