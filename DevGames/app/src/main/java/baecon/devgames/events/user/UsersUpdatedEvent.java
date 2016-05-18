package baecon.devgames.events.user;

import java.util.HashSet;

import baecon.devgames.events.SynchronizableModelUpdatedEvent;

public class UsersUpdatedEvent extends SynchronizableModelUpdatedEvent {

    public UsersUpdatedEvent(Integer result, HashSet<Long> removed, HashSet<Long> added, HashSet<Long> updated) {
        super(result, removed, added, updated);
    }
}
