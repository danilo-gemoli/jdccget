package jdcc.settings.parser;

import jdcc.settings.entries.SettingsEntry;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSettingsParser implements SettingsParser {
    protected List<SettingsEntry> settingsEntries;

    protected AbstractSettingsParser() {
        settingsEntries = new LinkedList<>();
    }
}
