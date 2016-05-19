package baecon.devgames.events.push;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushTaskDoneEvent;
import retrofit.client.Response;


public class PushPushTaskDoneEvent extends PushTaskDoneEvent {
    public PushPushTaskDoneEvent(long localModelId, boolean success) {
        super(localModelId, success);
    }

    public PushPushTaskDoneEvent(long localModelId, boolean success, int statusCode) {
        super(localModelId, success, statusCode);
    }

    public PushPushTaskDoneEvent(long localModelId, boolean success, int statusCode, IModelUpdate lastUnsuccessfulUpdate, Response lastUnsuccessfulUpdateResponse) {
        super(localModelId, success, statusCode, lastUnsuccessfulUpdate, lastUnsuccessfulUpdateResponse);
    }
}
