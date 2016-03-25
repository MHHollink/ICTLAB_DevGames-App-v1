package baecon.devgames.events.user;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushDoneEvent;

public class UserPushDoneEvent extends PushDoneEvent {

    public UserPushDoneEvent(IModelUpdate update) {
        super(update);
    }

    public UserPushDoneEvent(long updateId, long localUpdateId) {
        super(updateId, localUpdateId);
    }
}
