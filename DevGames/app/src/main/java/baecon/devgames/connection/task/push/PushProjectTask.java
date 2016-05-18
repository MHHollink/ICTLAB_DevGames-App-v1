package baecon.devgames.connection.task.push;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import baecon.devgames.model.Project;
import baecon.devgames.model.update.ProjectUpdate;

/**
 * Created by Marcel on 09-5-2016.
 */
public class PushProjectTask extends ModelPushTask<Project, ProjectUpdate> {

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
        return null;
    }

    @Override
    protected Dao<ProjectUpdate, Long> getModelUpdateDao() {
        return null;
    }

    @Override
    protected ModelPushEventFactory getModelPushEventFactory() {
        return null;
    }
}
