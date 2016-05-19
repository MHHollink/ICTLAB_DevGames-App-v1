package baecon.devgames.events.push;

import java.util.HashSet;

import baecon.devgames.events.SynchronizableModelUpdatedEvent;

public class PushUpdatedEvent extends SynchronizableModelUpdatedEvent {

    public PushUpdatedEvent(Integer result, HashSet<Long> removed, HashSet<Long> added, HashSet<Long> updated) {
        super(result, removed, added, updated);
    }
}
