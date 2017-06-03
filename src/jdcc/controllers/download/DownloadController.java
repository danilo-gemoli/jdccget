package jdcc.controllers.download;

import jdcc.controllers.Controller;
import jdcc.kernels.downloadmanager.DownloadKernel;

public interface DownloadController extends Controller {
    /***
     * Imposta il tempo massimo per cui questo controller deve aspettare il messaggio di
     * download da parte del bot.
     *
     * @param timeToWaitMillis il tempo per cui aspettare in millisecondi.
     */
    void setTimeToWaitDownloadMessage(long timeToWaitMillis);
}
