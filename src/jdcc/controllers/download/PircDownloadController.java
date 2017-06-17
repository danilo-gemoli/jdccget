package jdcc.controllers.download;

import jdcc.controllers.AbstractController;
import jdcc.dispatcher.DispatcherObserver;
import jdcc.events.messages.XdccDownloading;
import jdcc.kernels.Kernel;
import jdcc.kernels.bot.FileTransferConnection;
import jdcc.kernels.downloadmanager.DownloadKernel;
import jdcc.events.Event;
import jdcc.events.messages.DownloadConnection;
import jdcc.logger.JdccLogger;
import jdcc.utils.StringUtility;

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
    private Lock lock;
    private Condition xdccDownloadMessageArrived;
    private long timeToWaitDownloadMsg;
    private int acceptedDownloadNum;
    private String botNickname;

    public  PircDownloadController() {
        lock = new ReentrantLock();
        xdccDownloadMessageArrived = lock.newCondition();
        acceptedDownloadNum = 0;
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
    public void setBotNickname(String nickname) {
        botNickname = nickname;
    }

    @Override
    public void handle(DownloadConnection event) {
        if (!acceptDownload(event))
            return;
        threadsExecutor.execute(() -> {
            waitForBotDownloadingMessageBeforeHandleDownload();
            beginDownload(event.fileTransferConnection);
        });
    }

    @Override
    public void handle(XdccDownloading event) {
        try {
            lock.lock();
            setResumeDownload(event.resumeSupported);
            xdccDownloadMessageArrived.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /***
     * Quando si richiede un download il bot invia un messaggio informando se può fare la ripresa
     * del download oppure no. Prima di accettare il download, si vuole, quindi, aspettare questo
     * messaggio (non all'infinito ma con un timeout) in modo tale da impostare in modo corretto
     * l'opzione di ripresa.
     */
    private void waitForBotDownloadingMessageBeforeHandleDownload() {
        Boolean resume = null;
        boolean lockAlreadyReleased = false;
        try {
            lock.lock();
            resume = getResumeDownload();
            if (resume == null) {
                xdccDownloadMessageArrived.await(timeToWaitDownloadMsg, TimeUnit.MILLISECONDS);
                resume = getResumeDownload();
            }
            if (resume != null) {
                // *PARANOIA* meglio non avere nessun lock quando si entra nel kernel.
                lock.unlock();
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
                lock.unlock();
            }
        }
    }

    private void setResumeDownload(boolean value) {
        resumeDownload = value;
    }

    private Boolean getResumeDownload() {
        return resumeDownload;
    }

    private void beginDownload(FileTransferConnection fileTransferConnection) {
        kernel.onNewFileTransferConnection(fileTransferConnection);
    }

    /***
     * Controllo sui download, si vuole evitare di accettare ad occhi chiusi qualsiasi cosa:
     * 1) se ne accetta solo uno;
     * 2) TODO: controllo sul nome del file?
     */
    private boolean acceptDownload(DownloadConnection event) {
        if (acceptedDownloadNum > 0) {
            JdccLogger.logger.info("PircDownloadController: acceptDownload: acceptedDownloadNum > 0");
            return false;
        } else if (!checkBotNickname(event.fileTransferConnection.getUserNickname())) {
            JdccLogger.logger.info("PircDownloadController: acceptDownload: bot nickname doesn't match");
            return false;
        }
        acceptedDownloadNum++;
        return true;
    }

    /***
     * Ritorna true se e solo se il nickname dell'utente che ci propone il download è quello che
     * ci si aspetta.
     */
    private boolean checkBotNickname(String nickname) {
        if (StringUtility.isNullOrEmpty(nickname))
            return false;
        if (botNickname != null)
            return botNickname.equals(nickname);
        return false;
    }
}
