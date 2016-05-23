package baecon.devgames.connection.synchronization;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.connection.client.dto.IssueDTO;
import baecon.devgames.connection.task.poll.ModelPollTask;
import baecon.devgames.connection.task.push.ModelPushTask;
import baecon.devgames.events.issue.IssuePushTaskDoneEvent;
import baecon.devgames.model.Issue;
import baecon.devgames.model.update.IssueUpdate;

/**
 * TODO: write class level documentation.
 *
 * @author Marcel
 * @since 23-5-2016.
 */
public class IssueManager extends AbsModelManager<Issue, IssueDTO, IssueUpdate, IssuePushTaskDoneEvent> {
    
    private static IssueManager instance;

    public static IssueManager get(Context context) {
        if (instance == null) {
            instance = new IssueManager(DevGamesApplication.get(context));
        }
        return instance;
    }

    public static IssueManager getInstance(Fragment context) {
        if (instance == null) {
            instance = new IssueManager(DevGamesApplication.get(context));
        }
        return instance;
    }
    
    private IssueManager(DevGamesApplication app) {
        super(app);
    }

    @Override
    protected ModelPushTask<Issue, IssueUpdate> newUpdateTask(DevGamesApplication app, Long id) {
        return null; // TODO: 23-5-2016  
    }

    @Override
    protected ModelPollTask<Issue, IssueUpdate, IssueDTO> newPollTask(Context context) {
        return null; // TODO: 23-5-2016  
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
    public void create(Issue issue) {
        // TODO: 23-5-2016  
    }

    @Override
    public void update(Issue issue) {
        // TODO: 23-5-2016  
    }

    @Override
    public void update(Long id, String field, Serializable value) {
        // TODO: 23-5-2016  
    }

    @Override
    public void delete(Issue issue) {
        // TODO: 23-5-2016  
    }
}
