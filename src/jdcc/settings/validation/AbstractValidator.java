package jdcc.settings.validation;

import jdcc.logger.JdccLogger;
import jdcc.settings.entries.SettingsEntry;

public abstract class AbstractValidator implements Validator {
    protected boolean nullityCheck(SettingsEntry entry) {
        if (entry.value == null) {
            JdccLogger.logger.warn("AbstractValidator: value of entry \"\" is null.", entry.name);
            return true;
        }
        return false;
    }
}
