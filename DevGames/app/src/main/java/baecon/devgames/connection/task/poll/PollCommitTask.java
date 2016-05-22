package baecon.devgames.connection.task.poll;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.connection.client.dto.CommitDTO;
import baecon.devgames.connection.synchronization.CommitManager;
import baecon.devgames.database.DBHelper;
import baecon.devgames.events.SynchronizableModelUpdatedEvent;
import baecon.devgames.events.commit.CommitsUpdatedEvent;
import baecon.devgames.model.Commit;
import baecon.devgames.model.Push;
import baecon.devgames.model.User;
import baecon.devgames.model.update.CommitUpdate;
import baecon.devgames.util.L;

public class PollCommitTask extends ModelPollTask<Commit, CommitUpdate, CommitDTO> {

    private Long userId;
    private Dao<User, Long> userDao;
    private Dao<Push, Long> pushDao;

    public PollCommitTask(Context context) {
        this(context, null);
    }

    /**
     * Creates a new instance of this REST task.
     *
     * @param context       The context from which this task was created.
     * @param userId        The id of the user you want to get the commits for
     */
    public PollCommitTask(Context context, Long userId) {
        super(context, CommitManager.get(context));
        this.userId = userId;
    }

    @Override
    protected SynchronizableModelUpdatedEvent getUpdatedEvent(Integer result, HashSet<Long> removed, HashSet<Long> added, HashSet<Long> updated) {
        return new CommitsUpdatedEvent(result, removed, added, updated);
    }

    @Override
    protected List<CommitDTO> doPoll(DevGamesClient client) throws SQLException {
        List<User> users = null;
        List<CommitDTO> commitDTOs = new ArrayList<>();

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

                List<Push> pushes = pushDao.queryBuilder()
                        .orderBy(Push.Column.TIMESTAMP, false)
                        .where()
                        .eq(Push.Column.PUSHER, u)
                        .query();

                if(pushes != null) {
                    for (Push push : pushes) {

                        List<CommitDTO> temp = client.getCommitsFromPush(push.getId());

                        if (temp != null) {
                            commitDTOs.addAll(temp);
                        }
                    }
                }
                else  {
                    L.w("Got 0 pushes! Cannot retrieve commits");
                }
            }
        }
        else {
            L.w("Got 0 users! Cannot retrieve pushes");
        }

        return commitDTOs;
    }

    @Override
    protected Commit dtoToModel(CommitDTO dto) {
        return dto != null ? dto.toModel() : null;
    }

    @Override
    protected Dao<Commit, Long> getModelDao() {
        return DBHelper.getCommitDao(getDbHelper());
    }

    @Override
    protected Dao<CommitUpdate, Long> getModelUpdateDao() {
        return DBHelper.getCommitUpdateDao(getDbHelper());
    }
}
