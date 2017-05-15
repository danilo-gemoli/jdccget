package jdcc.builder;

import jdcc.dispatcher.Dispatcher;
import jdcc.exceptions.BuildErrorException;
import jdcc.settings.Settings;

/***
 * ApplicationBuilder che si occupa di fare il bootstrap dell'applicazione.
 */
public interface ApplicationBuilder {

    /***
     * Imposta la configurazione per il builder.
     *
     * @param settings la configurazione che usarà questo builder durante la costruzione.
     */
    void setConfiguration(Settings settings);

    /***
     * Costruisce gli oggetti fondamentali dell'applicazione.
     *
     * @throws BuildErrorException in caso di errore durante la costruzione
     */
    void build() throws BuildErrorException;

    /***
     * Ritorna il dispatcher dell'applicazione.
     *
     * @return il dispatcher.
     */
    Dispatcher getDispatcher();

}
