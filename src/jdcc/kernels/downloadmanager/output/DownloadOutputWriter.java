package jdcc.kernels.downloadmanager.output;

import java.io.IOException;

public interface DownloadOutputWriter {

    /***
     * Imposta il momento in cui è iniziato il downlaod.
     *
     * @param startTime in millisecondi.
     */
    void setStartTime(long startTime);

    /***
     * Imposta la grandezza iniziale del file che si sta scaricando (in caso fosse stato una
     * ripresa di download).
     * @param startLenght
     */
    void setPartialFileStartLenght(long startLenght);

    /***
     * Imposta il nome della rete irc.
     *
     * @param networkName
     */
    void setNetworkName(String networkName);

    /***
     * Imposta il canale su cui si trova il bot.
     *
     * @param channelName
     */
    void setChannelName(String channelName);

    /***
     * Imposta il nome del bot.
     *
     * @param botname
     */
    void setBotName(String botname);

    /***
     * Numero del package.
     *
     * @param packNum
     */
    void setPackNum(int packNum);

    /***
     * Imposta il nome del file.
     *
     * @param filename
     */
    void setFileName(String filename);

    /***
     * Imposta lo stato corrente del download.
     *
     * @param bytesReceived il numero di bytes ricevuti in questo momento.
     * @param fileSize grandezza complessiva del file.
     */
    void setDownloadStatus(long bytesReceived, long fileSize);

    /***
     * Scrive lo stato corrente del download sull'output implementato.
     *
     * @throws IOException
     */
    void write() throws IOException;
}
