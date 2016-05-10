package baecon.devgames.database.task;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.io.Serializable;
import java.sql.SQLException;

import baecon.devgames.connection.synchronization.ProjectManager;
import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.Operation;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.projects.ProjectPushScheduledEvent;
import baecon.devgames.model.Project;
import baecon.devgames.model.update.ProjectUpdate;
import baecon.devgames.util.L;

/**
 * Created by Marcel on 09-5-2016.
 */
public class SaveProjectTask extends ModelCUDTask<Project, ProjectUpdate> {

    public SaveProjectTask(Context context, Operation operation, Project project) {
        super(context, operation, project);
    }

    public SaveProjectTask(Context context, Long id, String field, Serializable value) {
        super(context, id, field, value);
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
    protected ProjectUpdate generateModelUpdate(Operation operation, Project project) {
        return new ProjectUpdate(project);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        L.v("{0}", result);

        if (getModelUpdate() != null && result != null) {
            ProjectManager.get(context).offerUpdate(getModelUpdate());
            BusProvider.getBus().post(new ProjectPushScheduledEvent(getModelUpdate(), result == UPDATED));
        }
        else {
            L.w("ProjectUpdate not scheduled! Project was null or a local database error occurred");
        }
    }
}
