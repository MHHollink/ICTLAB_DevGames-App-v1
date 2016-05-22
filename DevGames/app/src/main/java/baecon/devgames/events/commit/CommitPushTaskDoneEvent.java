package baecon.devgames.events.commit;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushTaskDoneEvent;
import retrofit.client.Response;


public class CommitPushTaskDoneEvent extends PushTaskDoneEvent {

    public CommitPushTaskDoneEvent(long localModelId, boolean success) {
        super(localModelId, success);
    }

    public CommitPushTaskDoneEvent(long localModelId, boolean success, int statusCode) {
        super(localModelId, success, statusCode);
    }

    public CommitPushTaskDoneEvent(long localModelId, boolean success, int statusCode, IModelUpdate lastUnsuccessfulUpdate, Response lastUnsuccessfulUpdateResponse) {
        super(localModelId, success, statusCode, lastUnsuccessfulUpdate, lastUnsuccessfulUpdateResponse);
    }
}
