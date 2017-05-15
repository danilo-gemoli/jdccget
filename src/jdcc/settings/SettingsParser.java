package jdcc.settings;

import java.io.IOException;

public interface SettingsParser {
    /***
     * Fa il parsing caricando le impostazioni da file.
     *
     * @param filename filename completo di path.
     * @return l'oggetto impostazioni.
     */
    Settings parse(String filename) throws IOException;
}
