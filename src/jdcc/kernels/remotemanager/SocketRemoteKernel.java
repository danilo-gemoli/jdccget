package jdcc.kernels.remotemanager;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketRemoteKernel implements RemoteKernel, Runnable {
    private ServerSocket serverSocket;
    private int port;

    public SocketRemoteKernel(int port) {
        this.port = port;
    }

    @Override
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void run() {
        try {
            serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {

    }
}
