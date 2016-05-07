package baecon.devgames.model.update;

import android.content.Context;

import java.io.Serializable;
import java.sql.SQLException;

import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.connection.client.dto.PushDTO;
import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.ModelUpdate;
import baecon.devgames.model.Push;
import retrofit.client.Response;

/**
 * Created by Marcel on 07-5-2016.
 */
public class PushUpdate extends AbsModelUpdate<Push> {

    /**
     * Empty constructor for ORMLite
     */
    public PushUpdate() {
    }

    public PushUpdate(Push push) {
        super(push);
    }

    /**
     * Create an {@linkplain baecon.devgames.database.modelupdate.EntireModelUpdate}.
     *
     * @param id
     *         The local id (that was generated by the local database) for the actual {@link Push}
     * @param push
     *         The instance of the {@link Push} that should be synchronized
     */
    public PushUpdate(long id, Push push) {
        super(id, push);
    }

    /**
     * Create a {@link baecon.devgames.database.modelupdate.SingleFieldModelUpdate}.
     *
     * @param uuid
     *         The local id (that was generated by the local database) for the actual {@link Push}
     * @param field
     *         The column name of the field that has to be updated
     * @param value
     *         The value of the field that has to be updated.
     */
    public PushUpdate(long uuid, String field, Serializable value) {
        super(uuid, field, value);
    }

    @Override
    public Push getModelFromDb(DBHelper dbHelper) throws SQLException {
        return DBHelper.getPushDao(dbHelper).queryForId(getId());
    }

    @Override
    public Response sync(Context context, DBHelper dbHelper, DevGamesClient client) throws Exception {
        return null; // todu
    }
}
