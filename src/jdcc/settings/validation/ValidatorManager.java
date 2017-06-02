package jdcc.settings.validation;

import jdcc.exceptions.ValidateException;
import jdcc.logger.JdccLogger;
import jdcc.settings.Settings;
import jdcc.settings.entries.SettingsEntry;
import jdcc.settings.entries.Dictionary;

import java.nio.file.Path;
import java.util.List;

public class ValidatorManager {
    private List<SettingsEntry> settingsEntries;

    public void setSettingsEntries(List<SettingsEntry> settingsEntries) {
        this.settingsEntries = settingsEntries;
    }

    public void validate(Validator validator) throws ValidateException {
        try {
            doActionsOnEntries(validator, null);
        } catch (Exception e) {
            JdccLogger.logger.error("ValidatorManager: validate exception: \"{}\""
                    , e.getMessage(), e);
            throw new ValidateException(e.getMessage());
        }
    }

    public Settings buildSettings() throws ValidateException {
        try {
            Settings settings = new Settings();
            doActionsOnEntries(null, settings);
            return settings;
        } catch (Exception e) {
            JdccLogger.logger.error("ValidatorManager: buildSettings exception: \"{}\""
                    , e.getMessage(), e);
            throw new ValidateException(e.getMessage());
        }
    }

    private void doActionsOnEntries(Validator validator, Settings settings) {
        for (SettingsEntry entry : settingsEntries) {
            switch (entry.name) {
                case Dictionary.SERVER_HOSTNAME:
                    if (validator != null)
                        validator.validateServerHostname(entry);
                    else  if (settings != null)
                        settings.SERVER_HOSTNAME = (String) entry.value;
                    break;
                case Dictionary.SERVER_PORT:
                    if (validator != null)
                        validator.validateServerPort(entry);
                    else  if (settings != null)
                        settings.SERVER_PORT = (Integer) entry.value;
                    break;
                case Dictionary.NICKNAME:
                    if (validator != null)
                        validator.validateNickname(entry);
                    else  if (settings != null)
                        settings.NICKNAME = (String) entry.value;
                    break;
                case Dictionary.CHANNEL:
                    if (validator != null)
                        validator.validateChannel(entry);
                    else  if (settings != null)
                        settings.CHANNEL = (String) entry.value;
                    break;
                case Dictionary.BOT_NAME:
                    if (validator != null)
                        validator.validateBotname(entry);
                    else  if (settings != null)
                        settings.BOT_NAME = (String) entry.value;
                    break;
                case Dictionary.PACK_NUMBER:
                    if (validator != null)
                        validator.validatePackNumber(entry);
                    else  if (settings != null)
                        settings.PACK_NUMBER = (Integer) entry.value;
                    break;
                case Dictionary.DOWNLOAD_PATH:
                    if (validator != null)
                        validator.validateDownloadPath(entry);
                    else  if (settings != null)
                        settings.DOWNLOAD_PATH = (Path) entry.value;
                    break;
                case Dictionary.CONFIG_PATH:
                    if (validator != null)
                        validator.validateConfigPath(entry);
                    else  if (settings != null)
                        settings.CONFIG_PATH = (Path) entry.value;
                    break;
                case Dictionary.RESUME_DOWNLOAD:
                    if (validator != null)
                        validator.validateResumeDownload(entry);
                    else  if (settings != null)
                        settings.RESUME_DOWNLOAD = (Boolean) entry.value;
                    break;
                case Dictionary.HI_CHANNEL_MSG:
                    if (validator != null)
                        validator.validateHiChannelMsg(entry);
                    else  if (settings != null)
                        settings.HI_CHANNEL_MSG = (String) entry.value;
                    break;
                default:
                    JdccLogger.logger.warn("ValidatorManager: entry name \"{}\" not recognized"
                            , entry.name);
            }
        }
    }
}
