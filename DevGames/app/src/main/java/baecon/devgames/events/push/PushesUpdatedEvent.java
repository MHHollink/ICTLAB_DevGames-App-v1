package baecon.devgames.events.push;

import java.util.HashSet;

import baecon.devgames.events.SynchronizableModelUpdatedEvent;

public class PushesUpdatedEvent extends SynchronizableModelUpdatedEvent {

    public PushesUpdatedEvent(Integer result, HashSet<Long> removed, HashSet<Long> added, HashSet<Long> updated) {
        super(result, removed, added, updated);
    }
}
