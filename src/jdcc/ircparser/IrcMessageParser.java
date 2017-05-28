package jdcc.ircparser;

import jdcc.exceptions.NoSupportedLanguageException;

public interface IrcMessageParser {
    /***
     * Classe utilità per i linguaggi.
     */
    final class Languages {
        public static final String ALL_LANGUAGES = "all-languages";
    }

    /***
     * Fa il parsing di un messaggio xdcc e ne ritorna il tipo.
     *
     * @param message il messaggio xdcc.
     * @return il tipo di messaggio xdcc.
     */
    XdccMessageType parseXdccMessage(String message);

    /***
     * Estre il pack number da un messaggio xdcc.
     *
     * @param message il messaggio xdcc.
     * @return il pack number estratto, -1 altrimenti.
     */
    int getPackNumberFromMessage(String message);

    /***
     * Imposta la lingua con cui fare il parsing dei messaggi.
     *
     * @param lang la lingua, prova tutte le lingue disponibili se impostato su "all-languages".
     * @throws NoSupportedLanguageException linguaggio richiesto non supportato.
     */
    void setLanguage(String lang) throws NoSupportedLanguageException;

    /***
     * Imposta il match case-sensitive sui messaggi per cui fare il parsing.
     */
    void setCaseSensitive(boolean value);
}
