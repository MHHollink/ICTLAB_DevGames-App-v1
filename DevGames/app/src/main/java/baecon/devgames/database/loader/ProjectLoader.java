package baecon.devgames.database.loader;


import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.squareup.otto.Subscribe;

import baecon.devgames.database.DBHelper;
import baecon.devgames.events.projects.ProjectPushScheduledEvent;
import baecon.devgames.events.projects.ProjectsUpdatedEvent;
import baecon.devgames.model.Project;
import baecon.devgames.model.update.ProjectUpdate;

/**
 * Created by Marcel on 19-5-2016.
 */
public class ProjectLoader extends SynchronizableModelLoader<Project, ProjectUpdate> {


    public ProjectLoader(Context context, Long localModelId) {
        super(context, localModelId);
    }

    @Override
    protected Dao<ProjectUpdate, Long> getUpdateDao(DBHelper dbHelper) {
        return DBHelper.getProjectUpdateDao(getDbHelper());
    }

    @Override
    protected Dao<Project, Long> getDao(DBHelper dbHelper) {
        return DBHelper.getProjectDao(getDbHelper());
    }


    @Subscribe
    public void onUpdatedEvent(ProjectsUpdatedEvent event) {
        checkForContentChanged(event);
    }

    @Subscribe
    public void onEvent(ProjectPushScheduledEvent event) {
        checkForContentChanged(event);
    }

}
