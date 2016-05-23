package baecon.devgames.database.task;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.io.Serializable;

import baecon.devgames.connection.synchronization.DuplicationManager;
import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.Operation;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.duplication.DuplicationPushScheduledEvent;
import baecon.devgames.model.Duplication;
import baecon.devgames.model.update.DuplicationUpdate;
import baecon.devgames.util.L;

/**
 * TODO: write class level documentation.
 *
 * @author Marcel
 * @since 23-5-2016.
 */
public class SaveDuplicationTask extends ModelCUDTask<Duplication, DuplicationUpdate> {

    public SaveDuplicationTask(Context context, Long id, String field, Serializable value) {
        super(context, id, field, value);
    }

    public SaveDuplicationTask(Context context, Operation operation, Duplication duplication) {
        super(context, operation, duplication);
    }

    @Override
    protected Dao<Duplication, Long> getModelDao() {
        return DBHelper.getDuplicationDao(getDbHelper());
    }

    @Override
    protected Dao<DuplicationUpdate, Long> getModelUpdateDao() {
        return DBHelper.getDuplicationUpdateDao(getDbHelper());
    }

    @Override
    protected DuplicationUpdate generateModelUpdate(Operation operation, Duplication duplication) {
        return new DuplicationUpdate(duplication);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        L.v("{0}", result);

        if (getModelUpdate() != null && result != null) {
            DuplicationManager.get(context).offerUpdate(getModelUpdate());
            BusProvider.getBus().post(new DuplicationPushScheduledEvent(getModelUpdate(), result == UPDATED));
        }
        else {
            L.w("DuplicationUpdate not scheduled! Duplication was null or a local database error occurred");
        }
    }
}
