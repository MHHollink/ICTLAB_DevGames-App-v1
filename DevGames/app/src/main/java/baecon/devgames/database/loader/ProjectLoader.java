package baecon.devgames.database.loader;


import android.content.Context;

import com.j256.ormlite.dao.Dao;

import baecon.devgames.database.DBHelper;
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
        return null;
    }

    @Override
    protected Dao<Project, Long> getDao(DBHelper dbHelper) {
        return null;
    }
}
