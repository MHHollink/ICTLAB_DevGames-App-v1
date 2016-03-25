package baecon.devgames.events;

import java.util.HashSet;

import baecon.devgames.util.Utils;

public class SynchronizableModelUpdatedEvent {

    /**
     * The result where the poll procedure finished with.
     */
    protected final int result;

    /**
     * The list of local model ids that represent the model instances that have been removed from the local database
     */
    protected final HashSet<Long> removed;

    /**
     * The list of local model ids that represent the model instances that have been added to the local database
     */
    protected final HashSet<Long> added;

    /**
     * The list of local model ids that represent the model instances that have been updated in the local database
     */
    protected final HashSet<Long> updated;

    /**
     * Create a new event the indicates what type of change has been made to one or more instances of the model. Pass
     * {@code null} to parameters you don't use.
     *  @param result
     *         The result where the poll procedure finished with.
     * @param removed
     *         The list of local model ids that represent the model instances that have been removed from the local
     *         database. Null allowed if none.
     * @param added
     *         The list of local model ids that represent the model instances that have been added to the local
     *         database. Null allowed if none.
     * @param updated
     */
    public SynchronizableModelUpdatedEvent(Integer result, HashSet<Long> removed, HashSet<Long> added, HashSet<Long> updated) {
        this.result = result == null ? 0 : result;
        this.removed = removed;
        this.added = added;
        this.updated = updated;
    }

    /**
     * Returns whether this event indicates new instances of this model have been added.
     *
     * @return Whether this event indicates new instances of this model have been added.
     */
    public boolean hasNewItems() {
        return added != null && added.size() != 0;
    }

    /**
     * Returns whether this event indicates new instances of this model have been updated.
     *
     * @return Whether this event indicates new instances of this model have been updated.
     */
    public boolean hasUpdatedItems() {
        return updated != null && updated.size() != 0;
    }

    /**
     * Returns whether this event indicates new instances of this model have been removed.
     *
     * @return Whether this event indicates new instances of this model have been removed.
     */
    public boolean hasRemovedItems() {
        return removed != null && removed.size() != 0;
    }

    /**
     * Returns the list of local model ids that represent the model instances that have been removed from the local
     * database
     *
     * @return The list of local model ids that represent the model instances that have been removed from the local
     * database
     */
    public HashSet<Long> getRemoved() {
        return removed;
    }

    /**
     * Returns the list of local model ids that represent the model instances that have been added to the local database
     *
     * @return The list of local model ids that represent the model instances that have been added to the local database
     */
    public HashSet<Long> getAdded() {
        return added;
    }

    /**
     * Returns the list of local model ids that represent the model instances that have been updated in the local
     * database
     *
     * @return The list of local model ids that represent the model instances that have been updated in the local database
     */
    public HashSet<Long> getUpdated() {
        return updated;
    }

    /**
     * Returns the result code where the poll procedure finished with.
     *
     * @return The result code where the poll procedure finished with
     *
     * @see baecon.devgames.database.task.DBTask#UPDATED
     * @see baecon.devgames.database.task.DBTask#NO_WORK
     * @see baecon.devgames.database.task.DBTask#DB_ERROR
     * @see baecon.devgames.database.task.DBTask#GENERAL_ERROR
     */
    public int getResult() {
        return result;
    }

    @Override
    public String toString() {
        // Use getClass.getSimpleName(), so every subclass doesn't need to override the toString()
        return getClass().getSimpleName() + "{" +
                "removed=" + Utils.collectionToString(removed) +
                ", added=" + Utils.collectionToString(added) +
                ", updated=" + Utils.collectionToString(updated) +
                '}';
    }
}
