package jdcc.settings;

import jdcc.settings.entries.SettingsEntry;
import jdcc.settings.validation.Validator;

import java.nio.file.Path;

public enum Dictionary {
    CONFIG_PATH {
        @Override
        public String getCmdOptName() {
            return "cp";
        }

        @Override
        public String getCmdLongOptName() {
            return "config-path";
        }

        @Override
        public String getCmdDescription() {
            return "config file path.";
        }

        @Override
        public String getFileSettingsName() {
            return "config-path";
        }

        @Override
        public String getSettingsEntryName() {
            return "config-path";
        }

        @Override
        public void setValueOnSettings(Settings settings, SettingsEntry entry) {
            settings.CONFIG_PATH = (Path) entry.value;
        }

        @Override
        public void validate(Validator validator, SettingsEntry entry) {
            validator.validateConfigPath(entry);
        }
    },
    SERVER_HOSTNAME {
        @Override
        public String getCmdOptName() {
            return "sh";
        }

        @Override
        public String getCmdLongOptName() {
            return "server-hostname";
        }

        @Override
        public String getCmdDescription() {
            return "irc server hostname";
        }

        @Override
        public String getFileSettingsName() {
            return "server-hostname";
        }

        @Override
        public String getSettingsEntryName() {
            return "server-hostname";
        }

        @Override
        public void setValueOnSettings(Settings settings, SettingsEntry entry) {
            settings.SERVER_HOSTNAME = (String) entry.value;
        }

        @Override
        public void validate(Validator validator, SettingsEntry entry) {
            validator.validateServerHostname(entry);
        }
    },
    SERVER_PORT {
        @Override
        public String getCmdOptName() {
            return "sp";
        }

        @Override
        public String getCmdLongOptName() {
            return "server-port";
        }

        @Override
        public String getCmdDescription() {
            return "irc server port";
        }

        @Override
        public String getFileSettingsName() {
            return "server-port";
        }

        @Override
        public String getSettingsEntryName() {
            return "server-port";
        }

        @Override
        public void setValueOnSettings(Settings settings, SettingsEntry entry) {
            settings.SERVER_PORT = (Integer) entry.value;
        }

        @Override
        public void validate(Validator validator, SettingsEntry entry) {
            validator.validateServerPort(entry);
        }
    },
    NICKNAME {
        @Override
        public String getCmdOptName() {
            return "nn";
        }

        @Override
        public String getCmdLongOptName() {
            return "nickname";
        }

        @Override
        public String getCmdDescription() {
            return "irc nickname";
        }

        @Override
        public String getFileSettingsName() {
            return "nickname";
        }

        @Override
        public String getSettingsEntryName() {
            return "nickname";
        }

        @Override
        public void setValueOnSettings(Settings settings, SettingsEntry entry) {
            settings.NICKNAME = (String) entry.value;
        }

        @Override
        public void validate(Validator validator, SettingsEntry entry) {
            validator.validateNickname(entry);
        }
    },
    CHANNEL {
        @Override
        public String getCmdOptName() {
            return "c";
        }

        @Override
        public String getCmdLongOptName() {
            return "channel";
        }

        @Override
        public String getCmdDescription() {
            return "irc channel to join";
        }

        @Override
        public String getFileSettingsName() {
            return "channel";
        }

        @Override
        public String getSettingsEntryName() {
            return "channel";
        }

        @Override
        public void setValueOnSettings(Settings settings, SettingsEntry entry) {
            settings.CHANNEL = (String) entry.value;
        }

        @Override
        public void validate(Validator validator, SettingsEntry entry) {
            validator.validateChannel(entry);
        }
    },
    BOT_NAME {
        @Override
        public String getCmdOptName() {
            return "bn";
        }

        @Override
        public String getCmdLongOptName() {
            return "bot-name";
        }

        @Override
        public String getCmdDescription() {
            return "botname to request the file";
        }

        @Override
        public String getFileSettingsName() {
            return "bot-name";
        }

        @Override
        public String getSettingsEntryName() {
            return "bot-name";
        }

        @Override
        public void setValueOnSettings(Settings settings, SettingsEntry entry) {
            settings.BOT_NAME = (String) entry.value;
        }

        @Override
        public void validate(Validator validator, SettingsEntry entry) {
            validator.validateBotname(entry);
        }
    },
    PACK_NUMBER {
        @Override
        public String getCmdOptName() {
            return "pn";
        }

        @Override
        public String getCmdLongOptName() {
            return "pack-number";
        }

        @Override
        public String getCmdDescription() {
            return "package number";
        }

        @Override
        public String getFileSettingsName() {
            return "pack-number";
        }

        @Override
        public String getSettingsEntryName() {
            return "pack-number";
        }

        @Override
        public void setValueOnSettings(Settings settings, SettingsEntry entry) {
            settings.PACK_NUMBER = (Integer) entry.value;
        }

        @Override
        public void validate(Validator validator, SettingsEntry entry) {
            validator.validatePackNumber(entry);
        }
    },
    RESUME_DOWNLOAD {
        @Override
        public String getCmdOptName() {
            return "rd";
        }

        @Override
        public String getCmdLongOptName() {
            return "resume-download";
        }

        @Override
        public String getCmdDescription() {
            return "rusume the download";
        }

        @Override
        public String getFileSettingsName() {
            return "resume-download";
        }

        @Override
        public String getSettingsEntryName() {
            return "resume-download";
        }

        @Override
        public void setValueOnSettings(Settings settings, SettingsEntry entry) {
            settings.RESUME_DOWNLOAD = (Boolean) entry.value;
        }

        @Override
        public void validate(Validator validator, SettingsEntry entry) {
            validator.validateResumeDownload(entry);
        }
    },
    DOWNLOAD_PATH {
        @Override
        public String getCmdOptName() {
            return "dp";
        }

        @Override
        public String getCmdLongOptName() {
            return "download-path";
        }

        @Override
        public String getCmdDescription() {
            return "download path";
        }

        @Override
        public String getFileSettingsName() {
            return "download-path";
        }

        @Override
        public String getSettingsEntryName() {
            return "download-path";
        }

        @Override
        public void setValueOnSettings(Settings settings, SettingsEntry entry) {
            settings.DOWNLOAD_PATH = (Path) entry.value;
        }

        @Override
        public void validate(Validator validator, SettingsEntry entry) {
            validator.validateDownloadPath(entry);
        }
    },
    HI_CHANNEL_MSG {
        @Override
        public String getCmdOptName() {
            return "hcm";
        }

        @Override
        public String getCmdLongOptName() {
            return "hi-channel-msg";
        }

        @Override
        public String getCmdDescription() {
            return "message on channel join";
        }

        @Override
        public String getFileSettingsName() {
            return "hi-channel-msg";
        }

        @Override
        public String getSettingsEntryName() {
            return "hi-channel-msg";
        }

        @Override
        public void setValueOnSettings(Settings settings, SettingsEntry entry) {
            settings.HI_CHANNEL_MSG = (String) entry.value;
        }

        @Override
        public void validate(Validator validator, SettingsEntry entry) {
            validator.validateHiChannelMsg(entry);
        }
    };

    public abstract String getCmdOptName();
    public abstract String getCmdLongOptName();
    public abstract String getCmdDescription();
    public abstract String getFileSettingsName();
    public abstract String getSettingsEntryName();
    public abstract void setValueOnSettings(Settings settings, SettingsEntry entry);
    public abstract void validate(Validator validator, SettingsEntry entry);
}