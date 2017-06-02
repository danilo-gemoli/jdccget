package jdcc.settings.parser;

import jdcc.exceptions.SettingsParsingException;
import jdcc.settings.entries.SettingsEntry;

import java.util.List;

public interface SettingsParser {
    /***
     * Fa il parsing delle impostazioni.
     *
     * @throws SettingsParsingException
     * @return una lista di oggetti "impostazione".
     */
    List<SettingsEntry> parse() throws SettingsParsingException;
}
