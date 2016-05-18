package baecon.devgames.connection.synchronization;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.squareup.otto.Subscribe;

import java.io.Serializable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.connection.client.dto.UserDTO;
import baecon.devgames.connection.task.poll.ModelPollTask;
import baecon.devgames.connection.task.poll.PollRelatedUsersTask;
import baecon.devgames.connection.task.poll.PollUserTask;
import baecon.devgames.connection.task.push.PushUserTask;
import baecon.devgames.database.modelupdate.Operation;
import baecon.devgames.database.task.SaveUserTask;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.user.UserPushTaskDoneEvent;
import baecon.devgames.model.User;
import baecon.devgames.model.update.UserUpdate;
import baecon.devgames.util.AsyncTask;
import baecon.devgames.util.L;

public class UserManager extends AbsModelManager<User, UserDTO, UserUpdate, UserPushTaskDoneEvent>{

    private static UserManager instance;

    public static UserManager get(Context context) {
        if (instance == null) {
            instance = new UserManager(DevGamesApplication.get(context));
        }
        return instance;
    }

    public static UserManager get(Fragment fragment) {
        if (instance == null) {
            instance = new UserManager(DevGamesApplication.get(fragment));
        }
        return instance;
    }

    protected UserManager(DevGamesApplication app) {
        super(app);
    }

    @Override
    public void init() {
        super.init();
        L.d("Hello world!");
        BusProvider.getBus().register(this);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        L.d("Goodbye world!");
        BusProvider.getBus().unregister(this);
    }

    public void pollUnknownUser(final Long userId) {
        getUIThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                new PollUserTask(getApplication(), userId).executeThreaded();
            }
        });
    }

    @Override
    protected PushUserTask newUpdateTask(DevGamesApplication app, Long id) {
        return new PushUserTask(app, id);
    }

    @Override
    protected ModelPollTask<User, UserUpdate, UserDTO> newPollTask(Context context) {
        return new PollRelatedUsersTask(context, this);
    }

    @Override
    protected Executor getPollExecutor() {
        return AsyncTask.SERIAL_EXECUTOR;
    }

    @Override
    public long getBackgroundPollingInterval() {
        return 0; // We do not poll
    }

    @Override
    public long getForegroundPollingInterval() {
        return TimeUnit.MINUTES.toMillis(10L);
    }

    @Override
    public boolean isAllowedToSyncInBackground() {
        return true;
    }

    @Override
    public boolean isInForegroundSyncMode() {
        return true;
    }

    @Override
    public void create(User user) {
        new SaveUserTask(getApplication(), Operation.CREATE, user).executeThreaded();
    }

    @Override
    public void update(User user) {
        new SaveUserTask(getApplication(), Operation.UPDATE, user).executeThreaded();
    }

    @Override
    public void update(Long id, String field, Serializable value) {
        new SaveUserTask(getApplication(), id, field, value).executeThreaded();
    }

    @Override
    public void delete(User user) {
        new SaveUserTask(getApplication(), Operation.DELETE, user).executeThreaded();
    }

    @Subscribe
    public void onUpdateTaskDoneEvent(UserPushTaskDoneEvent event) {
        super.onUpdateTaskDoneEvent(event);
    }
}
