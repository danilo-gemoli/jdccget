package jdcc.controllers.app;

import jdcc.controllers.AbstractController;
import jdcc.dispatcher.DispatcherObserver;
import jdcc.events.Event;
import jdcc.events.commands.ShutDown;
import jdcc.kernels.Kernel;
import jdcc.kernels.app.Application;

public class MainAppController extends AbstractController implements AppController
        , DispatcherObserver {

    private Application appKernel;

    @Override
    public void setKernel(Kernel kernel) {
        this.appKernel = (Application) kernel;
    }

    @Override
    public void notify(Event event) {
        event.handle(this);
    }

    @Override
    public void handle(ShutDown event) {
        appKernel.setExitStatus(event.exitStatus);
        appKernel.dispose();
    }
}
