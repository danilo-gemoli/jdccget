package jdcc.settings.parser;

import jdcc.exceptions.SettingsParsingException;
import jdcc.settings.Settings;

public interface SettingsParser {
    /***
     * Fa il parsing delle impostazioni.
     *
     * @throws SettingsParsingException
     * @return il file di configurazione.
     */
    Settings parse() throws SettingsParsingException;
}
