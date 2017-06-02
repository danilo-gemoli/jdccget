package jdcc.settings.validation;

import jdcc.settings.entries.SettingsEntry;

/***
 * Questa intefaccia si occupa di validare tutte le impostazioni.
 */
public interface Validator {
    void validateConfigPath(SettingsEntry entry);

    void validateServerHostname(SettingsEntry entry);

    void validateServerPort(SettingsEntry entry);

    void validateNickname(SettingsEntry entry);

    void validateChannel(SettingsEntry entry);

    void validateBotname(SettingsEntry entry);

    void validatePackNumber(SettingsEntry entry);

    void validateResumeDownload(SettingsEntry entry);

    void validateDownloadPath(SettingsEntry entry);

    void validateHiChannelMsg(SettingsEntry entry);
}
