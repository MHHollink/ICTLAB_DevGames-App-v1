package baecon.devgames.model.update;

import android.content.Context;

import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;

import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.database.DBHelper;
import baecon.devgames.model.Commit;
import retrofit.client.Response;

/**
 * Created by Marcel on 07-5-2016.
 */
@DatabaseTable(tableName = DBHelper.Tables.COMMIT_UPDATE)
public class CommitUpdate extends AbsModelUpdate<Commit> {

    public CommitUpdate() {
        // Empty constructor for ORMLite
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
        throw new RuntimeException("Not implemented");
    }
}
