package baecon.devgames.events.issue;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushTaskDoneEvent;
import retrofit.client.Response;


public class IssuePushTaskDoneEvent extends PushTaskDoneEvent {

    public IssuePushTaskDoneEvent(long localModelId, boolean success) {
        super(localModelId, success);
    }

    public IssuePushTaskDoneEvent(long localModelId, boolean success, int statusCode) {
        super(localModelId, success, statusCode);
    }

    public IssuePushTaskDoneEvent(long localModelId, boolean success, int statusCode, IModelUpdate lastUnsuccessfulUpdate, Response lastUnsuccessfulUpdateResponse) {
        super(localModelId, success, statusCode, lastUnsuccessfulUpdate, lastUnsuccessfulUpdateResponse);
    }
}
