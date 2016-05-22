package baecon.devgames.events.commit;

import java.util.HashSet;

import baecon.devgames.events.SynchronizableModelUpdatedEvent;


public class CommitsUpdatedEvent extends SynchronizableModelUpdatedEvent {

    public CommitsUpdatedEvent(Integer result, HashSet<Long> removed, HashSet<Long> added, HashSet<Long> updated) {
        super(result, removed, added, updated);
    }
}
