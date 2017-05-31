package jdcc;

import jdcc.exceptions.SettingsLoadingException;
import jdcc.kernels.app.Application;
import jdcc.kernels.app.builder.ApplicationBuilder;
import jdcc.kernels.app.builder.ConfigBuilder;
import jdcc.exceptions.BuildErrorException;
import jdcc.logger.JdccLogger;
import jdcc.settings.Settings;
import jdcc.settings.SettingsManager;

public class Main {
    public static void main(String[] argv) {
        JdccLogger.logger.info("Main: start");
        SettingsManager settingsManager = new SettingsManager();
        Settings settings = null;
        try {
            settings = settingsManager.load(argv);
        } catch (SettingsLoadingException settingsLoadingException) {
            JdccLogger.logger.error("Main: noFileSettingsFoundException", settingsLoadingException);
            System.exit(1);
        }

        ApplicationBuilder builder = new ConfigBuilder();
        builder.setConfiguration(settings);

        Application app = null;
        try {
            app = builder.build();
        } catch (BuildErrorException buildErrorException) {
            JdccLogger.logger.error("Main: buildErrorException", buildErrorException);
            System.exit(1);
        }

        try {
            app.start();
        } catch (Exception appEx) {
            JdccLogger.logger.error("Main: app start exception", appEx);
            System.exit(1);
        }
    }
}