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
import baecon.devgames.connection.synchronization.ProjectManager;
import baecon.devgames.database.loader.ModelListLoader;
import baecon.devgames.database.loader.ProjectListLoader;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.projects.ProjectsUpdatedEvent;
import baecon.devgames.model.Project;
import baecon.devgames.ui.activity.ProjectDetailsActivity;
import baecon.devgames.ui.widget.ModelListAdapter;
import baecon.devgames.ui.widget.ProjectsAdapter;
import baecon.devgames.util.L;
import baecon.devgames.util.SortOption;

public class ProjectsFragment extends ModelListFragment<Project, Long> implements DevGamesTab{

    private String title = "";

    @Override
    public IModelManager getSyncManager() {
        return ProjectManager.get(getActivity());
    }

    @Override
    public List<SortOption<Project>> getSortOptions() {
        ArrayList<SortOption<Project>> list = new ArrayList<>(2);
        list.add(Project.Sort.NAME);
        list.add(Project.Sort.SCORE);
        return list;
    }

    @Override
    public SortOption<Project> getCurrentSortOption() {
        SortOption<Project> option = super.getCurrentSortOption();

        if(option == null) {
            option = Project.Sort.NAME;
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

        setEmptyText(R.string.no_projects_available);

        ListView list = getListView();

        // Register for a context menu. Long clicks on an item causes onCreateContextMenu to be called
        registerForContextMenu(list);
    }

    @Override
    public void onListItemClick(Project project, View v, int position, long id) {
        super.onListItemClick(project, v, position, id);

        L.d("clicked list, project={0}", project.getId());

        Intent intent = new Intent(getActivity(), ProjectDetailsActivity.class);
        intent.putExtra(ProjectDetailsActivity.PROJECT_ID , project.getId());
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
    protected ModelListAdapter<Project> onCreateModelListAdapter(Context context) {
        return new ProjectsAdapter(context, Project.Sort.NAME);
    }

    @Override
    public ModelListLoader<Project, Long> onCreateLoader(int i, Bundle bundle) {
        return new ProjectListLoader(getActivity(), getCurrentSortOption(), getCurrentFilterOption(), getCurrentSearchQuery());
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public ProjectsFragment setTitle(String s) {
        title = s;
        return this;
    }

    @Override
    public Fragment getFragmentFromTab() {
        return getFragment();
    }

    @Subscribe
    public void onProjectsUpdatedEvent(ProjectsUpdatedEvent event) {
        L.d("onProjectsUpdatedEvent");
        onRefreshDone();
    }
}
