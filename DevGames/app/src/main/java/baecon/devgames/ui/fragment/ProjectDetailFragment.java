package baecon.devgames.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import baecon.devgames.R;
import baecon.devgames.database.loader.ProjectLoader;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.projects.ProjectPushScheduledEvent;
import baecon.devgames.events.projects.ProjectsUpdatedEvent;
import baecon.devgames.model.Project;
import baecon.devgames.ui.activity.ProjectDetailsActivity;
import baecon.devgames.util.L;
import baecon.devgames.util.Utils;

/**
 * TODO: write class level documentation.
 *
 * @author Marcel
 * @since 23-5-2016.
 */
public class ProjectDetailFragment extends DevGamesFragment implements DevGamesTab {

    private String title;

    @Override
    public Fragment getFragmentFromTab() {
        return getFragment();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public ProjectDetailFragment setTitle(String s) {
        title = s;
        return this;
    }

    public static final int PROJECT_LOADER_ID = 2;

    private Long initialProjectId = null;
    private Project project = null;

    private TextView txtName;
    private TextView txtScore;
    private TextView txtProjects;
    private TextView txtCommits;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null) {
            initialProjectId = b.getLong(ProjectDetailsActivity.PROJECT_ID);
        }
        else {
            L.e("No initialProjectId given!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtName = (TextView) view.findViewById(R.id.txt_name);
        txtScore = (TextView) view.findViewById(R.id.txt_score);
        txtProjects = (TextView) view.findViewById(R.id.txt_developers);
        txtCommits = (TextView) view.findViewById(R.id.txt_commits);
    }

    @Override
    public void onResume() {
        super.onResume();

        BusProvider.getBus().register(this);

        // Start loading the data
        startOrRefreshUserLoader(initialProjectId);
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();

        BusProvider.getBus().unregister(this);
    }

    /**
     * Starts the loader that loads the project from the local database
     *
     * @param localProjectId The local id of the project
     */
    private void startOrRefreshUserLoader(Long localProjectId) {

        L.v("Called");

        Bundle args = new Bundle(1);
        args.putLong(ProjectDetailsActivity.PROJECT_ID, localProjectId);

        getLoaderManager().restartLoader(PROJECT_LOADER_ID, args, projectLoaderCallbacks);
    }

    /**
     * Callbacks used for the user loader
     */
    private LoaderManager.LoaderCallbacks<Project> projectLoaderCallbacks = new LoaderManager.LoaderCallbacks<Project>() {

        @Override
        public Loader<Project> onCreateLoader(int id, Bundle args) {
            Long userId = args != null ? args.getLong(ProjectDetailsActivity.PROJECT_ID) : null;
            L.d("userId : "+ userId);
            return new ProjectLoader(getActivity(), userId);
        }

        @Override
        public void onLoadFinished(Loader<Project> loader, Project data) {
            L.v("Called");
            project = data;
            updateUI();
        }

        @Override
        public void onLoaderReset(Loader<Project> loader) {
            L.v("Called");
            project = null;
            updateUI();
        }
    };

    private void updateUI(){
        L.d("Called, User : {0}", project);

        if (project == null)
            return;

        txtName.setText(project.getName());
        txtScore.setText(String.valueOf(project.getScore()));
        txtProjects.setText(String.valueOf(
                project.getDevelopers() != null ? project.getDevelopers().size() : 0
        ));
        txtCommits.setText(String.valueOf(
                project.getPushes() != null ? project.getPushes().size() : 0
        ));
    }

    public void onProjectPushScheduledEvent(ProjectPushScheduledEvent event) {
        L.v("{0}", event);

        if (!event.success)
            Utils.createToast(this, "Een update kon niet met de server worden gesynchonizeerd", Toast.LENGTH_SHORT);
    }

    @Subscribe
    public void onProjectsUpdatedEvent(ProjectsUpdatedEvent event) {
        L.v("{0}", event);
    }

}
