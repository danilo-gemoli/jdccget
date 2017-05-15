package jdcc.conditions.base;

import jdcc.events.Event;

import java.util.LinkedList;
import java.util.List;

/***
 * Classe astratta di CompoundCondition che implementa le funzionalità di base.
 */
public abstract class AbstractCompoundCondition extends AbstractCondition implements
        CompoundCondition
{
    protected List<Condition> subConditions;

    public AbstractCompoundCondition() {
        subConditions = new LinkedList<>();
    }

    @Override
    public void addCondition(Condition condition) {
        subConditions.add(condition);
    }

    @Override
    public boolean isSatisfied() {
        for (Condition c : subConditions)
            if (!c.isSatisfied())
                return false;
        return true;
    }

    @Override
    public void tryToSatisfy(Event e) {
        if (isSatisfied())
            return;
        for (Condition c : subConditions)
            c.tryToSatisfy(e);
    }
}
