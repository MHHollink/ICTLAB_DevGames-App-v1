package baecon.devgames.database.loader;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.squareup.otto.Subscribe;

import baecon.devgames.database.DBHelper;
import baecon.devgames.events.user.UserPushScheduledEvent;
import baecon.devgames.events.user.UsersUpdatedEvent;
import baecon.devgames.model.User;
import baecon.devgames.model.update.UserUpdate;

/**
 * Loads a {@link User}.
 */
public class UserLoader extends SynchronizableModelLoader<User, UserUpdate> {

    public UserLoader(Context context, Long userLocalId) {
        super(context, userLocalId);
    }

    @Override
    protected Dao<User, Long> getDao(DBHelper dbHelper) {
        return DBHelper.getUserDao(dbHelper);
    }

    @Override
    protected Dao<UserUpdate, Long> getUpdateDao(DBHelper dbHelper) {
        return DBHelper.getUserUpdateDao(dbHelper);
    }

    @Subscribe
    public void onUpdatedEvent(UsersUpdatedEvent event) {
        checkForContentChanged(event);
    }

    @Subscribe
    public void onEvent(UserPushScheduledEvent event) {
        checkForContentChanged(event);
    }
}