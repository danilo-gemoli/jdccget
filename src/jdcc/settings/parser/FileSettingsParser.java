package jdcc.settings.parser;

public interface FileSettingsParser extends SettingsParser {
    /***
     * Imposta il file di configurazione da cui fare il parsing.
     *
     * @param configFile file di configurazione comprensivo di path.
     */
    void setConfigFile(String configFile);
}
