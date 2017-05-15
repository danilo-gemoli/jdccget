package jdcc.kernels.bot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.dcc.ReceiveFileTransfer;

import java.io.*;
import java.net.Socket;

public class MyBotFactory extends Configuration.BotFactory {

    @Override
    public ReceiveFileTransfer createReceiveFileTransfer(PircBotX bot, Socket socket, User user, File file, long startPosition, long fileSize) {
        return new MyReceiveFileTransfer(bot.getConfiguration(), socket, user, file, startPosition, fileSize);
    }
}

class MyReceiveFileTransfer extends ReceiveFileTransfer
{
    private FileTransferCallback transferCallback;

    public MyReceiveFileTransfer(Configuration configuration, Socket socket, User user, File file
            , long startPosition, long fileSize) {
        super(configuration, socket, user, file, startPosition, fileSize);
    }

    public void setTransferCallback(FileTransferCallback transferCallback) {
        this.transferCallback = transferCallback;
    }

    private void onBytesReceived(int bytesRead, long filesize) {
        if (transferCallback != null) {
            transferCallback.onBytesReceived(bytesRead, filesize);
        }
    }

    protected void transferFile() throws IOException {
        BufferedInputStream socketInput = new BufferedInputStream(socket.getInputStream());
        OutputStream socketOutput = socket.getOutputStream();
        RandomAccessFile fileOutput = new RandomAccessFile(file.getCanonicalPath(), "rw");
        fileOutput.seek(startPosition);

        //Recieve file
        int defaultBufferSize = configuration.getDccTransferBufferSize();
        byte[] inBuffer = new byte[defaultBufferSize];
        byte[] outBuffer = new byte[4];
        while (true) {
            //Adjust buffer based on remaining bytes (if we know how big the file is)
            long remainingBytes = fileSize - bytesTransfered;
            int bufferSize = (remainingBytes > 0 && remainingBytes < defaultBufferSize)
                    ? (int) remainingBytes : defaultBufferSize;

            //Read next part of incomming file
            int bytesRead = socketInput.read(inBuffer, 0, bufferSize);

            if (bytesRead == -1)
                //Done
                break;

            onBytesReceived(bytesRead, fileSize);

            //Write to file
            fileOutput.write(inBuffer, 0, bytesRead);
            bytesTransfered += bytesRead;

            //Send back an acknowledgement of how many bytes we have got so far.
            //Convert bytesTransfered to an "unsigned, 4 byte integer in network byte order", per DCC specification
            outBuffer[0] = (byte) ((bytesTransfered >> 24) & 0xff);
            outBuffer[1] = (byte) ((bytesTransfered >> 16) & 0xff);
            outBuffer[2] = (byte) ((bytesTransfered >> 8) & 0xff);
            outBuffer[3] = (byte) (bytesTransfered & 0xff);
            socketOutput.write(outBuffer);
            onAfterSend();

            // PATCHED
            if (remainingBytes - bytesRead == 0)
                //Were done
                break;
        }
    }
}

