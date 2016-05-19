package baecon.devgames.connection.synchronization;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.connection.client.dto.PushDTO;
import baecon.devgames.connection.task.poll.ModelPollTask;
import baecon.devgames.connection.task.poll.PollPushTask;
import baecon.devgames.connection.task.push.ModelPushTask;
import baecon.devgames.connection.task.push.PushPushTask;
import baecon.devgames.database.modelupdate.Operation;
import baecon.devgames.database.task.SavePushTask;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.push.PushPushTaskDoneEvent;
import baecon.devgames.model.Push;
import baecon.devgames.model.update.PushUpdate;
import baecon.devgames.util.L;

public class PushManager extends AbsModelManager<Push, PushDTO, PushUpdate, PushPushTaskDoneEvent> {

    private static PushManager instance;

    public static PushManager get(Context context) {
        if (instance == null) {
            instance = new PushManager(DevGamesApplication.get(context));
        }
        return instance;
    }

    public static PushManager get(Fragment fragment) {
        if (instance == null) {
            instance = new PushManager(DevGamesApplication.get(fragment));
        }
        return instance;
    }

    protected PushManager(DevGamesApplication app) {
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
    @Override
    protected ModelPushTask<Push, PushUpdate> newUpdateTask(DevGamesApplication app, Long id) {
        return new PushPushTask(app, id);
    }

    @Override
    protected ModelPollTask<Push, PushUpdate, PushDTO> newPollTask(Context context) {
        return new PollPushTask(context);
    }

    protected ModelPollTask<Push, PushUpdate, PushDTO> newPollTask(Context context, long userId) {
        return new PollPushTask(context, userId);
    }

    @Override
    public long getBackgroundPollingInterval() {
        return 0;
    }

    @Override
    public long getForegroundPollingInterval() {
        return TimeUnit.MINUTES.toMillis(30L);
    }

    @Override
    public boolean isAllowedToSyncInBackground() {
        return false;
    }

    @Override
    public void create(Push push) {
        new SavePushTask(getApplication(), Operation.CREATE, push).executeThreaded();
    }

    @Override
    public void update(Push push) {
        new SavePushTask(getApplication(), Operation.UPDATE, push).executeThreaded();
    }

    @Override
    public void update(Long id, String field, Serializable value) {
        new SavePushTask(getApplication(), id, field, value).executeThreaded();

    }

    @Override
    public void delete(Push push) {
        new SavePushTask(getApplication(), Operation.DELETE, push).executeThreaded();
    }
}
