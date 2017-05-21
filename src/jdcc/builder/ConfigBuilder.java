package jdcc.builder;

import jdcc.exceptions.BuildErrorException;
import jdcc.kernels.bot.BotKernel;
import jdcc.kernels.bot.PircBot;
import jdcc.kernels.botmanager.DccDownloaderKernel;
import jdcc.kernels.botmanager.BotKernelManager;
import jdcc.controllers.bot.BotController;
import jdcc.controllers.bot.PircBotController;
import jdcc.controllers.botmanager.DccDownloaderController;
import jdcc.controllers.botmanager.ManagerController;
import jdcc.controllers.download.DownloadController;
import jdcc.controllers.download.PircDownloadController;
import jdcc.dispatcher.Dispatcher;
import jdcc.dispatcher.SingleThreadDispatcher;
import jdcc.kernels.downloadmanager.DownloadKernel;
import jdcc.kernels.downloadmanager.ThreadedDownload;
import jdcc.kernels.downloadmanager.output.ConsoleOutputWriter;
import jdcc.kernels.downloadmanager.output.DownloadOutputWriter;
import jdcc.exceptions.NoSupportedLanguageException;
import jdcc.ircparser.IrcMessageParser;
import jdcc.ircparser.keywordsparser.KeywordsIrcMessageParser;
import jdcc.kernels.downloadmanager.statistics.DownloadStatistics;
import jdcc.kernels.downloadmanager.statistics.SimpleDownloadStatistics;
import jdcc.settings.Settings;

public class ConfigBuilder implements ApplicationBuilder {

    private Settings settings;

    private Dispatcher dispatcher;
    private BotKernel botKernel;
    private BotController botController;
    private BotKernelManager kernelManager;
    private ManagerController managerController;
    private DownloadKernel downloadKernel;
    private DownloadController downloadController;
    private IrcMessageParser messageParser;
    private DownloadOutputWriter downloadOutputWriter;
    private DownloadStatistics downloadStatistics;

    public ConfigBuilder() { }

    @Override
    public void setConfiguration(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void build() throws BuildErrorException {
        dispatcher = new SingleThreadDispatcher();

        botKernel = new PircBot();
        botController = new PircBotController();

        kernelManager = new DccDownloaderKernel();
        managerController = new DccDownloaderController();

        downloadKernel = new ThreadedDownload();
        downloadController = new PircDownloadController();

        messageParser = new KeywordsIrcMessageParser();
        downloadOutputWriter = new ConsoleOutputWriter();
        downloadStatistics = new SimpleDownloadStatistics();

        try {
            messageParser.setLanguage(IrcMessageParser.Languages.ALL_LANGUAGES);
        } catch (NoSupportedLanguageException e) {
            throw new BuildErrorException(String.format("Impossibile impostare il linguaggio %s " +
                    "nel parser irc", e.language));
        }
        messageParser.setCaseSensitive(false);

        dispatcher.registerCommandsObserver(botController);
        dispatcher.registerMessagesObserver(managerController);
        dispatcher.registerMessagesObserver(downloadController);

        botController.setDispatcher(dispatcher);
        managerController.setDispatcher(dispatcher);
        downloadController.setDispatcher(dispatcher);

        botKernel.setController(botController);
        botKernel.setMessagePaser(messageParser);
        botController.setKernel(botKernel);

        kernelManager.setController(managerController);
        managerController.setKernel(kernelManager);

        downloadKernel.setDownloadStatistics(downloadStatistics);
        downloadKernel.setController(downloadController);
        downloadKernel.setOutputWriter(downloadOutputWriter);
        downloadKernel.setDownloadPath(settings.DOWNLOAD_PATH);
        downloadKernel.setResumeDownload(settings.RESUME_DOWNLOAD);
        downloadController.setKernel(downloadKernel);
    }

    @Override
    public Dispatcher getDispatcher() {
        return dispatcher;
    }
}
