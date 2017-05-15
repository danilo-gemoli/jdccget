package jdcc.conditions.base;

import jdcc.events.Event;

/***
 * Questa interfaccia rappresenta una condizione.
 */
public interface Condition
{
    /***
     * Tenta di soddisfazione la condizione con l'evento; siccome la condizione puà dipendere
     * a sua volta da altre condizioni, non è detto che sia subito soddisfatta.
     *
     * @param e L'evento da passare alla condizione.
     */
    void tryToSatisfy(Event e);

    /***
     * Determina se la condizione è soddisfatta.
     *
     * @return true soddisfatta, false altrimenti.
     */
    boolean isSatisfied();
}
