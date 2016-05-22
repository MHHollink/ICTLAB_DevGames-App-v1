package baecon.devgames.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import baecon.devgames.R;
import baecon.devgames.connection.synchronization.IModelManager;
import baecon.devgames.connection.synchronization.PushManager;
import baecon.devgames.database.loader.ModelListLoader;
import baecon.devgames.database.loader.PushListLoader;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.push.PushesUpdatedEvent;
import baecon.devgames.model.Push;
import baecon.devgames.ui.activity.PushDetailsActivity;
import baecon.devgames.ui.widget.ModelListAdapter;
import baecon.devgames.ui.widget.PushAdapter;
import baecon.devgames.util.L;
import baecon.devgames.util.SortOption;

public class UserPushesFragment extends ModelListFragment<Push, Long> implements DevGamesTab{

    private String title = "";

    @Override
    public IModelManager getSyncManager() {
        return PushManager.get(getActivity());
    }

    @Override
    public List<SortOption<Push>> getSortOptions() {
        ArrayList<SortOption<Push>> list = new ArrayList<>(3);
        list.add(Push.Sort.PROJECT);
        list.add(Push.Sort.SCORE);
        list.add(Push.Sort.TIME);
        return list;
    }

    @Override
    public SortOption<Push> getCurrentSortOption() {
        SortOption<Push> option = super.getCurrentSortOption();
        if(option == null) {
            option = Push.Sort.TIME;
            setCurrentSortOption(option);
        }
        return option;
    }

    @Override
    public List<FilterOption> getFilterOptions() {
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEmptyText(R.string.no_pushes_available);

        ListView list = getListView();

        // Register for a context menu. Long clicks on an item causes onCreateContextMenu to be called
        registerForContextMenu(list);
    }


    @Override
    public void onListItemClick(Push push, View v, int position, long id) {
        super.onListItemClick(push, v, position, id);

        L.d("clicked list, push={0}", push.getId());

        Intent intent = new Intent(getActivity(), PushDetailsActivity.class);
        intent.putExtra(PushDetailsActivity.PUSH_ID, push.getId());
        startActivity(intent);

        getActivity().overridePendingTransition(R.anim.top_to_bottom_loose_focus, R.anim.top_to_bottom_gain_focus);
    }

    @Override
    public void onSortRequest() {
        showSortDialog();
    }

    @Override
    public void onResume() {
        L.d("onResume");
        super.onResume();
        BusProvider.getBus().register(this);
    }

    @Override
    public void onPause() {
        L.d("onPause");
        super.onPause();
        BusProvider.getBus().unregister(this);
    }

    @Override
    protected ModelListAdapter<Push> onCreateModelListAdapter(Context context) {
        return new PushAdapter(context, Push.Sort.TIME);
    }

    @Override
    public ModelListLoader<Push, Long> onCreateLoader(int i, Bundle bundle) {
        return new PushListLoader(getActivity(), getCurrentSortOption(), getCurrentFilterOption(), getCurrentSearchQuery());
    }


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public UserPushesFragment setTitle(String s) {
        title = s;
        return this;
    }

    @Override
    public Fragment getFragmentFromTab() {
        return getFragment();
    }

    @Subscribe
    public void onPushUpdatedEvent(PushesUpdatedEvent event) {
        L.d("onPushUpdatedEvent");
        onRefreshDone();
    }
}