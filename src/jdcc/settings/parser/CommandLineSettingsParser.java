package jdcc.settings.parser;

public interface CommandLineSettingsParser extends SettingsParser {
    /***
     * Imposta gli argomenti da linea di comando da cui fare il parsing.
     *
     * @param args gli argomenti da linea di comando.
     */
    void setCommandLineArgs(String[] args);
}
