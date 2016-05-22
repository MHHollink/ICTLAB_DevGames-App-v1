package baecon.devgames.database.task;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.io.Serializable;

import baecon.devgames.connection.synchronization.PushManager;
import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.Operation;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.push.PushPushScheduledEvent;
import baecon.devgames.model.Push;
import baecon.devgames.model.update.PushUpdate;
import baecon.devgames.util.L;


public class SavePushTask extends ModelCUDTask<Push, PushUpdate>{

    public SavePushTask(Context context, Operation operation, Push push) {
        super(context, operation, push);
    }

    public SavePushTask(Context context, Long id, String field, Serializable value) {
        super(context, id, field, value);
    }

    @Override
    protected Dao<Push, Long> getModelDao() {
        return DBHelper.getPushDao(getDbHelper());
    }

    @Override
    protected Dao<PushUpdate, Long> getModelUpdateDao() {
        return DBHelper.getPushUpdateDao(getDbHelper());
    }

    @Override
    protected PushUpdate generateModelUpdate(Operation operation, Push push) {
        return new PushUpdate(push);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        L.v("{0}", result);

        if (getModelUpdate() != null && result != null) {
            PushManager.get(context).offerUpdate(getModelUpdate());
            BusProvider.getBus().post(new PushPushScheduledEvent(getModelUpdate(), result == UPDATED));
        }
        else {
            L.w("PushUpdate not scheduled! Push was null or a local database error occurred");
        }
    }
}
