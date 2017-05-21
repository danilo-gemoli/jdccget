package jdcc.kernels.downloadmanager.statistics;

/***
 * Interfaccia finalizzata a tenere traccia delle statistiche sul download.
 */
public interface DownloadStatistics {
    /***
     * Imposta il momento in cui è partito il download.
     *
     * @param startTime inizio download in millisecondi.
     */
    void setDownloadStartTime(long startTime);

    /***
     * Imposta la posizione da cui sta riprendendo il download.
     *
     * @param bytes il numero di bytes già presenti.
     */
    void setDownloadStartPosition(long bytes);

    /***
     * Imposta i bytes totali da scaricare.
     *
     * @param bytes i bytes.
     */
    void setTotalBytesToDownload(long bytes);

    /***
     * Imposta i bytes scaricati in questo momento.
     *
     * @param bytes i bytes.
     */
    void setCurrentDownloadedBytes(long bytes);

    /***
     * Calcola la percentuale di download.
     *
     * @return la percentuale di download.
     */
    int getDownloadCompletingPercentage();

    /***
     * Calcola il tempo rimamente in base ai parametri correnti.
     *
     * @return il tempo rimanente in secondi.
     */
    long getRemainingTimeInSeconds();

    /***
     * Ritorna i bytes/KBytes etc. che rimangono da scaricare.
     *
     * @return la grandezza del file rimanente.
     */
    Size getDownloadedSize();

    /***
     * La grandezza del file arrotondata all'unità di misura più vicina.
     *
     * @return la grandezza del file.
     */
    Size getTotalSizeToDownload();

    /***
     * Calcola e ritorna la velocità corrente del download in base ai parametri
     * impostati.
     * Il risultato è arrotondato all'unità di misura più vicina.
     *
     * @return velocità corrente.
     */
    Size getCurrentDownloadSpeed();
}
