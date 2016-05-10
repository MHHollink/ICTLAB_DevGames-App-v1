package baecon.devgames.events.projects;

import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushScheduledEvent;

/**
 * Created by Marcel on 09-5-2016.
 */
public class ProjectPushScheduledEvent extends PushScheduledEvent {

    public ProjectPushScheduledEvent(IModelUpdate modelUpdate, boolean success) {
        super(modelUpdate, success);
    }

    public ProjectPushScheduledEvent(long updateId, long localModelId, boolean success) {
        super(updateId, localModelId, success);
    }
}
