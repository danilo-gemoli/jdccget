package jdcc.controllers.download;

import jdcc.controllers.AbstractController;
import jdcc.dispatcher.DispatcherObserver;
import jdcc.kernels.Kernel;
import jdcc.kernels.downloadmanager.DownloadKernel;
import jdcc.events.Event;
import jdcc.events.messages.DownloadConnection;

public class PircDownloadController extends AbstractController
        implements DownloadController, DispatcherObserver {

    private DownloadKernel kernel;

    @Override
    public void setKernel(Kernel kernel) {
        this.kernel = (DownloadKernel) kernel;
    }

    @Override
    public void notify(Event event) {
        event.handle(this);
    }

    @Override
    public void handle(DownloadConnection event) {
        kernel.onNewFileTransferConnection(event.fileTransferConnection);
    }

}
