package jdcc.conditions.base;

import jdcc.events.Event;

/***
 * Specializzazione di AbstractCondition da cui tutte le condizioni base devono derivare.
 */
public abstract class AbstractBaseCondition extends AbstractCondition
{
    @Override
    public void tryToSatisfy(Event e) {
//        e.handle(eventAdapter);
    }
}
