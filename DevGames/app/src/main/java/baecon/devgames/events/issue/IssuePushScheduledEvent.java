package baecon.devgames.events.issue;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushScheduledEvent;


public class IssuePushScheduledEvent extends PushScheduledEvent {

    public IssuePushScheduledEvent(IModelUpdate modelUpdate, boolean success) {
        super(modelUpdate, success);
    }

    public IssuePushScheduledEvent(long updateId, long localModelId, boolean success) {
        super(updateId, localModelId, success);
    }
}
