package jdcc.settings.validation;

import jdcc.logger.JdccLogger;
import jdcc.settings.entries.SettingsEntry;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class SyntacticValidator extends AbstractValidator {
    @Override
    public void validateConfigPath(SettingsEntry entry) {
        if (nullityCheck(entry)) return;
        validatePath(entry);
    }

    @Override
    public void validateServerHostname(SettingsEntry entry) {

    }

    @Override
    public void validateServerPort(SettingsEntry entry) {
        if (nullityCheck(entry)) return;

        int port;
        try {
            port = Integer.parseInt((String) entry.value);
            entry.value = new Integer(port);
        } catch (Exception e) {
            JdccLogger.logger.warn("SyntacticValidator: server port error, using default");
            entry.value = null;
        }
    }

    @Override
    public void validateNickname(SettingsEntry entry) {

    }

    @Override
    public void validateRealname(SettingsEntry entry) {

    }

    @Override
    public void validateLoginname(SettingsEntry entry) {

    }

    @Override
    public void validateChannel(SettingsEntry entry) {

    }

    @Override
    public void validateBotname(SettingsEntry entry) {

    }

    @Override
    public void validatePackNumber(SettingsEntry entry) {
        if (nullityCheck(entry)) return;

        int packNum;
        try {
            packNum = Integer.parseInt((String) entry.value);
            entry.value = new Integer(packNum);
        } catch (Exception e) {
            entry.value = null;
            JdccLogger.logger.warn("SyntacticValidator: error during pack number \"{}\" validation"
                    , entry.value, e);
        }
    }

    @Override
    public void validateResumeDownload(SettingsEntry entry) {
        if (nullityCheck(entry)) return;

        boolean resume;
        try {
            resume = Boolean.parseBoolean((String) entry.value);
            entry.value = new Boolean(resume);
        } catch (Exception e) {
            entry.value = null;
            JdccLogger.logger.error("SyntacticValidator: resume download validation error \"{}\""
                    , entry.value, e);
        }
    }

    @Override
    public void validateDownloadPath(SettingsEntry entry) {
        if (nullityCheck(entry)) return;
        validatePath(entry);
    }

    @Override
    public void validateHiChannelMsg(SettingsEntry entry) {

    }

    private void validatePath(SettingsEntry entry) {
        try {
            entry.value = Paths.get((String) entry.value);
        } catch (InvalidPathException e) {
            JdccLogger.logger.error("SyntacticValidator: invalid \"{}\" path \"{}\""
                    , entry.name, entry.value, e);
            entry.value = null;
        }
    }
}
