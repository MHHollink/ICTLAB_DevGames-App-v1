package baecon.devgames.database.loader;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.PushScheduledEvent;
import baecon.devgames.events.SynchronizableModelUpdatedEvent;
import baecon.devgames.model.ISynchronizable;
import baecon.devgames.model.update.AbsModelUpdate;
import baecon.devgames.ui.fragment.ModelListFragment;
import baecon.devgames.util.L;
import baecon.devgames.util.SortOption;
import baecon.devgames.util.Utils;

/**
 * Created by Marcel on 31-3-2016.
 */
public abstract class SynchronizableModelListLoader
        <Model extends ISynchronizable, ModelUpdateClass extends IModelUpdate>
        extends ModelListLoader<Model, Long> {

    protected HashSet<Long> ids;

    protected SynchronizableModelListLoader(Context context) {
        super(context);
    }

    protected SynchronizableModelListLoader(Context context, SortOption<Model> sortOption, ModelListFragment.FilterOption filterOption,
                                            String searchQuery) {
        super(context, sortOption, filterOption, searchQuery);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        BusProvider.getBus().register(this);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();

        try {
            BusProvider.getBus().unregister(this);
        }
        catch (Exception e) {
            L.e(e, "Could not unregister from event Bus");
        }
    }

    @Override
    public List<Model> loadInBackground() {

        List<Model> items = super.loadInBackground();

        if (items != null) {

            if (ids == null) {
                ids = new HashSet<>(items.size());
            }
            else {
                ids.clear();
            }

            for (Model item : items) {
                ids.add(item.getId());
            }
        }
        else {
            ids = null;
        }

        return items;
    }

    /**
     * Supply the update Dao that is related to the table from the {@link Model}
     *
     * @param dbHelper
     *         The DBHelper to get access to the DAO
     *
     * @return the Dao that is related to the table from the {@link Model}.
     */
    protected abstract Dao<ModelUpdateClass, Long> getUpdateDao(DBHelper dbHelper);

    protected boolean hasUpdatesLeftForModel(long localModelId) throws SQLException {
        return getUpdateDao(getDbHelper()).queryBuilder()
                .where()
                .eq(AbsModelUpdate.Column.ID, localModelId)
                .countOf() != 0;
    }

    /**
     * Determines whether a model has a blocking error
     *
     * @param localModelId
     *         The local id of the model
     *
     * @return True when the model with the local model id has an update that has a blocking error
     *
     * @throws java.sql.SQLException
     *         Thrown when something went wrong while executing the query
     */
    protected boolean hasBlockingError(long localModelId) throws SQLException {

        Dao<ModelUpdateClass, Long> updateDao = getUpdateDao(getDbHelper());
        Where<ModelUpdateClass, Long> where = updateDao.queryBuilder().where();

        // Should produce the following WHERE clause (column names are not the correct ones, just to give an idea):
        // WHERE local_model_id={0} AND blocking_status_code IS NOT NULL OR blocking_reason NOT NULL )
        long count =
                where
                        .eq(AbsModelUpdate.Column.LOCAL_ID, localModelId)
                        .and()
                        .gt(AbsModelUpdate.Column.BLOCKING_ERROR_HTTP_STATUS_CODE, 0)
                        .countOf();

        //L.v( "count where blocking={0}, count total={1}, return={2}", count, updateDao.countOf(), count != 0 );

        return count != 0;
    }

    /**
     * <p>All {@link ISynchronizable}s have events (derived from {@link PushScheduledEvent} and {@link
     * SynchronizableModelUpdatedEvent}). The implementing party should subscribe for those events. Then, pass the event
     * on to this method, which will decide if {@link #onContentChanged()} should be called.</p>
     *
     * <p>This class keeps track of which {@code Synchronizable}s are loaded. If the {@link
     * PushScheduledEvent#localModelId} matches one of the items that was loaded last time, {@link #onContentChanged()}
     * will be called.</p>
     *
     * <p>Note: if {@link #isStarted()} returns false, the function will just return and check nothing.</p>
     *
     * @param event
     *         The event for your model where you're subscribed to
     */
    protected void checkForContentChanged(PushScheduledEvent event) {

        // Only do this when the Loader is started. Otherwise we'd cause errors.
        if (isStarted() && Utils.isNotEmpty(ids) && ids.contains(event.localModelId)) {
            onContentChanged();
        }
    }

    /**
     * <p>All {@link ISynchronizable}s have events (derived from {@link PushScheduledEvent} and {@link
     * SynchronizableModelUpdatedEvent}). The implementing party should subscribe for those events. Then, pass the event
     * on to this method, which will decide if {@link #onContentChanged()} should be called.</p>
     *
     * <p>This class keeps track of which {@code Synchronizable}s are loaded. {@link #onContentChanged()} will be invoked
     * in the following cases:</p>
     * <ol>
     * <li>{@link SynchronizableModelUpdatedEvent#hasNewItems()} == true</li>
     * <li>{@link SynchronizableModelUpdatedEvent#hasRemovedItems()} == true</li>
     * <li>{@link SynchronizableModelUpdatedEvent#hasUpdatedItems()} == true <strong>AND</strong> the list of the most
     * recent loaded items contains at least one of the updated items.</li>
     * </ol>
     *
     * <p>Note: if {@link #isStarted()} returns false, the function will just return and check nothing.</p>
     *
     * @param event
     *         The event for your model where you're subscribed to
     */
    protected void checkForContentChanged(SynchronizableModelUpdatedEvent event) {

        // Only do this when the Loader is started. Otherwise we'd cause errors.
        if (isStarted()) {

            /**
             * Update if:
             * 1. Items were added to the data set
             * 2. Items were removed from the data set
             * 3. Items were updated in the data set AND we are displaying that data
             */

            if (event.hasNewItems()
                    || event.hasRemovedItems()
                    || (event.hasUpdatedItems() && collectionContainsAny(ids, event.getUpdated()))) {
                onContentChanged();
            }
        }
    }

    /**
     * Returns whether {@code a} contains any item in {@code b}.
     * {@code a || b} returns false.
     * {@code a.size() == 0 || b.size()}, returns false.
     *
     * @param a
     *         First collection
     * @param b
     *         Second collection
     *
     * @return whether {@code a} contains any item in {@code b}.
     */
    private boolean collectionContainsAny(Collection a, Collection b) {

        if (a == null || b == null) {
            return false;
        }

        if (a.size() == 0 || b.size() == 0) {
            return false;
        }

        for (Object o : a) {
            if (b.contains(o)) {
                return true;
            }
        }

        return false;
    }
}
