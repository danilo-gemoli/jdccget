package jdcc.kernels.app;

import jdcc.controllers.app.AppController;
import jdcc.controllers.bot.BotController;
import jdcc.controllers.botmanager.ManagerController;
import jdcc.controllers.download.DownloadController;
import jdcc.dispatcher.Dispatcher;
import jdcc.ircparser.IrcMessageParser;
import jdcc.kernels.Kernel;
import jdcc.kernels.bot.BotKernel;
import jdcc.kernels.botmanager.BotKernelManager;
import jdcc.kernels.downloadmanager.DownloadKernel;
import jdcc.kernels.downloadmanager.output.DownloadOutputWriter;
import jdcc.kernels.downloadmanager.statistics.DownloadStatistics;

public interface Application extends Kernel {
    /***
     * Avvia l'applicazione.
     *
     * @throws Exception
     */
    void start() throws Exception;

    /***
     * Imposta lo stato d'uscita dell'applicazione. Solitamente questo metodo viene chiamato
     * subito prima di terminare l'applicazione.
     *
     * @param status lo stato d'uscita.
     */
    void setExitStatus(int status);

    /***
     * Imposta il controller dell'applicazione.
     *
     * @param controller il controller.
     */
    void setController(AppController controller);

    void setDispatcher(Dispatcher dispatcher);

    void setBotKernel(BotKernel botKernel);

    void setBotController(BotController botController);

    void setKernelManager(BotKernelManager botKernelManager);

    void setManagerController(ManagerController managerController);

    void setDownloadKernel(DownloadKernel downloadKernel);

    void setDownloadController(DownloadController downloadController);

    void setMessageParser(IrcMessageParser ircMessageParser);

    void setDownloadOutputWriter(DownloadOutputWriter downloadOutputWriter);

    void setDownloadStatistics(DownloadStatistics downloadStatistics);

    void setHiChannelMessage(String message);

    void setBotName(String botName);

    void setPackNumber(int packNumber);

    void setChannel(String channel);

    void setNickname(String nickname);

    void setRealname(String realname);

    void setLoginname(String realname);

    void setServerHostname(String serverHostname);

    void setServerPort(int serverPort);
}
