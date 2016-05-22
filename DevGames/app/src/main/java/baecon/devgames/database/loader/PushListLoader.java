package baecon.devgames.database.loader;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.squareup.otto.Subscribe;

import baecon.devgames.database.DBHelper;
import baecon.devgames.events.push.PushPushScheduledEvent;
import baecon.devgames.events.push.PushesUpdatedEvent;
import baecon.devgames.model.Push;
import baecon.devgames.model.update.PushUpdate;
import baecon.devgames.ui.fragment.ModelListFragment;
import baecon.devgames.util.SortOption;

/**
 * Created by Marcel on 20-5-2016.
 */
public class PushListLoader extends SynchronizableModelListLoader<Push, PushUpdate> {

    public PushListLoader(Context context) {
        super(context);
    }

    public PushListLoader(Context context, SortOption<Push> sortOption, ModelListFragment.FilterOption filterOption, String searchQuery) {
        super(context, sortOption, filterOption, searchQuery);
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
