package baecon.devgames.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import baecon.devgames.R;
import baecon.devgames.connection.synchronization.IModelManager;
import baecon.devgames.database.loader.ModelListLoader;
import baecon.devgames.ui.widget.DevGamesSwipeRefreshLayout;
import baecon.devgames.ui.widget.ModelListAdapter;
import baecon.devgames.util.L;
import baecon.devgames.util.SortOption;
import baecon.devgames.util.Utils;

import static baecon.devgames.ui.fragment.DevGamesTab.OnAddActionListener;
import static baecon.devgames.ui.fragment.DevGamesTab.OnFilterRequestListener;
import static baecon.devgames.ui.fragment.DevGamesTab.OnRefreshRequestListener;
import static baecon.devgames.ui.fragment.DevGamesTab.OnSearchActionListener;
import static baecon.devgames.ui.fragment.DevGamesTab.OnSortActionListener;

/**
 * An implementation of a {@link ListView} in a {@link Fragment}. Implementation is mostly taken from
 * {@link android.support.v4.app.ListFragment}. This class assumes that a list of {@link Model} elements has to be displayed. These elements
 * mostly come from a local database, used with the ORMLite database layer. with a {@link ModelListLoader} and {@link ModelListAdapter} that loads data
 * automatically from the database corresponding to the {@code Model} via a Loader. The ModelListLoader will take
 * care of loading the data async from the database. The ModelListAdapter will take care of binding the data to a list.
 * Use {@link #onRefreshRequest()} to restart the Loader.
 * <p>
 * Implement the following methods:
 * <ul>
 * <li>{@link #onCreateModelListAdapter(Context)} where you supply your implementation of a
 * {@link baecon.devgames.ui.widget.ModelListAdapter}</li>
 * <li>{@link #onCreateLoader(int, Bundle)} where you supply your implementation of a
 * {@link baecon.devgames.database.loader.ModelListLoader}</li>
 * </ul>
 * </p>
 * <p>
 * Use with:
 * <ul>
 * <li>{@link ModelListLoader}</li>
 * <li>{@link ModelListAdapter}</li>
 * </ul>
 * </p>
 * <br />
 *
 * @param <Model>
 *         The type of the ORMLite model class that represents a table in the local database
 * @param <Id>
 *         The type of the ID column of the {@link Model}
 */
