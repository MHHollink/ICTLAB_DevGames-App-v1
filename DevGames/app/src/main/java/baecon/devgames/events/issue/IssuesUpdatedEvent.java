package baecon.devgames.events.issue;

import java.util.HashSet;

import baecon.devgames.events.SynchronizableModelUpdatedEvent;


public class IssuesUpdatedEvent extends SynchronizableModelUpdatedEvent {

    public IssuesUpdatedEvent(Integer result, HashSet<Long> removed, HashSet<Long> added, HashSet<Long> updated) {
        super(result, removed, added, updated);
    }
}
