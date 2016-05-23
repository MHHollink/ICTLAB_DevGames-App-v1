package baecon.devgames.database.task;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.io.Serializable;

import baecon.devgames.connection.synchronization.IssueManager;
import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.Operation;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.issue.IssuePushScheduledEvent;
import baecon.devgames.model.Issue;
import baecon.devgames.model.update.IssueUpdate;
import baecon.devgames.util.L;

/**
 * TODO: write class level documentation.
 *
 * @author Marcel
 * @since 23-5-2016.
 */
public class SaveIssueTask extends ModelCUDTask<Issue, IssueUpdate> {

    public SaveIssueTask(Context context, Long id, String field, Serializable value) {
        super(context, id, field, value);
    }

    public SaveIssueTask(Context context, Operation operation, Issue issue) {
        super(context, operation, issue);
    }

    @Override
    protected Dao<Issue, Long> getModelDao() {
        return DBHelper.getIssueDao(getDbHelper());
    }

    @Override
    protected Dao<IssueUpdate, Long> getModelUpdateDao() {
        return DBHelper.getIssueUpdateDao(getDbHelper());
    }

    @Override
    protected IssueUpdate generateModelUpdate(Operation operation, Issue issue) {
        return new IssueUpdate(issue);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        L.v("{0}", result);

        if (getModelUpdate() != null && result != null) {
            IssueManager.get(context).offerUpdate(getModelUpdate());
            BusProvider.getBus().post(new IssuePushScheduledEvent(getModelUpdate(), result == UPDATED));
        }
        else {
            L.w("IssueUpdate not scheduled! Issue was null or a local database error occurred");
        }
    }
}
