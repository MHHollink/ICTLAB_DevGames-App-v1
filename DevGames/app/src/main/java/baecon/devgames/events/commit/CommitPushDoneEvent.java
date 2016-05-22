package baecon.devgames.events.commit;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushDoneEvent;

/**
 * TODO: write class level documentation.
 *
 *
 * @author Marcel
 * @since 22-5-2016.
 */
public class CommitPushDoneEvent extends PushDoneEvent {
    public CommitPushDoneEvent(IModelUpdate update) {
        super(update);
    }

    public CommitPushDoneEvent(long updateId, long localModelId) {
        super(updateId, localModelId);
    }
}
