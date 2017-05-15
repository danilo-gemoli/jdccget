package jdcc.kernels.bot;

import java.io.IOException;

/***
 * Rappresenta una connessione DCC con un client IRC.
 */
public interface FileTransferConnection {

    /***
     *
     * @return il nome del file che il il client vuole mandarci.
     */
    String getFilename();

    /***
     * Imposta la destinazione del file da scaricare.
     *
     * @param filepath path + filename del file da scaricare.
     */
    void setDestinationFilepath(String filepath);

    /***
     * Accetta il trasferimento del download.
     *
     * @param resumeDownload riprende il download se parte del file è già presente.
     * @throws IOException
     * @throws InterruptedException
     */
    void acceptDownload(boolean resumeDownload) throws IOException, InterruptedException;

    /***
     * Inizia il download.
     *
     * @throws IOException
     */
    void startDownload() throws IOException;


    /***
     * Indica se il download è stato ripreso da un file già esistente.
     * @return
     */
    boolean hasBeenResumed();

    /***
     * Se esiste un file parziale sul filesystem che si vuole riprendere, questo metodo
     * ritorna la sua grandezza iniziale.
     * @return
     */
    long getPartialFileStartLength();

    /***
     * Verifica se il download ha finito.
     *
     * @return true se ha finito, false altrimenti.
     */
    boolean isFinished();

    /***
     * Imposta la callback per gestire gli eventi del trasferimento file.
     *
     * @param callback
     */
    void setCallback(FileTransferCallback callback);

}
