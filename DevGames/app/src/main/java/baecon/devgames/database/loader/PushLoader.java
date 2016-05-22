package baecon.devgames.database.loader;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.squareup.otto.Subscribe;

import baecon.devgames.database.DBHelper;
import baecon.devgames.events.push.PushPushScheduledEvent;
import baecon.devgames.events.push.PushesUpdatedEvent;
import baecon.devgames.model.Push;
import baecon.devgames.model.update.PushUpdate;

public class PushLoader extends SynchronizableModelLoader<Push, PushUpdate> {

    public PushLoader(Context context, Long localModelId) {
        super(context, localModelId);
    }

    @Override
    protected Dao<PushUpdate, Long> getUpdateDao(DBHelper dbHelper) {
        return DBHelper.getPushUpdateDao(dbHelper);
    }

    @Override
    protected Dao<Push, Long> getDao(DBHelper dbHelper) {
        return DBHelper.getPushDao(getDbHelper());
    }


    @Subscribe
    public void onUpdatedEvent(PushesUpdatedEvent event) {
        checkForContentChanged(event);
    }

    @Subscribe
    public void onEvent(PushPushScheduledEvent event) {
        checkForContentChanged(event);
    }
}
