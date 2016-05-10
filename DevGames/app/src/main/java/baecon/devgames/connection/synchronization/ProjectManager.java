package baecon.devgames.connection.synchronization;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.connection.client.dto.ProjectDTO;
import baecon.devgames.connection.task.poll.ModelPollTask;
import baecon.devgames.connection.task.poll.PollProjectTask;
import baecon.devgames.connection.task.push.ModelPushTask;
import baecon.devgames.connection.task.push.PushProjectTask;
import baecon.devgames.database.modelupdate.Operation;
import baecon.devgames.database.task.SaveProjectTask;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.projects.ProjectPushTaskDoneEvent;
import baecon.devgames.model.Project;
import baecon.devgames.model.User;
import baecon.devgames.model.update.AbsModelUpdate;
import baecon.devgames.model.update.ProjectUpdate;
import baecon.devgames.util.L;

/**
 * Created by Marcel on 09-5-2016.
 */
public class ProjectManager extends AbsModelManager<Project, ProjectDTO, ProjectUpdate, ProjectPushTaskDoneEvent> {

    private static ProjectManager instance;

    public static ProjectManager get(Context context) {
        if (instance == null) {
            instance = new ProjectManager(DevGamesApplication.get(context));
        }
        return instance;
    }

    public static ProjectManager get(Fragment fragment) {
        if (instance == null) {
            instance = new ProjectManager(DevGamesApplication.get(fragment));
        }
        return instance;
    }

    protected ProjectManager(DevGamesApplication app) {
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
    protected ModelPushTask<Project, ProjectUpdate> newUpdateTask(DevGamesApplication app, Long id) {
        return new PushProjectTask(app, id); // TODO: 09-5-2016
    }

    @Override
    protected ModelPollTask<Project, ProjectUpdate, ProjectDTO> newPollTask(Context context) {
        return new PollProjectTask(context); // TODO: 09-5-2016
    }

    protected ModelPollTask<Project, ProjectUpdate, ProjectDTO> newPollTask(Context context, User user) {
        return new PollProjectTask(context, user.getId());
    }

    protected ModelPollTask<Project, ProjectUpdate, ProjectDTO> newPollTask(Context context, Long localUserId) {
        return new PollProjectTask(context, localUserId);
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
        return 0;
    }

    @Override
    public boolean isAllowedToSyncInBackground() {
        return false;
    }

    @Override
    public void create(Project project) {
        new SaveProjectTask(getApplication(), Operation.CREATE, project).executeThreaded();
    }

    @Override
    public void update(Project project) {
        new SaveProjectTask(getApplication(), Operation.UPDATE, project).executeThreaded();
    }

    @Override
    public void update(Long id, String field, Serializable value) {
        new SaveProjectTask(getApplication(), id, field, value);
    }

    @Override
    public void delete(Project project) {
        new SaveProjectTask(getApplication(), Operation.DELETE, project).executeThreaded();
    }
}
