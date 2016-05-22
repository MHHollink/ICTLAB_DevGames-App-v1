package baecon.devgames.events.projects;

import java.util.HashSet;

import baecon.devgames.events.SynchronizableModelUpdatedEvent;

/**
 * Created by Marcel on 10-5-2016.
 */
public class ProjectsUpdatedEvent extends SynchronizableModelUpdatedEvent {

    public ProjectsUpdatedEvent(Integer result, HashSet<Long> removed, HashSet<Long> added, HashSet<Long> updated) {
        super(result, removed, added, updated);
    }
}
