package baecon.devgames.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import baecon.devgames.util.Header;
import baecon.devgames.util.HeaderFactory;
import baecon.devgames.util.L;
import baecon.devgames.util.SortOption;

import java.util.Collections;
import java.util.List;

/**
 * An ArrayAdapter that takes care of binding the data from the {@link ModelClass} to a view. Use
 * {@link #setData(List, com.askcs.teamup.util.SortOption)} to set new data.
 * <p>
 * Use with:
 * <ul>
 * <li>{@link com.askcs.teamup.ui.fragment.ModelListFragment}</li>
 * <li>{@link com.askcs.teamup.database.loader.ModelListLoader}</li>
 * </ul>
 * </p>
 *
 * A ListAdapter that is able to display headers in a list, based on the {@link com.askcs.teamup.util.SortOption}.
 * Override {@link #calculateHeaderNeed(int, Object, Object)} and call {@link #needsHeader(int)} in {@link #getView(int,
 * View, ViewGroup)} to know whether a header should be displayed. This class assumes you have some header layout in
 * each entry (see <a href="http://cyrilmottier.com/2011/07/05/listview-tips-tricks-2-section-your-listview/">http://cyrilmottier.com/2011/07/05/listview-tips-tricks-2-section-your-listview/</a>).
 *
 * @param <ModelClass>
 *         The type of the ORMLite model class that represents a table in the local database
 */
public abstract class ModelListAdapter<ModelClass> extends ArrayAdapter<ModelClass> {

    private boolean headersEnabled = true;
    private final SortOption<ModelClass> defaultSortOption;
    private SortOption<ModelClass> currentSortOption;

    public ModelListAdapter(Context context) {
        this(context, null);
    }

    public ModelListAdapter(Context context, SortOption<ModelClass> defaultSortOption) {
        super(context, 0);
        this.defaultSortOption = defaultSortOption;
    }

    public boolean headersEnabled() {
        return headersEnabled;
    }

    public ModelListAdapter<ModelClass> setHeadersEnabled(boolean headersEnabled) {
        this.headersEnabled = headersEnabled;
        return this;
    }

    /**
     * Clears the current list of data and adds the supplied list of data. {@link #notifyDataSetChanged()} is called
     * after adding the data.
     *
     * @param data
     *         The new data
     */
    public void setData(List<ModelClass> data, SortOption<ModelClass> applySortOption) {

        L.v("setData");

        // clear() calls notifyDataSetChanged(), which means that the ListView is rendered again with 0 elements.
        // That means, the ListView scrolls to the top. Right after that, the new data is added. The ListView is smart and
        // sees somehow that the data is somehow the same (as Romain Guy claims at
        // https://www.youtube.com/watch?v=wDBM6wVEO70#t=52m25s ) and it will keep the scroll position
        setNotifyOnChange(false);

        // Clear all current elements
        clear();

        // If the applySortOption == null, apply the default one
        this.currentSortOption = applySortOption != null ? applySortOption : defaultSortOption;

        if (data != null && data.size() != 0 && currentSortOption != null && currentSortOption.getComparator() != null) {
            Collections.sort(data, currentSortOption.getComparator());
        }

        L.v("data size: {0}, using {1} -> {2}", data != null ? data.size() : 0,
                currentSortOption != null ? "currentSortOption" : "defaultSortOption",
                currentSortOption != null ? currentSortOption.getClass() : "none");

        // And fill the list with the new data, if that is available
        if (data != null) {

            if (android.os.Build.VERSION.SDK_INT >= 11) {
                // Available API >= 11
                addAll(data);
            }
            else {
                // Support for API < 11
                for (ModelClass item : data) {
                    add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    /**
     * Returns whether an item in the list should display a header. If the cache does not contain an entry for the item at
     * this position, {@link Header#calculateHeaderNeed(int, ModelClass, ModelClass)} is called. That is automatically saved in
     * the cache.
     *
     * @param position
     *         The position of the item in the list for which we want to know whether it should display a header
     *
     * @return True if the item at this position should display a header, otherwise false
     */
    protected boolean needsHeader(int position) {
        return headersEnabled && getHeaderFactory().needsHeader(getItem(position), getItem(position - 1));
    }

    /**
     * Returns the default SortOption that was given with the constructor.
     *
     * @return The default SortOption that was given with the constructor.
     */
    public SortOption<ModelClass> getDefaultSortOption() {
        return defaultSortOption;
    }

    /**
     * Returns the current applied SortOption.
     *
     * @return The current applied SortOption.
     */
    public SortOption<ModelClass> getCurrentSortOption() {
        return currentSortOption;
    }

    /**
     * Sets the current SortOption. Invalidates the list by calling {@link #notifyDataSetInvalidated()}.
     *
     * @param currentSortOption
     *         The SortOption to be applied
     */
    public void setCurrentSortOption(SortOption<ModelClass> currentSortOption) {

        // Use defaultSortOption if the given currentSortOption is null
        if (currentSortOption != null) {
            this.currentSortOption = currentSortOption;
        }
        else {
            L.w("param currentSortOption is null! Falling back to defaultSortOption");
            this.currentSortOption = defaultSortOption;
        }

        if (currentSortOption != null && currentSortOption.getComparator() != null) {
            sort(currentSortOption.getComparator());
        }
        else {
            L.w("Current sort option or its comparator is null! List not sorted.");
        }


        notifyDataSetInvalidated();
    }

    public HeaderFactory<ModelClass> getHeaderFactory() {
        return getCurrentSortOption() != null ?
                getCurrentSortOption().getHeaderFactory() :
                getDefaultSortOption().getHeaderFactory();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = newView(position, LayoutInflater.from(getContext()), parent);
        }

        // If headers are enabled, the first position always needs a header, or calculate if a header is needed
        boolean needsHeader = false;
        if (headersEnabled) {
            needsHeader = position == 0 || needsHeader(position);
        }

//        L.d("{0} getView pos={1} headersEnabled={2}, needsHeader={3}", getClass().getSimpleName(), position, headersEnabled, needsHeader);

        // Let the sub class bind the data to the view
        bindView(position, getItem(position), needsHeader, convertView);

        return convertView;
    }

    public abstract View newView(int position, LayoutInflater inflater, ViewGroup parent);

    public abstract void bindView(int position, ModelClass model, boolean needsHeader, View view);
}