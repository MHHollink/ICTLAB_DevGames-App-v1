package baecon.devgames.model.update;

import android.content.Context;

import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;

import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.database.DBHelper;
import baecon.devgames.model.Duplication;
import retrofit.client.Response;

/**
 * Created by Marcel on 07-5-2016.
 */
@DatabaseTable(tableName = DBHelper.Tables.DUPLICATION_UPDATE)
public class DuplicationUpdate extends AbsModelUpdate<Duplication> {

    public DuplicationUpdate() {
    }

    public DuplicationUpdate(Duplication duplication) {
        super(duplication);
    }

    public DuplicationUpdate(long id, Duplication duplication) {
        super(id, duplication);
    }

    public DuplicationUpdate(long uuid, String field, Serializable value) {
        super(uuid, field, value);
    }

    @Override
    public Duplication getModelFromDb(DBHelper dbHelper) throws SQLException {
        return DBHelper.getDuplicationDao(dbHelper).queryForId(getId());
    }

    @Override
    public Response sync(Context context, DBHelper dbHelper, DevGamesClient client) throws Exception {
        return null; // todo
    }
}
