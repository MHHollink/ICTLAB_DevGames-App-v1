package baecon.devgames.events.push;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushScheduledEvent;


public class PushPushScheduledEvent extends PushScheduledEvent {
    public PushPushScheduledEvent(IModelUpdate modelUpdate, boolean success) {
        super(modelUpdate, success);
    }

    public PushPushScheduledEvent(long updateId, long localModelId, boolean success) {
        super(updateId, localModelId, success);
    }
}