public abstract class ModelListFragment<Model, Id>
        extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Model>>, OnRefreshRequestListener, OnSearchActionListener,
        OnSortActionListener, OnFilterRequestListener, OnAddActionListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String SAVED_CURRENT_SORT_OPTION = "current_sort_option";
    public static final String SAVED_CURRENT_FILTER_OPTION = "current_filter_option";

    private String currentSearchQuery;
    private SortOption<Model> currentSortOption;
    private FilterOption currentFilterOption;

    protected DevGamesSwipeRefreshLayout swipeRefreshLayout;

    private int tempDialogSelectedPosition;

    /**
     * A handler to handle the {@link #requestFocus}
     */
    private final Handler handler = new Handler();

    /**
     * A runnable to request focus.
     */
    private final Runnable requestFocus = new Runnable() {
        @Override
        public void run() {
            listView.focusableViewAvailable(listView);
        }
    };

    /**
     * The listener for the {@link ListView}, when an item is clicked.
     */
    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (adapter == null) {
                onListItemClick(null, view, position, id);
            }
            else {
                onListItemClick(adapter.getItem(position), view, position, id);
            }
        }
    };

    /**
     * The adapter that holds the data to be displayed in the {@link #listView}.
     */
    private ModelListAdapter<Model> adapter;

    /**
     * The actual list. If the {@link #adapter} has 0 elements, it setse the {@link #emptyView} to {@link View#VISIBLE}.
     * This behaviour is enabled by {@link #ensureList()}, where {@link ListView#setEmptyView(View)} is called.
     * {@link #emptyView} MUST BE a sibling of this list view in order to enable this behaviour.
     */
    private ListView listView;

    /**
     * The view that will be shown when the adapter has 0 elements. The visibility of this view is managed by
     * {@link #listView}. In order to enable that behaviour, this view MUST BE a sibling of {@link #listView}.
     */
    private LinearLayout emptyView;

    /**
     * Whether the {@link #listContainer} is shown. If false, {@link #refreshLayout} should be made visible.
     */
    private boolean listShown;

    /**
     * The container that contains {@link #listView} and {@link #emptyView}.
     */
    private ViewGroup listContainer;

    /**
     * An indeterminate ProgressBar that is shown when a background refresh is going on, but there is no data to be shown
     */
    private ProgressBar refreshLayout;

    /**
     * Whether the user hit the refresh button in the menu. This is true while the background tasks are busy
     */
    private boolean refreshRequestedByUser = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_model_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ensureList();

        getListView().setDivider(null);
        getListView().setDividerHeight(0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ensureList();

        L.d("onActivityCreated");

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.cornflower,
                R.color.seance,
                R.color.electric
        );

        // Create the adapter
        adapter = onCreateModelListAdapter(getActivity());

        // No adapter supplied? Than we can't work with you.
        if (adapter == null) {
            throw new NullPointerException("Adapter can not be null!");
        }

        // Set the adapter and show we are loading data
        setListAdapter(adapter);
        setListShown(false);

        if (savedInstanceState != null) {

            // TODO
            // Restore the current sort option if available
//            if (savedInstanceState.containsKey(SAVED_CURRENT_SORT_OPTION)) {
//                Object savedSortOption = savedInstanceState.get(SAVED_CURRENT_SORT_OPTION);
//                if (savedSortOption != null && savedSortOption instanceof Sort) {
//                    setCurrentSortOption((Sort) savedSortOption);
//                }
//            }

            // Restore the current filter if available
            if (savedInstanceState.containsKey(SAVED_CURRENT_FILTER_OPTION)) {
                Object savedFilterOption = savedInstanceState.get(SAVED_CURRENT_FILTER_OPTION);
                if (savedFilterOption != null && savedFilterOption instanceof FilterOption) {
                    setCurrentFilterOption((FilterOption) savedFilterOption);
                }
            }
        }

        // Start loading data
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacks(requestFocus);
        listView = null;
        emptyView = null;
        swipeRefreshLayout = null;
        refreshLayout = null;
        super.onDestroyView();
    }

    /**
     * Invoked when an item in {@link #getListView()} is clicked.
     *
     * @param item
     *         The {@link Model} that is bound to the entry that was clicked. Can be null
     * @param v
     *         The view in the list that was clicked within the ListView
     * @param position
     *         The position of the clicked item in the list
     * @param id
     *         The row id of the item that was clicked
     */
    public void onListItemClick(Model item, View v, int position, long id) {
        L.v("Item clicked! Position: {0}", position);
    }

    @Override
    public void onSortRequest() {
        L.v("called");
    }

    @Override
    public void onFilterRequest() {
        L.v("called");
    }

    @Override
    public void onAddRequest() {
        L.v("called");
    }

    @Override
    public void onRefreshRequest() {
		handler.post(new Runnable() {
			public void run() {
				L.v("called");

				if (refreshRequestedByUser) {
					L.d("Already refreshing, ignoring request");
				}
				else {

					if (getActivity() != null && Utils.hasInternetConnection(getActivity())) {
                        if (swipeRefreshLayout != null) {
                            refreshRequestedByUser = true;
                            swipeRefreshLayout.setRefreshing(true);
                            getSyncManager().startPoll();
                        } else {
                            L.d("Skipping refresh, as swipeRefreshLayout is null");
                        }
                    }
				}
			}
		});
	}

    public void onRefreshDone() {

		handler.post(new Runnable() {
			public void run() {
				L.v("called");

				// Only do the animation when the item is not hidden
				if (swipeRefreshLayout != null && isVisible()) {
					swipeRefreshLayout.setRefreshing(false);
				}

				refreshRequestedByUser = false;
			}
		});
	}

    public boolean isRefreshing() {
        return refreshRequestedByUser;
    }

    @Override
    public void onRefresh() {
        onRefreshRequest();
    }

    /**
     * Shows an AlertDialog with a list that contains the {@link SortOption} options passed via {@link #getSortOptions()}.
     * When a list item is clicked, {@link #onSortOptionSelected(SortOption)} is called.
     */
    protected void showSortDialog() {

        final List<SortOption<Model>> sortOptions = getSortOptions();

        if (sortOptions == null) {
            L.d("getSortOptions returned null. Dialog will not be shown to the user");
        }
        else {

            int currentSortOptionPosition = 0;
            SortOption<Model> currentSortOption = getCurrentSortOption();
            if (currentSortOption != null) {
                currentSortOptionPosition = sortOptions.indexOf(currentSortOption);
            }

            List<String> stringifiedSortOptions = new ArrayList<>(sortOptions.size());

            for (int i = 0, sortOptionsSize = sortOptions.size(); i < sortOptionsSize; i++) {
                SortOption<Model> so = sortOptions.get(i);
                if (so.getI18nTitleResId() > 0) {
                    stringifiedSortOptions.add(getString(so.getI18nTitleResId()));
                }
                else {
                    L.w("Invalid string resource id! Showing a sort option with an empty title. Adapter position: {0}", i);
                    stringifiedSortOptions.add("");
                }
            }

            final ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.select_dialog_singlechoice,
                    stringifiedSortOptions);

            if (currentSortOption != null) {
                tempDialogSelectedPosition = sortOptions.indexOf(currentSortOption);
            }
            else {
                tempDialogSelectedPosition = 0;
            }

            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.sort_order)
                    .setSingleChoiceItems(adapter, currentSortOptionPosition, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Choice will be saved when positive button is clicked
                            tempDialogSelectedPosition = which;
                        }
                    })
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onSortOptionSelected(sortOptions.get(tempDialogSelectedPosition));
                            tempDialogSelectedPosition = -1;
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tempDialogSelectedPosition = -1;
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }

    public void showFilterDialog() {
        final List<FilterOption> filterOptions = getFilterOptions();

        if (filterOptions == null) {
            L.d("getFilterOptions returned null. Dialog will not be shown to the user");
        }
        else {

            int currentFilterOptionPosition = 0;
            FilterOption currentFilterOption = getCurrentFilterOption();
            if (currentFilterOption != null) {
                currentFilterOptionPosition = filterOptions.indexOf(currentFilterOption);
            }

            List<String> stringifiedFilterOptions = new ArrayList<>(filterOptions.size());

            for (int i = 0, filterOptionsSize = filterOptions.size(); i < filterOptionsSize; i++) {
                FilterOption fo = filterOptions.get(i);
                if (fo.getI18nTitleResId() > 0) {
                    stringifiedFilterOptions.add(getString(fo.getI18nTitleResId()));
                }
                else {
                    L.w("Invalid string resource id! Showing a filter option with an empty title. Adapter position: {0}", i);
                    stringifiedFilterOptions.add("");
                }
            }

            final ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.select_dialog_singlechoice,
                    stringifiedFilterOptions);

            if (currentFilterOption != null) {
                tempDialogSelectedPosition = filterOptions.indexOf(currentFilterOption);
            }
            else {
                tempDialogSelectedPosition = 0;
            }

            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.filter)
                    .setSingleChoiceItems(adapter, currentFilterOptionPosition, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Choice will be saved when positive button is clicked
                            tempDialogSelectedPosition = which;
                        }
                    })
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onFilterOptionSelected(filterOptions.get(tempDialogSelectedPosition));
                            tempDialogSelectedPosition = -1;
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tempDialogSelectedPosition = -1;
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }

    /**
     * Called when the user selects a {@link SortOption} from the shown dialog. The selection of the SortOption is saved by
     * the dialog and will be applied the next time {@link #onLoadFinished(Loader, List)} is called. The dialog is shown
     * when the user clicks the sort button in the ActionBar.
     *
     * @param sortOption
     *         The selected SortOption by the user from the dialog
     */
    public void onSortOptionSelected(SortOption<Model> sortOption) {
        L.d("called");
        setCurrentSortOption(sortOption);
    }

    /**
     * Called when the user selects a {@link ModelListFragment.FilterOption} from
     * the shown dialog (shown by {@link #showFilterDialog()}
     *
     * @param filterOption
     *         The selected FilterOption by the user from the dialog
     */
    public void onFilterOptionSelected(FilterOption filterOption) {
        L.d("Called");
        setCurrentFilterOption(filterOption);
    }

	/**
     * Returns the Synchronization Manager
     *
     * @return The Synchronization Manager
     */
    public abstract IModelManager getSyncManager();

    /**
     * Returns the list of sort options for the list of this fragment. This data will be used in an AlertDialog when the
     * user hits the sort button in the ActionBar. Returning {@code null} means no sorting option is available; the
     * AlertDialog will not be shown to the user.
     *
     * @return The list of sort options for the list of fragment. {@code null} allowed to show no dialog.
     */
    public abstract List<SortOption<Model>> getSortOptions();

    /**
     * Returns the list of filter options for the list of this fragment. This data will be used in an AlertDialog when the
     * user hits the filter button in the ActionBar. Returning {@code null} means no filter options are available.
     *
     * @return The list of filter options for the list of fragment. {@code null} allowed to show no dialog.
     */
    public abstract List<FilterOption> getFilterOptions();

    public SortOption<Model> getCurrentSortOption() {
        return currentSortOption;
    }

    public void setCurrentSortOption(SortOption<Model> currentSortOption) {
        this.currentSortOption = currentSortOption;

        if (getListAdapter() != null && currentSortOption != null && currentSortOption.getComparator() != null) {
            try {
                adapter.setCurrentSortOption(currentSortOption);
            }
            catch (Exception e) {
                L.e(e, "Could not set current sort option");
            }
        }
    }

    public String getCurrentSearchQuery() {
        return currentSearchQuery;
    }

    public FilterOption getCurrentFilterOption() {
        return currentFilterOption;
    }

    public void setCurrentFilterOption(FilterOption currentFilterOption) {

        L.v("{0}", currentFilterOption);

        this.currentFilterOption = currentFilterOption;
        onRefreshRequest();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        L.d("onQueryTextChange newText: {0}", newText);

        currentSearchQuery = newText;

        if (isAdded()) {
            // Restart the Loader to display the filtered the list
            getLoaderManager().restartLoader(0, null, this);
        }

        return true;
    }

    /**
     * Provide the data for the ListView. Typically, this is already called by {@link #onLoadFinished(Loader, List)}.
     */
    public void setListAdapter(ModelListAdapter<Model> adapter) {
        boolean hadAdapter = adapter != null;
        this.adapter = adapter;
        if (listView != null) {
            listView.setAdapter(adapter);
            if (!listShown && !hadAdapter) {
                // The list was hidden, and previously didn't have an
                // adapter.  It is now time to show it.
                setListShown(true, getView().getWindowToken() != null);
            }
        }
    }

    /**
     * Set the currently selected list item to the specified
     * position with the adapter's data
     *
     * @param position
     *         The position of the item that has to be set to 'selected'
     */
    public void setSelection(int position) {
        ensureList();
        listView.setSelection(position);
    }

    /**
     * Get the position of the currently selected list item.
     */
    public int getSelectedItemPosition() {
        ensureList();
        return listView.getSelectedItemPosition();
    }

    /**
     * Get the cursor row ID of the currently selected list item.
     */
    public long getSelectedItemId() {
        ensureList();
        return listView.getSelectedItemId();
    }

    /**
     * Returns the actual {@link ListView}
     */
    public ListView getListView() {
        ensureList();
        return listView;
    }

    /**
     * Restarts the loader calling {@link #getLoaderManager().restartLoader()}. Shows a progress bar when refreshing.
     */
    public void refresh() {
        getLoaderManager().restartLoader(0, null, this);

        if (swipeRefreshLayout != null && isVisible()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    /**
     * Returns the {@link ModelListAdapter<Model>} that provides the data to the {@link #getListView()}.
     *
     * @return The {@link ModelListAdapter<Model>} that provides the data to the {@link #getListView()}.
     */
    public ModelListAdapter<Model> getListAdapter() {
        return adapter;
    }

    /**
     * Supply your implementation of a {@link ModelListAdapter}. See class level javadoc.
     *
     * @param context
     *
     * @return Your implementation of a {@link ModelListAdapter}
     */
    protected abstract ModelListAdapter<Model> onCreateModelListAdapter(Context context);

    /**
     * Supply your implementation of a {@link ModelListLoader}
     *
     * @param i
     *         The ID whose loader is to be created
     * @param bundle
     *         Any arguments supplied by the caller
     *
     * @return Your implementation of a {@link ModelListLoader}
     */
    public abstract ModelListLoader<Model, Id> onCreateLoader(int i, Bundle bundle);

    /**
     * Called when a previously created loader has finished its load
     *
     * @param listLoader
     *         The {@link ModelListAdapter} that has finished
     * @param data
     *         The data generated by the Loader
     */
    public void onLoadFinished(Loader<List<Model>> listLoader, List<Model> data) {

        L.d("{0}.onLoadFinished. Loaded {1} items",
                getClass().getSimpleName(),
                ((data != null) ? data.size() : 0)
        );

        onRefreshDone();

        adapter.setData(data, currentSortOption);

        if (isResumed()) {
            setListShown(true);
        }
        else {
            setListShownNoAnimation(true);
        }
    }

    public void onLoaderReset(Loader<List<Model>> listLoader) {
        // No implementation needed
    }

    /**
     * Hides the progress indicator and makes the ListView visible when true, otherwise the opposite. Uses an animation
     * for the transition between visible and hidden for both the progress indicator and the ListView.
     *
     * @param shown
     *         True when the list should be shown, false when the list should be hidden
     */
    public void setListShown(boolean shown) {
        setListShown(shown, true);
    }

    /**
     * Hides the progress indicator and makes the ListView visible when true, otherwise the opposite. Without animation.
     *
     * @param shown
     *         True when the list should be shown, false when the list should be hidden
     */
    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }

    /**
     * Hides the progress indicator and makes the ListView visible when true, otherwise the opposite.
     *
     * @param shown
     *         True when the list should be shown, false when the list should be hidden
     * @param animate
     *         Whether an animation should be used for the transition from hidden to visible / visible to hidden
     */
    protected void setListShown(boolean shown, boolean animate) {
        ensureList();

        if (refreshLayout == null || swipeRefreshLayout == null) {
            L.e("Either one or both of the progress indicators are null! refresh_layout null = {0}, swipeRefreshLayout null = {1}",
                    refreshLayout == null, swipeRefreshLayout == null);
            return;
        }

        if (listShown == shown) {
            return;
        }

        listShown = shown;

        if (shown) {
            if (animate) {
                refreshLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                listContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            }
            else {
                refreshLayout.clearAnimation();
                listContainer.clearAnimation();
            }

            refreshLayout.setVisibility(View.GONE);
            listContainer.setVisibility(View.VISIBLE);
        }
        else {
            if (animate) {
                refreshLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                listContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            }
            else {
                refreshLayout.clearAnimation();
                listContainer.clearAnimation();
            }

            refreshLayout.setVisibility(View.VISIBLE);
            listContainer.setVisibility(View.GONE);
        }
    }

    /**
     * By default, the 'empty view' has a TextView. If that is available, the {@code text} will be set to that
     * {@link TextView}. The 'empty view' will be shown when {@link #setListShown(boolean)} or
     * {@link #setListShownNoAnimation(boolean)} is called with {@code true}, but the adapter has no elements.
     *
     * @param text
     *         The text to be shown when the list is visible, but the adapter has no elements
     */
    public void setEmptyText(CharSequence text) {
        ensureList();

        if (isAdded() && getView() != null && getView().findViewById(R.id.text) != null) {
            TextView emptyText = (TextView) getView().findViewById(R.id.text);

            if (emptyText != null) {
                emptyText.setText(text);
            }
        }
    }

    /**
     * Retrieves the String and calls {@link #setEmptyText(CharSequence)}.
     *
     * @param textResourceId
     *         The String resource id
     */
    public void setEmptyText(int textResourceId) {
        setEmptyText(getString(textResourceId));
    }

    /**
     * Makes sure all views have been setup to perform operations like hiding/displaying progress indicators, the list
     * itself and the empty view. Contrary to {@link android.support.v4.app.ListFragment}, this does not throw exceptions, but spits log messages
     * to the LogCat when something went wrong.
     */
    private void ensureList() {

        if (listView != null) {
            // List is already available
            return;
        }

        View root = getView();
        if (root == null) {
            L.e("Content view not yet created! ");
            return;
        }

        listContainer = (ViewGroup) root.findViewById(R.id.list_container);
        if (listContainer == null) {
            L.e("There was no ViewGroup found with R.id.list_container");
            return;
        }

        /**
         * The list that displays the actual content. Hidden if the adapter has 0 items.
         */
        listView = (ListView) root.findViewById(android.R.id.list);
        if (listView == null) {
            L.e("Could not find listView! Does your layout have a android.widget.ListView with id android.R.id.list ?");
            return;
        }

        /**
         * A indeterminate progress indicator, covering the whole screen. Showed when loading and no data can be shown
         * already
         */
        refreshLayout = (ProgressBar) root.findViewById(R.id.refresh_layout);
        if (refreshLayout == null) {
            L.w("A progress indicator layout as a child was not found. No view had id R.id.refresh_layout");
            return;
        }

        /**
         * A refresh bar with a small indeterminate progress indicator, used as an overlay when the user is manually
         * triggered a refresh. The view is removed as soon as the background refresh is done.
         * This view is used as an overlay (floating above the list) to indicate a manual refresh is busy.
         */
        swipeRefreshLayout = (DevGamesSwipeRefreshLayout) root.findViewById(R.id.refresh_bar);
        if (swipeRefreshLayout == null) {
            L.w("A progress indicator bar as a child was not found. No view had id R.id.refresh_bar");
            return;
        } else {
            swipeRefreshLayout.setListView(listView);
        }

        emptyView = (LinearLayout) root.findViewById(android.R.id.empty);
        if (emptyView == null) {
            L.w("An empty view was not found. No view had id android.R.id.empty");
            return;
        }

        if (listContainer.findViewById(android.R.id.list) == null
                || listContainer.findViewById(android.R.id.empty) == null) {
            L.e("List and empty view are not siblings! This will cause problems when you use the empty ");
            return;
        }

        /**
         * The ListView takes care of displaying / hiding the emptyView based on the adapter.getCount(). This is built in
         * behaviour. The emptyView MUST BE a sibling of the ListView in the layout and MUST HAVE the id android.R.id.empty
         */
        listView.setEmptyView(emptyView);

        listShown = true;
        listView.setOnItemClickListener(onItemClickListener);

        /**
         * If there is no adapter present, we assume there will be no data available yet. So, we'll show the refreshLayout
         */
        if (adapter == null) {
            setListShown(false, false);
        }
        else {
            ModelListAdapter<Model> adapter = this.adapter;
            this.adapter = null;
            setListAdapter(adapter);
        }
    }

    /**
     * An object that defines a filtering option for a list in a {@link ModelListFragment}.
     * {@link #getI18nTitleResId()} supplies the title of this sort option to display it in a dialog. This way, the user
     * is able to select the desired sorting option. The filter implementation is up to you.
     */
    public interface FilterOption extends Serializable {

        /**
         * The i18n title string resource id for this filter option. If an non valid id is supplied, an empty string is
         * used
         *
         * @return The i18n title string resource id for this filter option
         */
        int getI18nTitleResId();

        /**
         * Returns the string representation of this sort option. This will be used to store the sort option preference of
         * the user. Make sure this is a unique name.
         *
         * @return The string representation of this sort option.
         */
        String name();
    }
}
