package jdcc.settings.validation;

import jdcc.logger.JdccLogger;
import jdcc.settings.entries.SettingsEntry;

import java.nio.file.Files;
import java.nio.file.Path;

public class SemanticValidator extends AbstractValidator {
    @Override
    public void validateConfigPath(SettingsEntry entry) {
        if (nullityCheck(entry)) return;

        Path path = (Path) entry.value;
        if (!Files.isRegularFile(path)) {
            JdccLogger.logger.error(
                    "SemanticValidator: config path \"{}\" is not a directory"
                    , path.toString());
            entry.value = null;
        } else if (!Files.isReadable(path)) {
            JdccLogger.logger.warn(
                    "SemanticValidator: config path \"{}\" is not writable"
                    , path.toString());
            entry.value = null;
        }
    }

    @Override
    public void validateServerHostname(SettingsEntry entry) {

    }

    @Override
    public void validateServerPort(SettingsEntry entry) {
        if (nullityCheck(entry)) return;

        int port = (int) entry.value;
        if (!(0 <= port && port < 65536)) {
            JdccLogger.logger.warn(
                    "SemanticValidator: server port not in a valid range");
            entry.value = null;
        }
    }

    @Override
    public void validateNickname(SettingsEntry entry) {

    }

    @Override
    public void validateChannel(SettingsEntry entry) {

    }

    @Override
    public void validateBotname(SettingsEntry entry) {

    }

    @Override
    public void validatePackNumber(SettingsEntry entry) {

    }

    @Override
    public void validateResumeDownload(SettingsEntry entry) {

    }

    @Override
    public void validateDownloadPath(SettingsEntry entry) {
        if (nullityCheck(entry)) return;

        Path path = (Path) entry.value;
        if (!Files.isDirectory(path)) {
            JdccLogger.logger.error(
                    "SemanticValidator: download path \"{}\" is not a directory"
                    , path.toString());
            entry.value = null;
        } else if (!Files.isWritable(path)) {
            JdccLogger.logger.warn(
                    "SemanticValidator: download path \"{}\" is not writable"
                    , path.toString());
            entry.value = null;
        }
    }

    @Override
    public void validateHiChannelMsg(SettingsEntry entry) {

    }
}
