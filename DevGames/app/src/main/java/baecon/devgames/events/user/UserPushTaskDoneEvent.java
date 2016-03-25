package baecon.devgames.events.user;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushTaskDoneEvent;
import retrofit.client.Response;

public class UserPushTaskDoneEvent extends PushTaskDoneEvent {

    public UserPushTaskDoneEvent(long localModelId, boolean success) {
        super(localModelId, success);
    }

    public UserPushTaskDoneEvent(long localModelId, boolean success, int statusCode) {
        super(localModelId, success, statusCode);
    }

    public UserPushTaskDoneEvent(long localModelId, boolean success, int statusCode, IModelUpdate lastUnsuccessfulUpdate, Response lastUnsuccessfulUpdateResponse) {
        super(localModelId, success, statusCode, lastUnsuccessfulUpdate, lastUnsuccessfulUpdateResponse);
    }
}
