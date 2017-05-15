package jdcc.kernels;

public interface Kernel {
    /***
     * Rilascia tutte le risorse detenute da questo kernel.
     */
    void dispose();
}
