package baecon.devgames.events;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.model.update.UserUpdate;

/**
 * An event indicating that a {@link IModelUpdate} has been successfully scheduled for
 * synchronization with the back-end.
 */
public abstract class PushScheduledEvent {

    /**
     * The id of this update, generated by the database.
     */
    public final long updateId;

    /**
     * The local database id of the model where this update is related to.
     */
    public final long localModelId;

    /**
     *
     */
    public final boolean success;

    public PushScheduledEvent(long updateId, long localModelId, boolean success) {
        this.updateId = updateId;
        this.localModelId = localModelId;
        this.success = success;
    }

    public PushScheduledEvent(final IModelUpdate modelUpdate, boolean success) {
        this.updateId = modelUpdate.getId();
        this.localModelId = modelUpdate.getLocalId();
        this.success = success;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "updateId=" + updateId +
                ", localModelId=" + localModelId +
                ", success=" + success +
                '}';
    }
}
