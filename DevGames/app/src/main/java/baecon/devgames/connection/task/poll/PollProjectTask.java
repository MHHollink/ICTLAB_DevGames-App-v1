package baecon.devgames.connection.task.poll;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.connection.client.dto.ProjectDTO;
import baecon.devgames.connection.synchronization.ProjectManager;
import baecon.devgames.database.DBHelper;
import baecon.devgames.events.projects.ProjectsUpdatedEvent;
import baecon.devgames.model.Project;
import baecon.devgames.model.User;
import baecon.devgames.model.update.ProjectUpdate;
import baecon.devgames.util.L;

public class PollProjectTask extends ModelPollTask<Project, ProjectUpdate, ProjectDTO>{

    private Long userId;
    private Dao<User, Long> userDao;

    /**
     * Creates a PollProjectsTask that will poll for <strong>ALL projects of ALL users</strong>. This is a very heavy
     * operation, and probably not needed. Consider fetching projects for one user at a time.
     *
     * @param context Context to access network and database
     */
    public PollProjectTask(Context context) {
        this(context, null);
    }

    /**
     *
     * @param context
     * @param userId
     */
    public PollProjectTask(Context context, Long userId) {
        super(context, ProjectManager.get(context));
        this.userId = userId;

        userDao = DBHelper.getUserDao(getDbHelper());
    }

    @Override
    protected ProjectsUpdatedEvent getUpdatedEvent(Integer result,
                                                   HashSet<Long> removed,
                                                   HashSet<Long> added,
                                                   HashSet<Long> updated)
    {
        return new ProjectsUpdatedEvent(result, removed, added, updated);
    }

    @Override
    protected List<ProjectDTO> doPoll(DevGamesClient client) throws SQLException {
        List<User> users = null;
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        try {
            if (userId != null) {
                User user = userDao.queryForId(userId);
                if (user != null) {
                    users = new ArrayList<>();
                    users.add(user);
                }
            } else {
                users = userDao.queryForAll();
            }
        } catch (SQLException e) {
            L.e(e, "Something went wrong while retrieving the users");
            return null;
        }

        if (users != null) {
            for (User u : users) {
                List<ProjectDTO> temp = client.getProjectsOfUser(u.getId());

                if (temp != null) {
                    projectDTOs.addAll(temp);
                }
            }
        } else {
            L.w("Got 0 users! Cannot retrieve projects");
        }

        return projectDTOs;
    }

    @Override
    protected Project dtoToModel(ProjectDTO dto) {
        return dto != null ? dto.toModel() : null;
    }

    @Override
    protected Dao<Project, Long> getModelDao() {
        return DBHelper.getProjectDao(getDbHelper());
    }

    @Override
    protected Dao<ProjectUpdate, Long> getModelUpdateDao() {
        return DBHelper.getProjectUpdateDao(getDbHelper());
    }
}
