package baecon.devgames.connection.task.push;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.PushDoneEvent;
import baecon.devgames.events.PushTaskDoneEvent;
import baecon.devgames.events.projects.ProjectPushDoneEvent;
import baecon.devgames.events.projects.ProjectPushTaskDoneEvent;
import baecon.devgames.model.Project;
import baecon.devgames.model.update.ProjectUpdate;
import retrofit.client.Response;

/**
 * Created by Marcel on 09-5-2016.
 */
public class PushProjectTask extends ModelPushTask<Project, ProjectUpdate> {

    protected static ModelPushEventFactory eventFactory = new ModelPushEventFactory() {
        @Override
        PushTaskDoneEvent pushTaskDoneEvent(long id, boolean success) {
            return new ProjectPushTaskDoneEvent(id, success);
        }

        @Override
        PushTaskDoneEvent pushTaskDoneEvent(long id, boolean success, int statusCode) {
            return new ProjectPushTaskDoneEvent(id, success, statusCode);
        }

        @Override
        PushTaskDoneEvent pushTaskDoneEvent(long id, boolean success, int statusCode, IModelUpdate lastUnsuccessfulUpdate, Response lastUnsuccessfulUpdateResponse) {
            return new ProjectPushTaskDoneEvent(id, success, statusCode, lastUnsuccessfulUpdate, lastUnsuccessfulUpdateResponse);
        }

        @Override
        PushDoneEvent pushDoneEvent(long updateId, long id) {
            return new ProjectPushDoneEvent(updateId, id);
        }

        @Override
        PushDoneEvent pushDoneEvent(IModelUpdate modelUpdate) {
            return new ProjectPushDoneEvent(modelUpdate);
        }
    };

    /**
     * Create a new ModelUpdateTask that will synchronize a queue of updates of a model to the back-end.
     *
     * @param context The context
     * @param id
     */
    public PushProjectTask(Context context, Long id) {
        super(context, id);
    }

    @Override
    protected Dao<Project, Long> getModelDao() {
        return DBHelper.getProjectDao(getDbHelper());
    }

    @Override
    protected Dao<ProjectUpdate, Long> getModelUpdateDao() {
        return DBHelper.getProjectUpdateDao(getDbHelper());
    }

    @Override
    protected ModelPushEventFactory getModelPushEventFactory() {
        return eventFactory;
    }
}
