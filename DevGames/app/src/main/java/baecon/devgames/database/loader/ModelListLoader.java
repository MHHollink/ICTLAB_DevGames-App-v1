package baecon.devgames.database.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.database.DBHelper;
import baecon.devgames.ui.fragment.ModelListFragment;
import baecon.devgames.ui.widget.ModelListAdapter;
import baecon.devgames.util.L;
import baecon.devgames.util.SortOption;

/**
 * An {@link android.support.v4.content.AsyncTaskLoader} that loads all records from the {@link Model} table.
 * <p>
 * Implement the following methods:
 * <ul>
 * <li>{@link #getDao(DBHelper)} to supply the right Dao to fetch the data from</li>
 * </ul>
 * Optional:
 * <ul>
 * <li>{@link #query(QueryBuilder)} to adapt the QueryBuilder to your needs</li>
 * <li>{@link #processResult(java.util.List)} to edit the results before they get delivered</li>
 * </ul>
 * </p>
 * <p>
 * Use with:
 * <ul>
 * <li>{@link ModelListFragment}</li>
 * <li>{@link ModelListAdapter}</li>
 * </ul>
 * </p>
 */
public abstract class ModelListLoader<Model, Id> extends AsyncTaskLoader<List<Model>> {

    /**
     * The chosen sort option by the user in the {@link ModelListFragment}. May be null.
     */
    public final SortOption<Model> sortOption;

    /**
     * The chosen filter option by the user in the {@link ModelListFragment}. May be null.
     */
    public final ModelListFragment.FilterOption filterOption;

    /**
     * The search query, most of the time given in by the user at the search field in the ActionBar. May be null.
     */
    public final String searchQuery;

    protected List<Model> items;

    /**
     * Create a new FilterableModelListLoader without any filters
     *
     * @param context
     *         The context to access the database
     */
    public ModelListLoader(Context context) {
        this(context, null, null, null);
    }

    /**
     * Create a new FilterableModelListLoader with a {@link SortOption}, a {@link ModelListFragment.FilterOption} and a filter value. It is
     * up to the developer to implement the {@link #query(com.j256.ormlite.stmt.QueryBuilder)} method. Default uses {@link
     * ModelListLoader#query(com.j256.ormlite.stmt.QueryBuilder)}.
     *
     * @param context
     *         The context to access the database
     * @param sortOption
     *         The chosen sort option by the user in the {@link ModelListFragment}. Null allowed.
     * @param filterOption
     *         The chosen filter option by the user in the {@link ModelListFragment}. Null allowed.
     * @param searchQuery
     *         The search query, most of the time given in by the user at the search field in the ActionBar.
     *         May be null.
     */
    protected ModelListLoader(Context context, SortOption<Model> sortOption, ModelListFragment.FilterOption filterOption, String searchQuery) {
        super(context);
        this.sortOption = sortOption;
        this.filterOption = filterOption;
        this.searchQuery = searchQuery;
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
     * Gives the ability to make changes to the query that will be executed to load items. This method is executed
     * within the {@link #loadInBackground()}. If not touched, all rows will be fetched.
     *
     * @param queryBuilder
     *
     * @return By default all records from the table
     */
    protected List<Model> query(QueryBuilder<Model, Id> queryBuilder) throws SQLException {

        // Returns all
        return queryBuilder.query();
    }

    /**
     * To be used to make modifications to the list (can be null!) that will be delivered by the Loader. This method is
     * called right after the database call is finished. It runs within the bounds of the {@link #loadInBackground()}.
     * Default implementation is just to return the {@code fromDatabase}.
     *
     * @param fromDatabase
     *         The list of {@link Model} that came from the database. Can be null.
     *
     * @return The modified list of {@link Model}
     */
    protected List<Model> processResult(List<Model> fromDatabase) {
        return fromDatabase;
    }

    @Override
    public List<Model> loadInBackground() {

        try {

            // Load the list of items
            List<Model> temp = query(getDao(getDbHelper()).queryBuilder());

            // Process the list of items so it can be delivered
            items = new ArrayList<>(temp != null ? temp.size() : 0);
            items = processResult(temp);
        }
        catch (SQLException e) {
            L.e(e, "something went wrong loading the data from the database: {0}", e.getMessage());
        }

        // Return the processed result
        return items;
    }

    @Override
    protected void onStartLoading() {

        if (items != null && items.size() > 0) {
            //Return the data we already got
            deliverResult(items);
        }
        else {
            forceLoad();
        }
    }

    protected DBHelper getDbHelper() {
        return DevGamesApplication.get(getContext()).getDbHelper();
    }
}