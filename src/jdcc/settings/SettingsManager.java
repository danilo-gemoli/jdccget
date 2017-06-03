package jdcc.settings;

import jdcc.exceptions.*;
import jdcc.logger.JdccLogger;
import jdcc.settings.entries.SettingsEntry;
import jdcc.settings.entries.SettingsEntryMapper;
import jdcc.settings.parser.*;
import jdcc.settings.validation.SemanticValidator;
import jdcc.settings.validation.SyntacticValidator;
import jdcc.settings.validation.Validator;
import jdcc.settings.validation.ValidatorManager;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class SettingsManager {
    private Validator syntacticValidator;
    private Validator semanticValidator;
    private ValidatorManager validatorManager;
    private Settings cmdSettings;
    private Settings fileSettings;
    private Settings mergedSettings;
    private FileSettingsParser fileSettingsParser;
    private CommandLineSettingsParser cmdSettingsParser;

    public SettingsManager() {
        syntacticValidator = new SyntacticValidator();
        semanticValidator = new SemanticValidator();
        validatorManager = new ValidatorManager();
        fileSettingsParser = new SimpleFileSettingsParser();
        cmdSettingsParser = new SimpleCmdSettingsParser();
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
        Path fileSettingsPath;
        try {
            cmdSettings = doLoadFromCommandLineArgs(args);
            fileSettingsPath = cmdSettings.CONFIG_PATH;
        } catch (CommandLineArgsMalformedException cmdEx) {
            JdccLogger.logger.error("SettingsManager: load - CommandLineArgsMalformedException", cmdEx);
            merge = false;
            fileSettingsPath = Settings.DEFAULT_CONFIG_PATH;
        }

        if (fileSettingsPath == null) {
            fileSettingsPath = Settings.DEFAULT_CONFIG_PATH;
        }

        try {
            fileSettings = doLoadFromFile(fileSettingsPath.toString());
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
     * @throws SettingsLoadingException
     */
    public Settings loadFromFile(String filename)
            throws NoFileSettingsFoundException, SettingsLoadingException {
        try {
            fileSettings = doLoadFromFile(filename);
            fileSettings.assignDefaultToNullValues();
            return fileSettings;
        } catch (SettingsParsingException e) {
            throw new SettingsLoadingException();
        }
    }

    /***
     * Carica le impostazioni dagli argomenti della linea di comando.
     *
     * @param args gli argomenti della linea di comando.
     * @return le impostazioni caricate.
     * @throws SettingsLoadingException
     */
    public Settings loadFromCommandLineArgs(String[] args)
        throws SettingsLoadingException {
        try {
            cmdSettings = doLoadFromCommandLineArgs(args);
            cmdSettings.assignDefaultToNullValues();
            return cmdSettings;
        } catch (CommandLineArgsMalformedException e) {
            throw new SettingsLoadingException();
        }

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
            return parseValidateAndBuildSettings(fileSettingsParser);
        } catch (SettingsParsingException e) {
            JdccLogger.logger.error("SettingsManager: loadFromFile - settings parsing error", e);
            throw new SettingsParsingException();
        } catch (ValidateException e) {
            JdccLogger.logger.error("SettingsManager: loadFromFile - validation error", e);
            throw new SettingsParsingException();
        } catch (Exception e) {
            JdccLogger.logger.error("SettingsManager: loadFromFile - settings parsing error", e);
            throw new SettingsParsingException();
        }
    }

    private Settings doLoadFromCommandLineArgs(String[] args)
            throws CommandLineArgsMalformedException {
        cmdSettingsParser.setCommandLineArgs(args);
        try {
            return parseValidateAndBuildSettings(cmdSettingsParser);
        } catch (SettingsParsingException e) {
            JdccLogger.logger.error("SettingsManager: loadFromCommandLineArgs - settings parsing error", e);
            throw new CommandLineArgsMalformedException();
        } catch (ValidateException e) {
            JdccLogger.logger.error("SettingsManager: loadFromCommandLineArgs - validation error"
                    , e);
            throw new CommandLineArgsMalformedException();
        }
    }

    /***
     * Esegue parsing, validazione e costruzione della configurazione.
     */
    private Settings parseValidateAndBuildSettings(SettingsParser parser)
            throws SettingsParsingException, ValidateException {
        List<SettingsEntry> settingsEntries = parser.parse();
        mapConfigNamesToSettingsEntriesNames(settingsEntries);
        validatorManager.setSettingsEntries(settingsEntries);
        validatorManager.validate(syntacticValidator);
        validatorManager.validate(semanticValidator);
        Settings settings = validatorManager.buildSettings();
        return settings;
    }

    private void mapConfigNamesToSettingsEntriesNames(List<SettingsEntry> settingsEntries) {
        for (SettingsEntry entry : settingsEntries) {
            entry.name = SettingsEntryMapper.map(entry.name);
        }
    }
}