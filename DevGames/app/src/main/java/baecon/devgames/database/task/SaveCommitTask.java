package baecon.devgames.database.task;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.io.Serializable;

import baecon.devgames.connection.synchronization.CommitManager;
import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.Operation;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.commit.CommitPushScheduledEvent;
import baecon.devgames.model.Commit;
import baecon.devgames.model.update.CommitUpdate;
import baecon.devgames.util.L;

/**
 * TODO: write class level documentation.
 *
 * @author Marcel
 * @since 22-5-2016.
 */
public class SaveCommitTask extends ModelCUDTask<Commit, CommitUpdate> {

    public SaveCommitTask(Context context, Long id, String field, Serializable value) {
        super(context, id, field, value);
    }

    public SaveCommitTask(Context context, Operation operation, Commit commit) {
        super(context, operation, commit);
    }

    @Override
    protected Dao<Commit, Long> getModelDao() {
        return DBHelper.getCommitDao(getDbHelper());
    }

    @Override
    protected Dao<CommitUpdate, Long> getModelUpdateDao() {
        return DBHelper.getCommitUpdateDao(getDbHelper());
    }

    @Override
    protected CommitUpdate generateModelUpdate(Operation operation, Commit commit) {
        return new CommitUpdate(commit);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        L.v("{0}", result);

        if (getModelUpdate() != null && result != null) {
            CommitManager.get(context).offerUpdate(getModelUpdate());
            BusProvider.getBus().post(new CommitPushScheduledEvent(getModelUpdate(), result == UPDATED));
        }
        else {
            L.w("CommitUpdate not scheduled! Commit was null or a local database error occurred");
        }
    }
}
