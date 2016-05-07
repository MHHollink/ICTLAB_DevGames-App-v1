package baecon.devgames.model.update;

import android.content.Context;

import java.io.Serializable;
import java.sql.SQLException;

import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.database.DBHelper;
import baecon.devgames.model.Commit;
import retrofit.client.Response;

/**
 * Created by Marcel on 07-5-2016.
 */
public class CommitUpdate extends AbsModelUpdate<Commit> {

    public CommitUpdate() {
    }

    public CommitUpdate(Commit commit) {
        super(commit);
    }

    public CommitUpdate(long id, Commit commit) {
        super(id, commit);
    }

    public CommitUpdate(long uuid, String field, Serializable value) {
        super(uuid, field, value);
    }

    @Override
    public Commit getModelFromDb(DBHelper dbHelper) throws SQLException {
        return DBHelper.getCommitDao(dbHelper).queryForId(getId());
    }

    @Override
    public Response sync(Context context, DBHelper dbHelper, DevGamesClient client) throws Exception {
        return null; // todo
    }
}
