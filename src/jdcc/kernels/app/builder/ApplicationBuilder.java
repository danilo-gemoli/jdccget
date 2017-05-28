package jdcc.kernels.app.builder;

import jdcc.exceptions.BuildErrorException;
import jdcc.kernels.app.Application;
import jdcc.settings.Settings;

/***
 * ApplicationBuilder che si occupa di costruire i componenti dell'applicazione.
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
     * @return Application l'applicazione appena costruita.
     * @throws BuildErrorException in caso di errore durante la costruzione
     */
    Application build() throws BuildErrorException;
}
