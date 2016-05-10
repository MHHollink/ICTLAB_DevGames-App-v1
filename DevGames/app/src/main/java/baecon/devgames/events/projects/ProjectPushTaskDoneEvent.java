package baecon.devgames.events.projects;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushTaskDoneEvent;
import retrofit.client.Response;

/**
 * Created by Marcel on 09-5-2016.
 */
public class ProjectPushTaskDoneEvent extends PushTaskDoneEvent {

    public ProjectPushTaskDoneEvent(long localModelId, boolean success) {
        super(localModelId, success);
    }

    public ProjectPushTaskDoneEvent(long localModelId, boolean success, int statusCode) {
        super(localModelId, success, statusCode);
    }

    public ProjectPushTaskDoneEvent(long localModelId, boolean success, int statusCode, IModelUpdate lastUnsuccessfulUpdate, Response lastUnsuccessfulUpdateResponse) {
        super(localModelId, success, statusCode, lastUnsuccessfulUpdate, lastUnsuccessfulUpdateResponse);
    }


}
