package jdcc.kernels.downloadmanager;

import jdcc.kernels.Kernel;
import jdcc.kernels.bot.FileTransferConnection;
import jdcc.controllers.download.DownloadController;
import jdcc.kernels.downloadmanager.output.DownloadOutputWriter;

public interface DownloadKernel extends Kernel {

    /***
     * Imposta il controller di questo kernel.
     *
     * @param controller
     */
    void setController(DownloadController controller);

    /***
     * Imposta la cartella nella quale verrano scaricati i files.
     *
     * @param downloadPath
     */
    void setDownloadPath(String downloadPath);

    /***
     * Imposta il writer dello stato del download.
     *
     * @param outputWriter
     */
    void setOutputWriter(DownloadOutputWriter outputWriter);

    /***
     * Evento nuovo download.
     *
     * @param fileTransferConnection la nuova connessione download.
     */
    void onNewFileTransferConnection(FileTransferConnection fileTransferConnection);

    /***
     * Abilita/disabilita la ripresa del download se parte del file è già presenet.
     *
     * @param resume
     */
    void setResumeDownload(boolean resume);
}
