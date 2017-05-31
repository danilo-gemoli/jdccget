package jdcc.settings.parser;

import jdcc.exceptions.SettingsParsingException;
import jdcc.settings.Settings;

public class SimpleCmdSettingsParse extends AbstractSettingsParser implements CommandLineSettingsParser {
    private String[] cmdArgs;

    @Override
    public void setCommandLineArgs(String[] args) {
        this.cmdArgs = args;
    }

    @Override
    public Settings parse() throws SettingsParsingException {
        // TODO
        Settings settings = new Settings();
        settings.CONFIG_PATH = Settings.DEFAULT_CONFIG_PATH;
        return settings;
    }
}
