package baecon.devgames.model.update;

import android.content.Context;

import java.io.Serializable;
import java.sql.SQLException;

import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.database.DBHelper;
import baecon.devgames.model.Issue;
import retrofit.client.Response;

/**
 * Created by Marcel on 07-5-2016.
 */
public class IssueUpdate extends AbsModelUpdate<Issue> {

    public IssueUpdate() {
    }

    public IssueUpdate(Issue issue) {
        super(issue);
    }

    public IssueUpdate(long id, Issue issue) {
        super(id, issue);
    }

    public IssueUpdate(long uuid, String field, Serializable value) {
        super(uuid, field, value);
    }

    @Override
    public Issue getModelFromDb(DBHelper dbHelper) throws SQLException {
        return DBHelper.getIssueDao(dbHelper).queryForId(getId());
    }

    @Override
    public Response sync(Context context, DBHelper dbHelper, DevGamesClient client) throws Exception {
        return null; // todo
    }
}
