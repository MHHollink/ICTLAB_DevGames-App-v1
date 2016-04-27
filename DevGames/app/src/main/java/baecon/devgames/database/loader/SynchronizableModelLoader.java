package baecon.devgames.database.loader;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;

import baecon.devgames.database.DBHelper;
import baecon.devgames.database.modelupdate.IModelUpdate;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.PushScheduledEvent;
import baecon.devgames.events.SynchronizableModelUpdatedEvent;
import baecon.devgames.model.ISynchronizable;
import baecon.devgames.model.update.AbsModelUpdate;
import baecon.devgames.util.L;


public abstract class SynchronizableModelLoader<Model extends ISynchronizable,
        ModelUpdateClass extends IModelUpdate> extends ModelLoader<Model, Long> {

    public SynchronizableModelLoader(Context context, Long localModelId) {
        super(context, localModelId);
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
        } catch (Exception e) {
            L.e(e, "Could not unregister from event Bus");
        }
    }

    /**
     * Supply the update Dao that is related to the table from the {@link Model}
     *
     * @param dbHelper The DBHelper to get access to the DAO
     * @return the Dao that is related to the table from the {@link Model}.
     */
    protected abstract Dao<ModelUpdateClass, Long> getUpdateDao(DBHelper dbHelper);

    protected boolean hasUpdatesLeftForModel(long localModelId) throws SQLException {
        return getUpdateDao(getDbHelper()).queryBuilder()
                .where()
                .eq(AbsModelUpdate.Column.LOCAL_ID, localModelId)
                .countOf() != 0;
    }

    /**
     * Determines whether a model has a blocking error
     *
     * @param localModelId The local id of the model
     * @return True when the model with the local model id has an update that has a blocking error
     * @throws java.sql.SQLException Thrown when something went wrong while executing the query
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
     * <p>All {@link ISynchronizable}s have events (derived from {@link baecon.devgames.events.PushScheduledEvent} and {@link
     * baecon.devgames.events.SynchronizableModelUpdatedEvent}). The implementing party should subscribe for those events. Then, pass the event
     * on to this method, which will decide if {@link #onContentChanged()} should be called.</p>
     * <p>
     * <p>This class keeps track of which {@code Synchronizable} is loaded. If the {@link
     * baecon.devgames.events.PushScheduledEvent#localModelId} matches one the most recent loaded item, {@link #onContentChanged()}
     * will be called.</p>
     * <p>
     * <p>Note: if {@link #isStarted()} returns false, the function will just return and check nothing.</p>
     *
     * @param event The event for your model where you're subscribed to
     */
    protected void checkForContentChanged(PushScheduledEvent event) {

        // Only do this when the Loader is started. Otherwise we'd cause errors.
        if (isStarted()
                && item != null
                && item.getId() == event.localModelId) {
            onContentChanged();
        }
    }

    /**
     * <p>All {@link ISynchronizable}s have events (derived from {@link PushScheduledEvent} and {@link
     * SynchronizableModelUpdatedEvent}). The implementing party should subscribe for those events. Then, pass the event
     * on to this method, which will decide if {@link #onContentChanged()} should be called.</p>
     * <p>
     * <p>This class keeps track of which {@code Synchronizable}s are loaded. {@link #onContentChanged()} will be invoked
     * in the following cases:</p>
     * <ol>
     * <li>When {@code event} has updated items and one of the updated items matches the local id of {@link #item}</li>
     * </ol>
     * <p>
     * <p>Note: if {@link #isStarted()} returns false, the function will just return and check nothing.</p>
     *
     * @param event The event for your model where you're subscribed to
     */
    protected void checkForContentChanged(SynchronizableModelUpdatedEvent event) {

        // Only do this when the Loader is started. Otherwise we'd cause errors.
        if (isStarted()) {

            /**
             * Update when {@code event} has updated items and one of the updated items matches the local id of {@link #item}
             */

            if (event.hasUpdatedItems()
                    && item != null
                    && item.getId() != null
                    && event.getUpdated().contains(item.getId())) {
                onContentChanged();
            }
        }
    }
}
