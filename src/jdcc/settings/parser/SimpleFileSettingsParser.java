package jdcc.settings.parser;

import jdcc.exceptions.SettingsParsingException;
import jdcc.logger.JdccLogger;
import jdcc.settings.entries.SettingsEntry;

import java.io.*;
import java.util.List;
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
    private String settingsFilePath;

    public SimpleFileSettingsParser() {
        super();
    }

    @Override
    public void setConfigFile(String configFile) {
        this.settingsFilePath = configFile;
    }

    @Override
    public List<SettingsEntry> parse() throws SettingsParsingException {
        try {
            initReader(settingsFilePath);
            String currentLine;
            while ((currentLine = readLine()) != null) {
                Token token = getTokenFromLine(currentLine);
                if (!token.isIgnored()) {
                    addSettingsEntry(token);
                }
            }
            return settingsEntries;
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

    private void addSettingsEntry(Token token) {
        SettingsEntry entry = getSettingsEntry(token);
        settingsEntries.add(entry);
    }

    private SettingsEntry getSettingsEntry(Token token) {
        SettingsEntry entry = new SettingsEntry(token.key, token.value);
        return entry;
    }

    private Token getTokenFromLine(String currentLine) {
        Token token = new Token(currentLine);
        try {
            token.parseLine();
        } catch (Exception e) {
            JdccLogger.logger.warn("SimpleFileSettingsParser: error during line \"{}\" tokenization"
                    , currentLine, e);
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