package jdcc.kernels.bot;

public interface FileTransferCallback {

    /***
     * Metodo invocato ogni qual volta si finisce una lettura dal socket
     * della connessione DCC durante il trasferimento del file.
     *
     * @param bytesReceived numero di bytes letti dopo l'ultima lettura.
     * @param fileSize dimensione totale del file da trasferire.
     */
    void onBytesReceived(int bytesReceived, long fileSize);

}
