package jdcc.settings;

import jdcc.exceptions.NoFileSettingsFoundException;
import jdcc.exceptions.SettingsParsingException;
import jdcc.logger.JdccLogger;

import java.io.File;

public final class SettingsLoader {

    private static String DEFAULT_CONFIG_FILE = "config.txt";

    private SettingsLoader() { }

    /***
     * Carica le impostazioni dal file di dafault.
     *
     * @return le impostazioni caricate.
     * @throws NoFileSettingsFoundException
     * @throws SettingsParsingException
     */
    public static Settings load() throws NoFileSettingsFoundException, SettingsParsingException {
        return loadFromFile(DEFAULT_CONFIG_FILE);
    }

    /***
     * Carica le impostazioni da file.
     *
     * @param filename filename comprensivo di path.
     * @return le impostazioni caricate.
     * @throws NoFileSettingsFoundException
     * @throws SettingsParsingException
     */
    public static Settings loadFromFile(String filename)
            throws NoFileSettingsFoundException, SettingsParsingException {
        if (!new File(filename).exists()) {
            throw new NoFileSettingsFoundException();
        }

        try {
            SettingsParser settingsParser = new SimpleSettingsParser();
            return settingsParser.parse(filename);
        } catch (Exception e) {
            JdccLogger.logger.error("settings parsing error", e);
            throw new SettingsParsingException();
        }
    }



}
