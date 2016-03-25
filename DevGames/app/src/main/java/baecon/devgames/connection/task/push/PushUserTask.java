package baecon.devgames.connection.task.push;


import android.content.Context;

import com.j256.ormlite.dao.Dao;

import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.user.UserPushDoneEvent;
import baecon.devgames.events.user.UserPushTaskDoneEvent;
import baecon.devgames.model.User;
import baecon.devgames.model.update.UserUpdate;
import retrofit.client.Response;

public class PushUserTask extends ModelPushTask<User, UserUpdate> {

    protected static ModelPushEventFactory eventFactory = new ModelPushEventFactory() {
        @Override
        UserPushTaskDoneEvent pushTaskDoneEvent(long uuid, boolean success) {
            return new UserPushTaskDoneEvent(uuid, success);
        }

        @Override
        UserPushTaskDoneEvent pushTaskDoneEvent(long uuid, boolean success, int statusCode) {
            return new UserPushTaskDoneEvent(uuid, success, statusCode);
        }

        @Override
        UserPushTaskDoneEvent pushTaskDoneEvent(long uuid, boolean success, int statusCode, IModelUpdate lastUnsuccessfulUpdate, Response lastUnsuccessfulUpdateResponse) {
            return new UserPushTaskDoneEvent(uuid, success, statusCode, lastUnsuccessfulUpdate, lastUnsuccessfulUpdateResponse);
        }

        @Override
        UserPushDoneEvent pushDoneEvent(long updateId, long uuid) {
            return new UserPushDoneEvent(updateId, uuid);
        }

        @Override
        UserPushDoneEvent pushDoneEvent(IModelUpdate modelUpdate) {
            return new UserPushDoneEvent(modelUpdate);
        }
    };

    public PushUserTask(Context context, long uuid) {
        super(context, uuid);
    }

    @Override
    protected Dao<User, Long> getModelDao() {
        return DBHelper.getUserDao(getDbHelper());
    }

    @Override
    protected Dao<UserUpdate, Long> getModelUpdateDao() {
        return DBHelper.getUserUpdateDao(getDbHelper());
    }

    @Override
    protected ModelPushEventFactory getModelPushEventFactory() {
        return eventFactory;
    }
}
