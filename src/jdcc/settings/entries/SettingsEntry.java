package jdcc.settings.entries;

/***
 * Questa classe è una rappresentazione comune di tutte le sorgenti che rappresentano
 * le impostazioni dell'applicazione.
 * Dopo la prima fase di parsing (da file, riga di comando, ecc.) uscirà fuori una collezione di
 * SettingsEntry.
 */
public class SettingsEntry {
    public String name = "";
    public Object value = "";

    public SettingsEntry() { }

    public SettingsEntry(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
