package baecon.devgames.events.push;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushDoneEvent;


public class PushPushDoneEvent extends PushDoneEvent {
    public PushPushDoneEvent(IModelUpdate update) {
        super(update);
    }

    public PushPushDoneEvent(long updateId, long localModelId) {
        super(updateId, localModelId);
    }
}
