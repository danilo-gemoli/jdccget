package jdcc.utils;

public final class StringUtility {
    /***
     * Controlla se tutte le parole sono contenute nella stringa.
     *
     * @param str la stringa su cui fare il match.
     * @param words array di parole da controllare nella stringa.
     * @param caseSensitive abilita/disabilita il controllo sulle maiuscole.
     * @return true se la stringa contiene tutte le parole, altriemnti false.
     */
    public static boolean stringContainsWords(String str, String[] words, boolean caseSensitive) {
        String myStr = str;
        if (!caseSensitive) {
            myStr = myStr.toLowerCase();
        }
        for (String word : words) {
            if (!myStr.contains(word))
                return false;
        }
        return true;
    }
}
