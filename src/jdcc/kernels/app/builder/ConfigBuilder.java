package jdcc.kernels.app.builder;

import jdcc.controllers.app.AppController;
import jdcc.controllers.app.MainAppController;
import jdcc.exceptions.BuildErrorException;
import jdcc.kernels.app.Application;
import jdcc.kernels.app.JdccApp;
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
import jdcc.logger.JdccLogger;
import jdcc.settings.Settings;
import jdcc.utils.StringUtility;

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
    private AppController appController;

    public ConfigBuilder() { }

    @Override
    public void setConfiguration(Settings settings) {
        this.settings = settings;
    }

    @Override
    public Application build() throws BuildErrorException {
        checkSettings();

        Application app = new JdccApp();
        appController = new MainAppController();

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
            throw new BuildErrorException(String.format(
                    "Impossibile impostare il linguaggio %s nel parser irc", e.language));
        }
        messageParser.setCaseSensitive(false);

        dispatcher.registerObserver(botController);
        dispatcher.registerObserver(managerController);
        dispatcher.registerObserver(downloadController);
        dispatcher.registerObserver(appController);

        botController.setDispatcher(dispatcher);
        managerController.setDispatcher(dispatcher);
        downloadController.setDispatcher(dispatcher);
        appController.setKernel(app);

        botKernel.setController(botController);
        botKernel.setMessagePaser(messageParser);
        botController.setKernel(botKernel);

        kernelManager.setController(managerController);
        managerController.setKernel(kernelManager);

        downloadKernel.setDownloadStatistics(downloadStatistics);
        downloadKernel.setController(downloadController);
        downloadKernel.setOutputWriter(downloadOutputWriter);
        downloadKernel.setDownloadPath(settings.DOWNLOAD_PATH);
        downloadKernel.setResumeDownload(settings.RESUME_DOWNLOAD.booleanValue());
        downloadController.setTimeToWaitDownloadMessage(settings.TIME_TO_WAIT_DOWNLOAD_MSG.longValue());
        downloadController.setKernel(downloadKernel);
        downloadController.setBotNickname(settings.BOT_NAME);

        app.setController(appController);
        app.setBotKernel(botKernel);
        app.setDownloadKernel(downloadKernel);
        app.setKernelManager(kernelManager);

        app.setBotController(botController);
        app.setDownloadController(downloadController);
        app.setManagerController(managerController);

        app.setDownloadOutputWriter(downloadOutputWriter);
        app.setDownloadStatistics(downloadStatistics);

        app.setMessageParser(messageParser);

        app.setDispatcher(dispatcher);

        app.setServerHostname(settings.SERVER_HOSTNAME);
        app.setServerPort(settings.SERVER_PORT.intValue());
        app.setChannel(settings.CHANNEL);
        app.setHiChannelMessage(settings.HI_CHANNEL_MSG);
        app.setBotName(settings.BOT_NAME);
        app.setPackNumber(settings.PACK_NUMBER.intValue());
        app.setNickname(settings.NICKNAME);
        app.setRealname(settings.REALNAME);
        app.setLoginname(StringUtility.isNullOrEmpty(settings.LOGINNAME)? settings.REALNAME:
                settings.LOGINNAME);

        return app;
    }

    private void checkSettings() throws BuildErrorException {
        if (settings == null) {
            throw new BuildErrorException("You must specify the application settings.");
        }
        if (StringUtility.isNullOrEmpty(settings.SERVER_HOSTNAME)) {
            throw new BuildErrorException("You must specify a server hostname.");
        }
        if (settings.SERVER_PORT == null) {
            throw new BuildErrorException("You must specify a server port.");
        }
        if (settings.CHANNEL == null) {
            throw new BuildErrorException("You must specify an IRC channel.");
        }
        if (StringUtility.isNullOrEmpty(settings.BOT_NAME)) {
            throw new BuildErrorException("You must specify a bot name.");
        }
        if (settings.PACK_NUMBER == null) {
            throw new BuildErrorException("You must specify a package number.");
        }
        if (StringUtility.isNullOrEmpty(settings.NICKNAME)) {
            throw new BuildErrorException("You must specify a nickname.");
        }
        if (StringUtility.isNullOrEmpty(settings.REALNAME)) {
            throw new BuildErrorException("You must specify a realname.");
        }
        if (StringUtility.isNullOrEmpty(settings.LOGINNAME)) {
            JdccLogger.logger.info("ConfigBuilder: loginname is null, using realname instead.");
        }
        if (settings.RESUME_DOWNLOAD == null) {
            throw new BuildErrorException("You must specify the resume download value.");
        }
        if (settings.TIME_TO_WAIT_DOWNLOAD_MSG == null) {
            throw new BuildErrorException("You must specify a time to wait download message.");
        }
    }
}
