package jdcc.settings;

import jdcc.logger.JdccLogger;

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
class SimpleSettingsParser implements SettingsParser {

    private File settingsFile;
    private FileReader fileReader;
    private BufferedReader bufReader;
    private Settings settings;

    public SimpleSettingsParser() {
        settings = new Settings();
    }

    @Override
    public Settings parse(String filename) throws IOException {
        try {
            initReader(filename);
            String currentLine = readLine();
            while (currentLine != null) {
                Token token = getTokenFromLine(currentLine);
                if (!token.isIgnored()) {
                    assignToken(token);
                }
                currentLine = readLine();
            }
            return settings;
        } finally {
            closeReader();
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
                JdccLogger.logger.warn("Parsing: token \"{}\"=\"{}\" not recognized ", token.key,
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
            settings.RESUME_DOWNLOAD = resume;
        } catch (Exception e) {
            JdccLogger.logger.error("Parsing: resume download parsing error " + value, e);
        }
    }

    private void parseDownloadPath(String value) {
        Path path;

        try {
            path = Paths.get(value);
        } catch (InvalidPathException e) {
            JdccLogger.logger.error("Parsing: invalid path " + value, e);
            return;
        }
        if (!Files.isDirectory(path)) {
            JdccLogger.logger.error("Parsing: path {} is not a directory", value);
        } else {
            settings.DOWNLOAD_PATH = value;
        }
    }

    private void parseServerHostname(String value) {
        settings.SERVER_HOSTNAME = value;
    }

    private void parseServerPort(String value) {
        int port;
        try {
            port = Integer.parseInt(value);
            settings.SERVER_PORT = port;
        } catch (Exception e) {
            JdccLogger.logger.warn("Parsing: error during parsing server port " + value, e);
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
            settings.PACK_NUMBER = packNum;
        } catch (Exception e) {
            JdccLogger.logger.warn("Parsing: error during parsing pack number " + value, e);
        }
    }

    private Token getTokenFromLine(String currentLine) {
        Token token = new Token(currentLine);
        try {
            token.parseLine();
        } catch (Exception e) {
            JdccLogger.logger.warn("Parsing: error during tokenizing line \"" + currentLine + "\"", e);
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