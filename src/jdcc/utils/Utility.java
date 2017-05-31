package jdcc.utils;

public final class Utility {
    /***
     * Questa funzione generica ritorna value se non nullo, altrimenti il valore di default fornito.
     *
     * @param value il valore da testare.
     * @param defValue il valore di default da ritornare.
     * @param <T> il tipo generico.
     * @return value se non nullo, defValue altrimenti.
     */
    public static <T> T getValueNotNullOrDefault(T value, T defValue) {
        return !(value == null) ? value : defValue;
    }

    /***
     * Specializzazione della funzione getValueNotNullOrDefault per le stringhe.
     */
    public static String getValueNotNullOrDefault(String value, String defValue) {
        return !(StringUtility.isNullOrEmpty(value)) ? value : defValue;
    }
}
