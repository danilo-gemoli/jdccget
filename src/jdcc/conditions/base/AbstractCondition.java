package jdcc.conditions.base;

import jdcc.conditions.handler.ConditionEventAdapter;

/***
 * Classe astratta di Condition che implementa le funzionalità di base.
 */
public abstract class AbstractCondition implements Condition
{
    protected ConditionEventAdapter eventAdapter;

    protected boolean satisfied;

    @Override
    public boolean isSatisfied() {
        return satisfied;
    }
}
