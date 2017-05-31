package jdcc.settings;

import jdcc.exceptions.CommandLineArgsMalformedException;
import jdcc.exceptions.NoFileSettingsFoundException;
import jdcc.exceptions.SettingsLoadingException;
import jdcc.exceptions.SettingsParsingException;
import jdcc.logger.JdccLogger;
import jdcc.settings.parser.CommandLineSettingsParser;
import jdcc.settings.parser.FileSettingsParser;
import jdcc.settings.parser.SimpleCmdSettingsParse;
import jdcc.settings.parser.SimpleFileSettingsParser;

import java.io.File;

public class SettingsManager {
    private Settings cmdSettings;
    private Settings fileSettings;
    private Settings mergedSettings;
    private FileSettingsParser fileSettingsParser;
    private CommandLineSettingsParser cmdSettingsParser;

    public SettingsManager() {
        fileSettingsParser = new SimpleFileSettingsParser();
        cmdSettingsParser = new SimpleCmdSettingsParse();
    }

    /***
     * Carica le impostazioni dalla linea di comando, successivamente dal file di configurazione
     * e poi fa il merge delle due configurazioni.
     *
     * @param args le impostazioni da linea di comando.
     * @return le impostazioni caricate.
     * @throws NoFileSettingsFoundException
     * @throws SettingsParsingException
     */
    public Settings load(String[] args) throws SettingsLoadingException {
        boolean merge = true;
        try {
            cmdSettings = doLoadFromCommandLineArgs(args);
        } catch (CommandLineArgsMalformedException cmdEx) {
            JdccLogger.logger.error("SettingsManager: load - CommandLineArgsMalformedException", cmdEx);
            merge = false;
        }

        try {
            fileSettings = doLoadFromFile(cmdSettings.CONFIG_PATH.toString());
        } catch (NoFileSettingsFoundException noFileSettingsFoundException) {
            JdccLogger.logger.error("SettingsManager: noFileSettingsFoundException", noFileSettingsFoundException);
            throw new SettingsLoadingException();
        } catch (SettingsParsingException settingsParsingException) {
            JdccLogger.logger.error("SettingsManager: settingsParsingException", settingsParsingException);
            throw new SettingsLoadingException();
        }

        if (merge) {
            JdccLogger.logger.trace("SettingsManager: merging settings");
            mergeSettings();
            mergedSettings.assignDefaultToNullValues();
            return mergedSettings;
        }
        fileSettings.assignDefaultToNullValues();
        return fileSettings;
    }

    /***
     * Carica le impostazioni da file.
     *
     * @param filename filename comprensivo di path.
     * @return le impostazioni caricate.
     * @throws NoFileSettingsFoundException
     * @throws SettingsParsingException
     */
    public Settings loadFromFile(String filename)
            throws NoFileSettingsFoundException, SettingsParsingException {
        fileSettings = doLoadFromFile(filename);
        fileSettings.assignDefaultToNullValues();
        return fileSettings;
    }

    /***
     * Carica le impostazioni dagli argomenti della linea di comando.
     *
     * @param args gli argomenti della linea di comando.
     * @return le impostazioni caricate.
     * @throws CommandLineArgsMalformedException
     */
    public Settings loadFromCommandLineArgs(String[] args)
        throws CommandLineArgsMalformedException {
        cmdSettings = doLoadFromCommandLineArgs(args);
        cmdSettings.assignDefaultToNullValues();
        return cmdSettings;
    }

    /***
     * Fa il merge della configurazione da cmd con quella da file.
     * La configurazione da cmd ha sempre priorità.
     */
    private void mergeSettings() {
        mergedSettings = cmdSettings.overwrite(fileSettings);
    }

    private Settings doLoadFromFile(String filename)
            throws NoFileSettingsFoundException, SettingsParsingException {
        if (!new File(filename).exists()) {
            throw new NoFileSettingsFoundException();
        }
        fileSettingsParser.setConfigFile(filename);
        try {
            return fileSettingsParser.parse();
        } catch (Exception e) {
            JdccLogger.logger.error("SettingsManager: loadFromFile - settings parsing error", e);
            throw new SettingsParsingException();
        }
    }

    private Settings doLoadFromCommandLineArgs(String[] args)
            throws CommandLineArgsMalformedException {
        cmdSettingsParser.setCommandLineArgs(args);
        try {
            return cmdSettingsParser.parse();
        } catch (SettingsParsingException e) {
            JdccLogger.logger.error("SettingsManager: loadFromCommandLineArgs - settings parsing error", e);
            throw new CommandLineArgsMalformedException();
        }
    }
}