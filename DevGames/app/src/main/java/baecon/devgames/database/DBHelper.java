package baecon.devgames.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ReferenceObjectCache;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import baecon.devgames.R;
import baecon.devgames.model.Commit;
import baecon.devgames.model.Duplication;
import baecon.devgames.model.Issue;
import baecon.devgames.model.Project;
import baecon.devgames.model.Push;
import baecon.devgames.model.Setting;
import baecon.devgames.model.User;
import baecon.devgames.model.update.CommitUpdate;
import baecon.devgames.model.update.DuplicationUpdate;
import baecon.devgames.model.update.IssueUpdate;
import baecon.devgames.model.update.ProjectUpdate;
import baecon.devgames.model.update.PushUpdate;
import baecon.devgames.model.update.UserUpdate;
import baecon.devgames.util.Utils;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    public static final String TAG = DBHelper.class.getName();

    private static final String DATABASE_NAME = "devgames.db";

    private static final int DATABASE_VERSION = 12;

    private static int APP_VERSION_CODE;

    private Map<Class, Dao> daoMap = null;

    public DBHelper(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);

        daoMap = new HashMap<>();

        int versionCode = 0;
        if (APP_VERSION_CODE == 0) {
            APP_VERSION_CODE = Utils.getAppVersion(context);
        }
        APP_VERSION_CODE = versionCode;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.d(TAG, "onCreate: #");

        try {
            for (Class<?> tableClass : DatabaseConfigUtil.CLASSES) {
                Log.d(TAG, "onCreate: creating a table for class: "+ tableClass.getName());
                TableUtils.createTable(connectionSource, tableClass);
            }

            // Add some default values.
            this.getDao(Setting.class, String.class).create(new Setting(Setting.USERNAME, ""));
            this.getDao(Setting.class, String.class).create(new Setting(Setting.USER_ID, ""));
            this.getDao(Setting.class, String.class).create(new Setting(Setting.SESSION_ID, ""));
        }
        catch (SQLException e) {
            Log.e(TAG, "onCreate: Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: #");

        HashSet<String> oldTables = new HashSet<>();
        oldTables.addAll(
                Arrays.asList(
                        "users",
                        "commits",
                        "projects"
                )
        );

        for (String tableName : oldTables) {
            try {
                Log.i(TAG, "onUpgrade: Deleting table "+ tableName);
                database.execSQL("DROP TABLE IF EXISTS " + tableName);
                Log.i(TAG, "onUpgrade: Table "+tableName+" deleted");
            }
            catch (android.database.SQLException e) {
                Log.e(TAG, "onUpgrade: Could not delete table "+ tableName, e);
            }
        }

        onCreate(database, connectionSource);
    }

    /**
     * Calls {@link com.j256.ormlite.dao.Dao#clearObjectCache()} on all cached Dao objects.
     */
    public void cleanUpObjectCache() {

        Log.i(TAG, "cleanUpObjectCache: #");

        if (daoMap != null && daoMap.size() != 0) {
            for (Dao dao : daoMap.values()) {
                if (dao != null) {
                    dao.clearObjectCache();
                }
            }

        }
    }

    @SuppressWarnings("unchecked")
    public <M, I> Dao<M, I> getDao(Class<M> modelClass, Class<I> idClass) {

        Dao<M, I> dao = daoMap.get(modelClass);

        if (dao == null) {

            try {
                // Create a new DAO.
                dao = getDao(modelClass);
                dao.setObjectCache(new DevGamesReferenceObjectCache(true));

                // Put the DAO in the cached map.
                daoMap.put(modelClass, dao);

            }
            catch (SQLException e) {
                Log.e(TAG, "getDao: Could not create a DAO for: "+ modelClass, e);
            }
        }

        return dao;
    }

    static class DevGamesReferenceObjectCache extends ReferenceObjectCache {

        /**
         * @param useWeak
         *         Set to true if you want the cache to use {@link java.lang.ref.WeakReference}. If false then the cache
         *         will use {@link java.lang.ref.SoftReference}.
         */
        public DevGamesReferenceObjectCache(boolean useWeak) {
            super(useWeak);
        }

    }
    public static Dao<User, Long> getUserDao(DBHelper dbHelper) {
        return dbHelper.getDao(User.class, Long.class);
    }

    public static Dao<UserUpdate,Long> getUserUpdateDao(DBHelper dbHelper) {
        return dbHelper.getDao(UserUpdate.class, Long.class);
    }


    public static Dao<Project, Long> getProjectDao(DBHelper dbHelper) {
        return dbHelper.getDao(Project.class, Long.class);
    }

    public static Dao<ProjectUpdate, Long> getProjectUpdateDao(DBHelper dbHelper) {
        return dbHelper.getDao(ProjectUpdate.class, Long.class);
    }


    public static Dao<Commit, Long> getCommitDao(DBHelper dbHelper) {
        return dbHelper.getDao(Commit.class, Long.class);
    }

    public static Dao<CommitUpdate, Long> getCommitUpdateDao(DBHelper dbHelper) {
        return dbHelper.getDao(CommitUpdate.class, Long.class);
    }


    public static Dao<Push, Long> getPushDao(DBHelper dbHelper) {
        return dbHelper.getDao(Push.class, Long.class);
    }

    public static Dao<PushUpdate, Long> getPushUpdateDao(DBHelper dbHelper) {
        return dbHelper.getDao(PushUpdate.class, Long.class);
    }


    public static Dao<Issue, Long> getIssueDao(DBHelper dbHelper) {
        return dbHelper.getDao(Issue.class, Long.class);
    }

    public static Dao<IssueUpdate, Long> getIssueUpdateDao(DBHelper dbHelper) {
        return dbHelper.getDao(IssueUpdate.class, Long.class);
    }


    public static Dao<Duplication, Long> getDuplicationDao(DBHelper dbHelper) {
        return dbHelper.getDao(Duplication.class, Long.class);
    }

    public static Dao<DuplicationUpdate, Long> getDuplicationUpdateDao(DBHelper dbHelper) {
        return dbHelper.getDao(DuplicationUpdate.class, Long.class);
    }


    public static Dao<Setting, String> getSettingDao(DBHelper dbHelper) {
        return dbHelper.getDao(Setting.class, String.class);
    }

}
