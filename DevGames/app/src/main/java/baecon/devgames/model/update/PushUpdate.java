package baecon.devgames.model.update;

import android.content.Context;

import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;

import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.database.DBHelper;
import baecon.devgames.model.Push;
import retrofit.client.Response;

/**
 * Created by Marcel on 07-5-2016.
 */
@DatabaseTable(tableName = DBHelper.Tables.PUSH_UPDATE)
public class PushUpdate extends AbsModelUpdate<Push> {

    public PushUpdate() {
        // Empty constructor for ORMLite
    }

    public PushUpdate(Push push) {
        super(push);
    }

    public PushUpdate(long id, Push push) {
        super(id, push);
    }

    public PushUpdate(long uuid, String field, Serializable value) {
        super(uuid, field, value);
    }

    @Override
    public Push getModelFromDb(DBHelper dbHelper) throws SQLException {
        return DBHelper.getPushDao(dbHelper).queryForId(getId());
    }

    @Override
    public Response sync(Context context, DBHelper dbHelper, DevGamesClient client) throws Exception {
        throw new RuntimeException("Not implemented");
    }
}
