package jdcc.settings.validation;

import jdcc.exceptions.ValidateException;
import jdcc.logger.JdccLogger;
import jdcc.settings.Settings;
import jdcc.settings.entries.SettingsEntry;
import jdcc.settings.Dictionary;

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
            boolean found = false;
            for (Dictionary dict : Dictionary.values()) {
                if (dict.getSettingsEntryName().equals(entry.name)) {
                    if (validator != null) {
                        dict.validate(validator, entry);
                    } else if (settings != null) {
                        dict.setValueOnSettings(settings, entry);
                    }
                    found = true;
                }
            }
            if (!found) {
                JdccLogger.logger.warn("ValidatorManager: entry name \"{}\" not recognized"
                        , entry.name);
            }
        }
    }
}
