package jdcc.kernels.downloadmanager.output;

import jdcc.kernels.downloadmanager.statistics.Size;

import java.io.IOException;

public interface DownloadOutputWriter {
    /***
     * Imposta la grandezza scaricate del file.
     *
     * @param size grandezza scaricate.
     */
    void setDownloadedSize(Size size);

    /***
     * Imposta il tempo di download rimanente.
     *
     * @param millis tempo rimanente in millisecondi.
     */
    void setRemainingMillis(long millis);

    /***
     * Imposta la percentuale di completamento del download.
     *
     * @param percentage la percentuale.
     */
    void setDownloadPercentage(int percentage);

    /***
     * Imposta la velocità corrente del download.
     *
     * @param speed velocità corrente.
     */
    void setDownloadSpeed(float speed);

    /***
     * Imposta l'unità di misura della velocità di download.
     *
     * @param sizeMeasurementUnit unità di misura.
     */
    void setDownloadSpeedSizeMeasurementUnit(String sizeMeasurementUnit);

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
     * Imposta la grandezza del file.
     *
     * @param fileSize grandezza del file.
     */
    void setFileSize(Size fileSize);

    /***
     * Scrive lo stato corrente del download sull'output implementato.
     *
     * @throws IOException
     */
    void write() throws IOException;
}
