package baecon.devgames.connection.synchronization;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.connection.client.dto.DuplicationDTO;
import baecon.devgames.connection.task.poll.ModelPollTask;
import baecon.devgames.connection.task.push.ModelPushTask;
import baecon.devgames.events.duplication.DuplicationPushTaskDoneEvent;
import baecon.devgames.model.Duplication;
import baecon.devgames.model.update.DuplicationUpdate;

/**
 * TODO: write class level documentation.
 *
 * @author Marcel
 * @since 23-5-2016.
 */
public class DuplicationManager extends AbsModelManager<Duplication, DuplicationDTO, DuplicationUpdate, DuplicationPushTaskDoneEvent> {

    private static DuplicationManager instance;

    public static DuplicationManager get(Context context) {
        if (instance == null) {
            instance = new DuplicationManager(DevGamesApplication.get(context));
        }
        return instance;
    }

    public static DuplicationManager get(Fragment context) {
        if (instance == null) {
            instance = new DuplicationManager(DevGamesApplication.get(context));
        }
        return instance;
    }

    protected DuplicationManager(DevGamesApplication app) {
        super(app);
    }

    @Override
    protected ModelPushTask<Duplication, DuplicationUpdate> newUpdateTask(DevGamesApplication app, Long id) {
        return null; // TODO: 23-5-2016
    }

    @Override
    protected ModelPollTask<Duplication, DuplicationUpdate, DuplicationDTO> newPollTask(Context context) {
        return null; // TODO: 23-5-2016
    }

    @Override
    public long getBackgroundPollingInterval() {
        return 0; // TODO: 23-5-2016
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
    public void create(Duplication duplication) {
        // TODO: 23-5-2016
    }

    @Override
    public void update(Duplication duplication) {
        // TODO: 23-5-2016
    }

    @Override
    public void update(Long id, String field, Serializable value) {
        // TODO: 23-5-2016
    }

    @Override
    public void delete(Duplication duplication) {
        // TODO: 23-5-2016
    }
}
