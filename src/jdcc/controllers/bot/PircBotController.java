package jdcc.controllers.bot;

import jdcc.kernels.Kernel;
import jdcc.kernels.bot.BotKernel;
import jdcc.controllers.AbstractController;
import jdcc.dispatcher.DispatcherObserver;
import jdcc.events.Event;
import jdcc.events.commands.*;
import jdcc.logger.JdccLogger;


public class PircBotController extends AbstractController implements BotController,
    DispatcherObserver {

    private BotKernel kernel;

    @Override
    public void setKernel(Kernel kernel) {
        this.kernel = (BotKernel) kernel;
    }

    @Override
    public void notify(Event event) {
        JdccLogger.logger.info("event id: {} event type: {}", event.getId()
                , event.getClass().getCanonicalName());
        event.handle(this);
    }

    @Override
    public void handle(XdccSend event) {
        kernel.sendXdccSendMessage(event.botName, event.packNumber);
    }

    @Override
    public void handle(DirectMessage message) {
        kernel.sendMessage(message.target, message.message);
    }

    @Override
    public void handle(Connect connect) {
        kernel.connectToServer(connect.serverName, connect.port, connect.nickname,
                connect.serverPassword);
    }

    @Override
    public void handle(Disconnect event) {
        kernel.disconnect();
    }

    @Override
    public void handle(JoinChannel join) {
        kernel.joinChannel(join.name, join.password);
    }

    @Override
    public void handle(XdccRemove resume) {
        kernel.sendXdccRemoveMessage(resume.botName, resume.packNumber);
    }
}
