package jdcc.settings.entries;

import jdcc.logger.JdccLogger;

/***
 * Mappa il nome di un'impostazione al nome della rappresentazione intermedia SettingsEntry.
 * Utile per gli switch da linea di comando, che di solito sono del tipo "-x".
 */
public final class SettingsEntryMapper {
    private void SettingsEntryMapper() { }

    public static String map(String name) {
        switch (name) {
            case Dictionary.CONFIG_PATH:
            case Dictionary.SERVER_HOSTNAME:
            case Dictionary.SERVER_PORT:
            case Dictionary.NICKNAME:
            case Dictionary.CHANNEL:
            case Dictionary.BOT_NAME:
            case Dictionary.PACK_NUMBER:
            case Dictionary.RESUME_DOWNLOAD:
            case Dictionary.DOWNLOAD_PATH:
            case Dictionary.HI_CHANNEL_MSG:
                break;
            default:
                JdccLogger.logger.warn("SettingsEntryMapper: map not found for \"{}\""
                    , name);
        }
        return name;
    }
}
