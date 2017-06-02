package jdcc.settings.parser;

import jdcc.exceptions.SettingsParsingException;
import jdcc.settings.Settings;
import jdcc.settings.entries.SettingsEntry;

import java.util.List;

public class SimpleCmdSettingsParse extends AbstractSettingsParser implements CommandLineSettingsParser {
    private String[] cmdArgs;

    public SimpleCmdSettingsParse() {
        super();
    }

    @Override
    public void setCommandLineArgs(String[] args) {
        this.cmdArgs = args;
    }

    @Override
    public List<SettingsEntry> parse() throws SettingsParsingException {
        // TODO
        settingsEntries.add(new SettingsEntry("config-path"
                , Settings.DEFAULT_CONFIG_PATH.toString()));
        return settingsEntries;
    }
}
