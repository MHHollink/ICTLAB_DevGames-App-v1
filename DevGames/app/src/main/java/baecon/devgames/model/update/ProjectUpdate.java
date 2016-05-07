package baecon.devgames.model.update;

import android.content.Context;

import java.io.Serializable;
import java.sql.SQLException;

import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.database.DBHelper;
import baecon.devgames.model.Project;
import retrofit.client.Response;

/**
 * Created by Marcel on 07-5-2016.
 */
public class ProjectUpdate extends AbsModelUpdate<Project> {

    public ProjectUpdate() {
    }

    public ProjectUpdate(Project project) {
        super(project);
    }

    public ProjectUpdate(long id, Project project) {
        super(id, project);
    }

    public ProjectUpdate(long uuid, String field, Serializable value) {
        super(uuid, field, value);
    }

    @Override
    public Project getModelFromDb(DBHelper dbHelper) throws SQLException {
        return DBHelper.getProjectDao(dbHelper).queryForId(getId());
    }

    @Override
    public Response sync(Context context, DBHelper dbHelper, DevGamesClient client) throws Exception {
        return null;
    }
}
