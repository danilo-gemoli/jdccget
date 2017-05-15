package jdcc.events;

import java.util.UUID;

public abstract class AbstractEvent implements Event {

    protected UUID id;

    public AbstractEvent() {
        this.id = UUID.randomUUID();
    }

    public String getId() {
        return id.toString();
    }

}
