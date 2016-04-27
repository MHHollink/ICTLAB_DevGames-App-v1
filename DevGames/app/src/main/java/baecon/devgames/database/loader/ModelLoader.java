package baecon.devgames.database.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.database.DBHelper;
import baecon.devgames.util.L;

/**
 * An {@link android.support.v4.content.AsyncTaskLoader} that loads 1 records from the {@link Model} table.
 * <p>
 * Implement the following methods:
 * <ul>
 * <li>{@link #getDao(DBHelper)} to supply the right Dao to fetch the data from</li>
 * </ul>
 * Optional:
 * <ul>
 * <li>{@link #query(com.j256.ormlite.dao.Dao)} to adapt the QueryBuilder to your needs</li>
 * <li>{@link #processResult(Model)} to edit the results before they get delivered</li>
 * </ul>
 * </p>
 * Override {@link #query(com.j256.ormlite.dao.Dao)} to add extra clauses to the SQL.
 */
public abstract class ModelLoader<Model, Id> extends AsyncTaskLoader<Model> {

    protected Model item;
    protected Id id;

    public ModelLoader(Context context, Id id) {
        super(context);
        this.id = id;
    }

    /**
     * Supply the Dao that is related to the table from the {@link Model}.
     *
     * @param dbHelper
     *
     * @return the Dao that is related to the table from the {@link Model}.
     */
    protected abstract Dao<Model, Id> getDao(DBHelper dbHelper);

    /**
     * Gives the ability to make changes to the query that will be executed to load item. This method is executed
     * within the {@link #loadInBackground()}. If not touched, all rows will be fetched.
     *
     * @param dao
     *
     * @return The given dao
     */
    protected Model query(Dao<Model, Id> dao) throws SQLException {
        return id != null ? dao.queryForId(id) : null;
    }

    protected Model processResult(Model fromDatabase) {
        return fromDatabase;
    }

    @Override
    public Model loadInBackground() {

        try {

            // Load the item
            Model temp = query(getDao(getDbHelper()));

            // Process the result so it can be delivered
            item = processResult(temp);
        }
        catch (SQLException e) {
            L.e(e, "something went wrong loading the data from the database: {0}", e.getMessage());
        }

        return item;
    }

    @Override
    protected void onStartLoading() {

        if (item != null) {
            //Return the data we already got
            deliverResult(item);
        }
        else {
            forceLoad();
        }
    }

    protected DBHelper getDbHelper() {
        return DevGamesApplication.get(getContext()).getDbHelper();
    }
}