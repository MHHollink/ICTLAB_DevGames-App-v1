package baecon.devgames.database.loader;

import android.content.Context;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.util.List;

import baecon.devgames.database.DBHelper;
import baecon.devgames.events.projects.ProjectPushScheduledEvent;
import baecon.devgames.events.projects.ProjectsUpdatedEvent;
import baecon.devgames.model.Project;
import baecon.devgames.model.update.ProjectUpdate;
import baecon.devgames.ui.fragment.ModelListFragment;
import baecon.devgames.util.L;
import baecon.devgames.util.SortOption;


public class ProjectListLoader extends SynchronizableModelListLoader<Project, ProjectUpdate> {
    public ProjectListLoader(Context context) {
        super(context);
    }

    public ProjectListLoader(Context context, SortOption<Project> sortOption, ModelListFragment.FilterOption filterOption, String searchQuery) {
        super(context, sortOption, filterOption, searchQuery);
    }

    @Override
    protected List<Project> query(QueryBuilder<Project, Long> queryBuilder) throws SQLException {
        if (TextUtils.isEmpty(searchQuery)) {
            return super.query(queryBuilder);
        }
        else {
            queryBuilder
                    .where()
                    .like(Project.Column.NAME, "%" + searchQuery + "%")
                    .or()
                    .like(Project.Column.DESCRIPTION, "%" + searchQuery + "%")
                    .query();
            L.d("query={0}", queryBuilder.prepareStatementString());
            return queryBuilder.query();
        }
    }

    @Override
    protected Dao<ProjectUpdate, Long> getUpdateDao(DBHelper dbHelper) {
        return DBHelper.getProjectUpdateDao(dbHelper);
    }

    @Override
    protected Dao<Project, Long> getDao(DBHelper dbHelper) {
        return DBHelper.getProjectDao(dbHelper);
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
