package baecon.devgames.events.user;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushScheduledEvent;

public class UserPushScheduledEvent extends PushScheduledEvent {

    public UserPushScheduledEvent(long updateId, long localModelId, boolean success) {
        super(updateId, localModelId, success);
    }

    public UserPushScheduledEvent(IModelUpdate modelUpdate, boolean success) {
        super(modelUpdate, success);
    }
}
