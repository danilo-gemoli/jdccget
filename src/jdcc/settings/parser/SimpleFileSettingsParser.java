package jdcc.settings.parser;

import jdcc.exceptions.SettingsParsingException;
import jdcc.logger.JdccLogger;
import jdcc.settings.Settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Questo parser traduce istruzioni del tipo "key=value;"
 * Ogni istruzione deve trovarsi su una linea separata.
 * Le linee vuote sono ignorate.
 * Le linee che contengono solo spazi sono ignorate.
 * Le linee che iniziano per "#" sono trattate come commenti e quindi ignorate.
 */
public class SimpleFileSettingsParser extends AbstractSettingsParser implements FileSettingsParser {
    private File settingsFile;
    private FileReader fileReader;
    private BufferedReader bufReader;
    private Settings settings;
    private String settingsFilePath;

    public SimpleFileSettingsParser() {
        settings = new Settings();
    }

    @Override
    public void setConfigFile(String configFile) {
        this.settingsFilePath = configFile;
    }

    @Override
    public Settings parse() throws SettingsParsingException {
        try {
            initReader(settingsFilePath);
            String currentLine = readLine();
            while (currentLine != null) {
                Token token = getTokenFromLine(currentLine);
                if (!token.isIgnored()) {
                    assignToken(token);
                }
                currentLine = readLine();
            }
            return settings;
        } catch (Exception e) {
            throw new SettingsParsingException();
        } finally {
            try {
                closeReader();
            } catch (IOException ioEx) {
                JdccLogger.logger.error("SimpleFileSettingsParser: parse", ioEx);
            }
        }
    }

    private void assignToken(Token token) {
        switch (token.key) {
            case "server-hostname":
                parseServerHostname(token.value);
                break;
            case "server-port":
                parseServerPort(token.value);
                break;
            case "nickname":
                parseNickname(token.value);
                break;
            case "channel":
                parseChannel(token.value);
                break;
            case "bot-name":
                parseBotName(token.value);
                break;
            case "pack-number":
                parsePackNumber(token.value);
                break;
            case "download-path":
                parseDownloadPath(token.value);
                break;
            case "resume-download":
                parseResumeDownload(token.value);
                break;
            case "hi-channel-msg":
                parseHiChannelMsg(token.value);
                break;
            default:
                JdccLogger.logger.warn("SimpleFileSettingsParser: token \"{}\"=\"{}\" not recognized ", token.key,
                        token.value);
        }
    }

    private void parseHiChannelMsg(String value) {
        settings.HI_CHANNEL_MSG = value;
    }

    private void parseResumeDownload(String value) {
        boolean resume;
        try {
            resume = Boolean.parseBoolean(value);
            settings.RESUME_DOWNLOAD = new Boolean(resume);
        } catch (Exception e) {
            JdccLogger.logger.warn("SimpleFileSettingsParser: resume download error, using default");
            settings.RESUME_DOWNLOAD = Settings.DEFAULT_RESUME_DOWNLOAD;
            JdccLogger.logger.error("SimpleFileSettingsParser: resume download parsing error \"{}\"", value, e);
        }
    }

    private void parseDownloadPath(String value) {
        Path path;
        try {
            path = Paths.get(value);
        } catch (InvalidPathException e) {
            JdccLogger.logger.error("SimpleFileSettingsParser: invalid path \"{}\"", value, e);
            return;
        }
        if (!Files.isDirectory(path)) {
            JdccLogger.logger.error(
                    "SimpleFileSettingsParser: path \"{}\" is not a directory, using default", value);
            settings.DOWNLOAD_PATH = Settings.DEFAULT_DOWNLOAD_PATH;
        } else if (!Files.isWritable(path)) {
            JdccLogger.logger.warn(
                    "SimpleFileSettingsParser: path \"{}\" is not writable, using default", value);
            settings.DOWNLOAD_PATH = Settings.DEFAULT_DOWNLOAD_PATH;
        } else {
            settings.DOWNLOAD_PATH = path;
        }
    }

    private void parseServerHostname(String value) {
        settings.SERVER_HOSTNAME = value;
    }

    private void parseServerPort(String value) {
        int port;
        try {
            port = Integer.parseInt(value);
            if (!(0 <= port && port < 65536)) {
                JdccLogger.logger.warn(
                        "SimpleFileSettingsParser: server port not in a validrange, using default");
                settings.SERVER_PORT = Settings.DEFAULT_SERVER_PORT;
            } else {
                settings.SERVER_PORT = new Integer(port);
            }
        } catch (Exception e) {
            JdccLogger.logger.warn("SimpleFileSettingsParser: server port error, using default");
            settings.SERVER_PORT = Settings.DEFAULT_SERVER_PORT;
            JdccLogger.logger.warn("SimpleFileSettingsParser: error during parsing server port \"{}\"", value, e);
        }
    }

    private void parseNickname(String value) {
        settings.NICKNAME = value;
    }

    private void parseChannel(String value) {
        settings.CHANNEL = value;
    }

    private void parseBotName(String value) {
        settings.BOT_NAME = value;
    }

    private void parsePackNumber(String value) {
        int packNum;
        try {
            packNum = Integer.parseInt(value);
            settings.PACK_NUMBER = new Integer(packNum);
        } catch (Exception e) {
            settings.PACK_NUMBER = null;
            JdccLogger.logger.warn("SimpleFileSettingsParser: error during parsing pack number \"{}\"", value, e);
        }
    }

    private Token getTokenFromLine(String currentLine) {
        Token token = new Token(currentLine);
        try {
            token.parseLine();
        } catch (Exception e) {
            JdccLogger.logger.warn("SimpleFileSettingsParser: error during tokenizing line \"{}\"", currentLine, e);
        }
        return token;
    }

    private void initReader(String filename) throws FileNotFoundException {
        settingsFile = new File(filename);
        fileReader = new FileReader(settingsFile);
        bufReader = new BufferedReader(fileReader);
    }

    private String readLine() throws IOException {
        return bufReader.readLine();
    }

    private void closeReader() throws IOException {
        bufReader.close();
    }

    private class Token {
        public String key;
        public String value;
        public boolean isAComment;
        public boolean isEmpty;
        private String line;

        public Token(String line) {
            isAComment = false;
            isEmpty = false;
            key = "";
            value = "";
            this.line = line;
        }

        public boolean isIgnored() {
            return isAComment || isEmpty;
        }

        public void parseLine() throws Exception {
            if (line == null || line.trim().equals("")) {
                isEmpty = true;
                return;
            }

            if (line.length() > 0 && line.startsWith("#")) {
                isAComment = true;
                return;
            }

            Pattern p = Pattern.compile("^(?<key>[^=]+)=(?<value>.*)$");
            Matcher m = p.matcher(line);
            if (m.matches()) {
                key = m.group("key");
                value = m.group("value");
            } else {
                throw new Exception("No match found");
            }
        }
    }
}