package baecon.devgames.events.issue;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushDoneEvent;

/**
 * TODO: write class level documentation.
 *
 *
 * @author Marcel
 * @since 22-5-2016.
 */
public class IssuePushDoneEvent extends PushDoneEvent {
    public IssuePushDoneEvent(IModelUpdate update) {
        super(update);
    }

    public IssuePushDoneEvent(long updateId, long localModelId) {
        super(updateId, localModelId);
    }
}
