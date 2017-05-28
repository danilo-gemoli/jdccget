package jdcc;

import jdcc.kernels.app.Application;
import jdcc.kernels.app.builder.ApplicationBuilder;
import jdcc.kernels.app.builder.ConfigBuilder;
import jdcc.exceptions.BuildErrorException;
import jdcc.exceptions.NoFileSettingsFoundException;
import jdcc.exceptions.SettingsParsingException;
import jdcc.logger.JdccLogger;
import jdcc.settings.Settings;
import jdcc.settings.SettingsLoader;

public class Main {
    public static void main(String[] argv) {
        JdccLogger.logger.info("Main: start");
        Settings settings = null;
        try {
            settings = SettingsLoader.load();
        } catch (NoFileSettingsFoundException noFileSettingsFoundException) {
            JdccLogger.logger.error("Main: noFileSettingsFoundException", noFileSettingsFoundException);
            System.exit(1);
        } catch (SettingsParsingException settingsParsingException) {
            JdccLogger.logger.error("Main: settingsParsingException", settingsParsingException);
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