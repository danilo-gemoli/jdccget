package jdcc.kernels.remotemanager;

import jdcc.kernels.Kernel;

import java.io.IOException;

/***
 * Chi implementa questa interfaccia deve gestire tutta la logica
 * delle richieste eseguite da remoto.
 */
public interface RemoteKernel extends Kernel {

    /***
     * Abilita la ricezione di comandi da remoto.
     *
     * @param port la porta su cui stare in ascolto.
     */
    void start(int port) throws IOException;

    /***
     * Disabilita la ricezione di comandi da remoto.
     */
    void shutdown();

}
