package jdcc.settings.entries;

import jdcc.logger.JdccLogger;
import jdcc.settings.Dictionary;

/***
 * Mappa il nome di un'impostazione al nome della rappresentazione intermedia SettingsEntry.
 * Utile per gli switch da linea di comando, che di solito sono del tipo "-x".
 */
public final class SettingsEntryMapper {
    private void SettingsEntryMapper() { }

    public static String map(String name) {
        for (Dictionary dict : Dictionary.values()) {
            if (name.equals(dict.getCmdOptName())
                || name.equals(dict.getCmdLongOptName())
                || name.equals(dict.getFileSettingsName())) {
                return dict.getSettingsEntryName();
            }
        }
        JdccLogger.logger.warn("SettingsEntryMapper: map not found for \"{}\""
                , name);
        return name;
    }
}
