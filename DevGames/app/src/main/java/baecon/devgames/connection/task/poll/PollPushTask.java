package baecon.devgames.connection.task.poll;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.connection.client.dto.PushDTO;
import baecon.devgames.connection.synchronization.PushManager;
import baecon.devgames.database.DBHelper;
import baecon.devgames.events.push.PushesUpdatedEvent;
import baecon.devgames.model.Push;
import baecon.devgames.model.User;
import baecon.devgames.model.update.PushUpdate;
import baecon.devgames.util.L;

public class PollPushTask extends ModelPollTask<Push, PushUpdate, PushDTO>{
    private Long userId;
    private Dao<User, Long> userDao;
    private Dao<Push, Long> pushDao;

    /**
     * Creates a PollProjectsTask that will poll for <strong>ALL projects of ALL users</strong>. This is a very heavy
     * operation, and probably not needed. Consider fetching projects for one user at a time.
     *
     * @param context Context to access network and database
     */
    public PollPushTask(Context context) {
        this(context, null);
    }

    public PollPushTask(Context context, Long userId) {
        super(context, PushManager.get(context));
        this.userId = userId;

        userDao = DBHelper.getUserDao(getDbHelper());
        pushDao = DBHelper.getPushDao(getDbHelper());
    }

    @Override
    protected PushesUpdatedEvent getUpdatedEvent(Integer result,
                                                   HashSet<Long> removed,
                                                   HashSet<Long> added,
                                                   HashSet<Long> updated)
    {
        return new PushesUpdatedEvent(result, removed, added, updated);
    }

    @Override
    protected List<PushDTO> doPoll(DevGamesClient client) throws SQLException {
        List<PushDTO> pushDTOs = new ArrayList<>();
        List<User> users = null;

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

                // The latest push that is local will be used for timestamp.
                // We only want to retrieve the latest pair of pushes
                // Pushes never change, so why get all pushes - every time
                Push push = pushDao.queryBuilder()
                        .orderBy(Push.Column.TIMESTAMP, false)
                        .where()
                        .eq(Push.Column.PUSHER, u)
                        .queryForFirst();

                List<PushDTO> temp = client.getPushesOfUser(u.getId());

                if (temp != null) {
                    pushDTOs.addAll(temp);
                }
            }
        } else {
            L.w("Got 0 users! Cannot retrieve pushes");
        }

        return pushDTOs;
    }

    @Override
    protected Push dtoToModel(PushDTO dto) {
        return dto != null ? dto.toModel() : null;
    }

    @Override
    protected Dao<Push, Long> getModelDao() {
        return DBHelper.getPushDao(getDbHelper());
    }

    @Override
    protected Dao<PushUpdate, Long> getModelUpdateDao() {
        return DBHelper.getPushUpdateDao(getDbHelper());
    }
}
