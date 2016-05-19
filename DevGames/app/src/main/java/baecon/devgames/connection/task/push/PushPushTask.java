package baecon.devgames.connection.task.push;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.push.PushPushDoneEvent;
import baecon.devgames.events.push.PushPushTaskDoneEvent;
import baecon.devgames.model.Push;
import baecon.devgames.model.update.PushUpdate;
import retrofit.client.Response;

/**
 * Created by Marcel on 19-5-2016.
 */
public class PushPushTask extends ModelPushTask<Push, PushUpdate> {


    protected static ModelPushEventFactory eventFactory = new ModelPushEventFactory() {
        @Override
        PushPushTaskDoneEvent pushTaskDoneEvent(long uuid, boolean success) {
            return new PushPushTaskDoneEvent(uuid, success);
        }

        @Override
        PushPushTaskDoneEvent pushTaskDoneEvent(long uuid, boolean success, int statusCode) {
            return new PushPushTaskDoneEvent(uuid, success, statusCode);
        }

        @Override
        PushPushTaskDoneEvent pushTaskDoneEvent(long uuid, boolean success, int statusCode, IModelUpdate lastUnsuccessfulUpdate, Response lastUnsuccessfulUpdateResponse) {
            return new PushPushTaskDoneEvent(uuid, success, statusCode, lastUnsuccessfulUpdate, lastUnsuccessfulUpdateResponse);
        }

        @Override
        PushPushDoneEvent pushDoneEvent(long updateId, long uuid) {
            return new PushPushDoneEvent(updateId, uuid);
        }

        @Override
        PushPushDoneEvent pushDoneEvent(IModelUpdate modelUpdate) {
            return new PushPushDoneEvent(modelUpdate);
        }
    };
    /**
     * Create a new ModelUpdateTask that will synchronize a queue of updates of a model to the back-end.
     *
     * @param context The context
     * @param id
     */
    public PushPushTask(Context context, Long id) {
        super(context, id);
    }

    @Override
    protected Dao<Push, Long> getModelDao() {
        return DBHelper.getPushDao(getDbHelper());
    }

    @Override
    protected Dao<PushUpdate, Long> getModelUpdateDao() {
        return DBHelper.getPushUpdateDao(getDbHelper());

    }

    @Override
    protected ModelPushEventFactory getModelPushEventFactory() {
        return null;
    }
}
