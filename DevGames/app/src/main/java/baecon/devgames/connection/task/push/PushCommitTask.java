package baecon.devgames.connection.task.push;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushDoneEvent;
import baecon.devgames.events.PushTaskDoneEvent;
import baecon.devgames.events.commit.CommitPushDoneEvent;
import baecon.devgames.events.commit.CommitPushTaskDoneEvent;
import baecon.devgames.model.Commit;
import baecon.devgames.model.update.CommitUpdate;
import retrofit.client.Response;


public class PushCommitTask extends ModelPushTask<Commit, CommitUpdate> {

    protected static ModelPushEventFactory eventFactory = new ModelPushEventFactory() {
        @Override
        PushTaskDoneEvent pushTaskDoneEvent(long id, boolean success) {
            return new CommitPushTaskDoneEvent(id, success);
        }

        @Override
        PushTaskDoneEvent pushTaskDoneEvent(long id, boolean success, int statusCode) {
            return new CommitPushTaskDoneEvent(id, success, statusCode);
        }

        @Override
        PushTaskDoneEvent pushTaskDoneEvent(long id, boolean success, int statusCode, IModelUpdate lastUnsuccessfulUpdate, Response lastUnsuccessfulUpdateResponse) {
            return new CommitPushTaskDoneEvent(id, success, statusCode, lastUnsuccessfulUpdate, lastUnsuccessfulUpdateResponse);
        }

        @Override
        PushDoneEvent pushDoneEvent(long updateId, long id) {
            return new CommitPushDoneEvent(updateId, id);
        }

        @Override
        PushDoneEvent pushDoneEvent(IModelUpdate modelUpdate) {
            return new CommitPushDoneEvent(modelUpdate);
        }
    };
    
    /**
     * Create a new ModelUpdateTask that will synchronize a queue of updates of a model to the back-end.
     *
     * @param context The context
     * @param id
     */
    public PushCommitTask(Context context, Long id) {
        super(context, id);
    }

    @Override
    protected Dao<Commit, Long> getModelDao() {
        return DBHelper.getCommitDao(getDbHelper());
    }

    @Override
    protected Dao<CommitUpdate, Long> getModelUpdateDao() {
        return DBHelper.getCommitUpdateDao(getDbHelper());
    }

    @Override
    protected ModelPushEventFactory getModelPushEventFactory() {
        return eventFactory;
    }
}
