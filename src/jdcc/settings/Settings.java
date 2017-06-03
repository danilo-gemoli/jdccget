package jdcc.settings;

import jdcc.utils.Utility;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Settings {
    public static final Path DEFAULT_CONFIG_PATH = Paths.get("config.txt");
    public static final int DEFAULT_SERVER_PORT = 6667;
    public static final boolean DEFAULT_RESUME_DOWNLOAD = true;
    public static final Path DEFAULT_DOWNLOAD_PATH = Paths.get("./downloaded/test");
    public static final String DEFAULT_HI_CHANNEL_MSG = "hi";
    public static final long DEFAULT_TIME_TO_WAIT_DOWNLOAD_MSG = 3000;

    public Path     CONFIG_PATH;
    public String   SERVER_HOSTNAME;
    public Integer  SERVER_PORT;
    public String   NICKNAME;
    public String   CHANNEL;
    public String   BOT_NAME;
    public Integer  PACK_NUMBER;
    public Boolean  RESUME_DOWNLOAD;
    public Path     DOWNLOAD_PATH;
    public String   HI_CHANNEL_MSG;
    // TODO: renderlo impostabile da cmd e file.
    public Long     TIME_TO_WAIT_DOWNLOAD_MSG;

    protected void assignDefaultToNullValues() {
        CONFIG_PATH = Utility.getValueNotNullOrDefault(CONFIG_PATH, Settings.DEFAULT_CONFIG_PATH);
        HI_CHANNEL_MSG = Utility.getValueNotNullOrDefault(HI_CHANNEL_MSG, Settings.DEFAULT_HI_CHANNEL_MSG);
        SERVER_PORT = Utility.getValueNotNullOrDefault(SERVER_PORT, Settings.DEFAULT_SERVER_PORT);
        RESUME_DOWNLOAD = Utility.getValueNotNullOrDefault(RESUME_DOWNLOAD, Settings.DEFAULT_RESUME_DOWNLOAD);
        DOWNLOAD_PATH = Utility.getValueNotNullOrDefault(DOWNLOAD_PATH, Settings.DEFAULT_DOWNLOAD_PATH);
        TIME_TO_WAIT_DOWNLOAD_MSG = Utility.getValueNotNullOrDefault(TIME_TO_WAIT_DOWNLOAD_MSG, Settings.DEFAULT_TIME_TO_WAIT_DOWNLOAD_MSG);
    }

    protected Settings overwrite(Settings toOverwrite) {
        Settings result = new Settings();
        result.CONFIG_PATH = Utility.getValueNotNullOrDefault(CONFIG_PATH, toOverwrite.CONFIG_PATH);
        result.SERVER_HOSTNAME = Utility.getValueNotNullOrDefault(SERVER_HOSTNAME, toOverwrite.SERVER_HOSTNAME);
        result.SERVER_PORT = Utility.getValueNotNullOrDefault(SERVER_PORT, toOverwrite.SERVER_PORT);
        result.NICKNAME = Utility.getValueNotNullOrDefault(NICKNAME, toOverwrite.NICKNAME);
        result.CHANNEL = Utility.getValueNotNullOrDefault(CHANNEL, toOverwrite.CHANNEL);
        result.BOT_NAME = Utility.getValueNotNullOrDefault(BOT_NAME, toOverwrite.BOT_NAME);
        result.PACK_NUMBER = Utility.getValueNotNullOrDefault(PACK_NUMBER, toOverwrite.PACK_NUMBER);
        result.RESUME_DOWNLOAD = Utility.getValueNotNullOrDefault(RESUME_DOWNLOAD, toOverwrite.RESUME_DOWNLOAD);
        result.DOWNLOAD_PATH = Utility.getValueNotNullOrDefault(DOWNLOAD_PATH, toOverwrite.DOWNLOAD_PATH);
        result.HI_CHANNEL_MSG = Utility.getValueNotNullOrDefault(HI_CHANNEL_MSG, toOverwrite.HI_CHANNEL_MSG);
        result.TIME_TO_WAIT_DOWNLOAD_MSG = Utility.getValueNotNullOrDefault(TIME_TO_WAIT_DOWNLOAD_MSG, toOverwrite.TIME_TO_WAIT_DOWNLOAD_MSG);
        return result;
    }
}
