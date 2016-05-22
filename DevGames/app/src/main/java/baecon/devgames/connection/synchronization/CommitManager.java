package baecon.devgames.connection.synchronization;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.connection.client.dto.CommitDTO;
import baecon.devgames.connection.task.poll.ModelPollTask;
import baecon.devgames.connection.task.poll.PollCommitTask;
import baecon.devgames.connection.task.push.ModelPushTask;
import baecon.devgames.connection.task.push.PushCommitTask;
import baecon.devgames.database.modelupdate.Operation;
import baecon.devgames.database.task.SaveCommitTask;
import baecon.devgames.events.commit.CommitPushTaskDoneEvent;
import baecon.devgames.model.Commit;
import baecon.devgames.model.Push;
import baecon.devgames.model.update.CommitUpdate;

/**
 * Created by Marcel on 22-5-2016.
 */
public class CommitManager extends AbsModelManager<Commit, CommitDTO, CommitUpdate, CommitPushTaskDoneEvent> {

    private static CommitManager insance;

    public static CommitManager get(Context context) {
        if (insance == null) {
            insance = new CommitManager(DevGamesApplication.get(context));
        }
        return insance;
    }

    public static CommitManager get(Fragment context) {
        if (insance == null) {
            insance = new CommitManager(DevGamesApplication.get(context));
        }
        return insance;
    }


    private CommitManager(DevGamesApplication app) {
        super(app);
    }

    @Override
    protected ModelPushTask<Commit, CommitUpdate> newUpdateTask(DevGamesApplication app, Long id) {
        return new PushCommitTask(app, id);
    }

    @Override
    protected ModelPollTask<Commit, CommitUpdate, CommitDTO> newPollTask(Context context) {
        return new PollCommitTask(context);
    }

    protected ModelPollTask<Commit, CommitUpdate, CommitDTO> newPollTask(Context context, Push push) {
        return new PollCommitTask(context, push.getId());
    }

    protected ModelPollTask<Commit, CommitUpdate, CommitDTO> newPollTask(Context context, Long localPushId) {
        return new PollCommitTask(context, localPushId);
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
    public void create(Commit commit) {
        new SaveCommitTask(getApplication(), Operation.CREATE, commit);
    }

    @Override
    public void update(Commit commit) {
        new SaveCommitTask(getApplication(), Operation.UPDATE, commit);
    }

    @Override
    public void update(Long id, String field, Serializable value) {
        new SaveCommitTask(getApplication(), id, field, value);
    }

    @Override
    public void delete(Commit commit) {
        new SaveCommitTask(getApplication(), Operation.DELETE, commit);
    }
}
