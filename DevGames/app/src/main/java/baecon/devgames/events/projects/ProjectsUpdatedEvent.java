package baecon.devgames.events.projects;

import java.util.HashSet;

import baecon.devgames.events.SynchronizableModelUpdatedEvent;

/**
 * Created by Marcel on 10-5-2016.
 */
public class ProjectsUpdatedEvent extends SynchronizableModelUpdatedEvent {
    /**
     * Create a new event the indicates what type of change has been made to one or more instances of the model. Pass
     * {@code null} to parameters you don't use.
     *
     * @param result  The result where the poll procedure finished with.
     * @param removed The list of local model ids that represent the model instances that have been removed from the local
     *                database. Null allowed if none.
     * @param added   The list of local model ids that represent the model instances that have been added to the local
     *                database. Null allowed if none.
     * @param updated
     */
    public ProjectsUpdatedEvent(Integer result, HashSet<Long> removed, HashSet<Long> added, HashSet<Long> updated) {
        super(result, removed, added, updated);
    }
}
