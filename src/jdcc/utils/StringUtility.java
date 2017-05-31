package jdcc.utils;

public final class StringUtility {
    /***
     * Controlla se tutte le parole sono contenute nella stringa.
     *
     * @param str la stringa su cui fare il match.
     * @param words array di parole da controllare nella stringa.
     * @param caseSensitive abilita/disabilita il controllo sulle maiuscole.
     * @return true se la stringa contiene tutte le parole, false altrimenti.
     */
    public static boolean stringContainsWords(String str, String[] words, boolean caseSensitive) {
        String myStr = str;
        if (!caseSensitive) {
            myStr = myStr.toLowerCase();
        }
        for (String word : words) {
            String currentWord = word;
            if (!caseSensitive)
                currentWord = currentWord.toLowerCase();
            if (!myStr.contains(currentWord))
                return false;
        }
        return true;
    }

    /***
     * Testa se la stringa è nulla o vuota.
     *
     * @param str la stringa da testare.
     * @return true se nulla o vuota, false altrimenti.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.equals("");
    }
}
