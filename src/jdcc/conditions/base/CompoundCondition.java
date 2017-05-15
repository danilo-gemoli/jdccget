package jdcc.conditions.base;

/***
 * Questa interfaccia rappresenta una condizione composta, cioè una condizione che a sua volta
 * può avere delle sottocondizioni.
 */
public interface CompoundCondition extends Condition
{
    /***
     * Aggiunge una sottocondizione alla condizione.
     *
     * @param condition la sottocondizione da aggiungere.
     */
    void addCondition(Condition condition);
}
