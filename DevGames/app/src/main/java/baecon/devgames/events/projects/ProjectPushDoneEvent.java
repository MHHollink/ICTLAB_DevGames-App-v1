package baecon.devgames.events.projects;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushDoneEvent;

/**
 * Created by Marcel on 19-5-2016.
 */
public class ProjectPushDoneEvent extends PushDoneEvent {
    public ProjectPushDoneEvent(IModelUpdate update) {
        super(update);
    }

    public ProjectPushDoneEvent(long updateId, long localModelId) {
        super(updateId, localModelId);
    }
}
