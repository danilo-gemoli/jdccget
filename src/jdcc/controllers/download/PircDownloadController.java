package jdcc.controllers.download;

import jdcc.controllers.AbstractController;
import jdcc.dispatcher.DispatcherObserver;
import jdcc.events.messages.XdccDownloading;
import jdcc.kernels.Kernel;
import jdcc.kernels.downloadmanager.DownloadKernel;
import jdcc.events.Event;
import jdcc.events.messages.DownloadConnection;
import jdcc.logger.JdccLogger;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PircDownloadController extends AbstractController
        implements DownloadController, DispatcherObserver {

    private final ScheduledThreadPoolExecutor threadsExecutor = new ScheduledThreadPoolExecutor(1);
    private DownloadKernel kernel;
    private Boolean resumeDownload;
    private Lock xdccDownloadMessageLock;
    private Condition xdccDownloadMessageArrived;
    private long timeToWaitDownloadMsg;

    public  PircDownloadController() {
        xdccDownloadMessageLock = new ReentrantLock();
        xdccDownloadMessageArrived = xdccDownloadMessageLock.newCondition();
    }

    @Override
    public void setKernel(Kernel kernel) {
        this.kernel = (DownloadKernel) kernel;
    }

    @Override
    public void notify(Event event) {
        event.handle(this);
    }

    @Override
    public void setTimeToWaitDownloadMessage(long timeToWaitMillis) {
        this.timeToWaitDownloadMsg = timeToWaitMillis;
    }

    @Override
    public void handle(DownloadConnection event) {
        waitForBotDownloadingMessageBeforeHandleDownload(event);
    }

    @Override
    public void handle(XdccDownloading event) {
        try {
            xdccDownloadMessageLock.lock();
            setResumeDownload(event.resumeSupported);
            xdccDownloadMessageArrived.signalAll();
        } finally {
            xdccDownloadMessageLock.unlock();
        }
    }

    /***
     * Quando si richiede un download il bot invia un messaggio informando se può fare la ripresa
     * del download oppure no. Prima di accettare il download, si vuole, quindi, aspettare questo
     * messaggio (non all'infinito ma con un timeout) in modo tale da impostare in modo corretto
     * l'impostazione di ripresa.
     */
    private void waitForBotDownloadingMessageBeforeHandleDownload(DownloadConnection event) {
        final PircDownloadController me = this;
        threadsExecutor.execute(() -> {
            Boolean resume = null;
            boolean lockAlreadyReleased = false;
            try {
                xdccDownloadMessageLock.lock();
                resume = me.getResumeDownload();
                if (resume == null) {
                    xdccDownloadMessageArrived.await(timeToWaitDownloadMsg, TimeUnit.MILLISECONDS);
                    resume = me.getResumeDownload();
                }
                if (resume != null) {
                    // *PARANOIA* Meglio non avere nessun lock quando si entra nel kernel.
                    xdccDownloadMessageLock.unlock();
                    lockAlreadyReleased = true;
                    JdccLogger.logger.info("PircDownloadController: waitForBotDownloadingMessageBeforeHandleDownload: bot resume download: \"{}\"",
                            resume);
                    kernel.setResumeDownload(resume.booleanValue());
                }
            } catch (InterruptedException ie) {
                JdccLogger.logger.warn(
                        "PircDownloadController: waitForBotDownloadingMessageBeforeHandleDownload: thread interrupted",
                        resume);
            } finally {
                if (!lockAlreadyReleased) {
                    xdccDownloadMessageLock.unlock();
                }
            }
            kernel.onNewFileTransferConnection(event.fileTransferConnection);
        });
    }

    private void setResumeDownload(boolean value) {
        resumeDownload = value;
    }

    private Boolean getResumeDownload() {
        return resumeDownload;
    }
}
