package baecon.devgames.events.commit;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushScheduledEvent;


public class CommitPushScheduledEvent extends PushScheduledEvent {

    public CommitPushScheduledEvent(IModelUpdate modelUpdate, boolean success) {
        super(modelUpdate, success);
    }

    public CommitPushScheduledEvent(long updateId, long localModelId, boolean success) {
        super(updateId, localModelId, success);
    }
}
