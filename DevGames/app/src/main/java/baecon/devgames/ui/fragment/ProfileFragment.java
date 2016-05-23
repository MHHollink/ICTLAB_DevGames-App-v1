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
import baecon.devgames.database.loader.UserLoader;
import baecon.devgames.events.BusProvider;
import baecon.devgames.events.user.UserPushScheduledEvent;
import baecon.devgames.events.user.UsersUpdatedEvent;
import baecon.devgames.model.User;
import baecon.devgames.util.L;
import baecon.devgames.util.Utils;

public class ProfileFragment extends DevGamesFragment implements DevGamesTab {

    private String title = "";

    public static final String USER_LOCAL_ID = "user_local_id";
    public static final int USER_LOADER_ID = 1;

    @Override
    public Fragment getFragmentFromTab() {
        return getFragment();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public ProfileFragment setTitle(String s) {
        title = s;
        return this;
    }

    private Long initialUserId = null;
    private User user = null;

    private TextView nameView;
    private TextView scoreView;
    private TextView projectsView;
    private TextView commitsView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null) {
            initialUserId = b.getLong(USER_LOCAL_ID);
        }
        else {
            L.e("No LocalUserId given!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameView = (TextView) view.findViewById(R.id.txt_name);
        scoreView = (TextView) view.findViewById(R.id.txt_score);
        projectsView = (TextView) view.findViewById(R.id.txt_projects);
        commitsView = (TextView) view.findViewById(R.id.txt_commits);
    }

    @Override
    public void onResume() {
        super.onResume();

        BusProvider.getBus().register(this);

        // Start loading the data
        startOrRefreshUserLoader(initialUserId);
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();

        BusProvider.getBus().unregister(this);
    }

    /**
     * Starts the loader that loads the user from the local database
     *
     * @param localUserId The local id of the client
     */
    private void startOrRefreshUserLoader(Long localUserId) {

        L.v("Called");

        Bundle args = new Bundle(1);
        args.putLong(USER_LOCAL_ID, localUserId);

        getLoaderManager().restartLoader(USER_LOADER_ID, args, userLoaderCallbacks);
    }


    /**
     * Callbacks used for the user loader
     */
    private LoaderManager.LoaderCallbacks<User> userLoaderCallbacks = new LoaderManager.LoaderCallbacks<User>() {

        @Override
        public Loader<User> onCreateLoader(int id, Bundle args) {
            Long userId = args != null ? args.getLong(USER_LOCAL_ID) : null;
            L.d("userId : "+ userId);
            return new UserLoader(getActivity(), userId);
        }

        @Override
        public void onLoadFinished(Loader<User> loader, User data) {
            L.v("Called");
            user = data;
            updateUI();
        }

        @Override
        public void onLoaderReset(Loader<User> loader) {
            L.v("Called");
            user = null;
            updateUI();
        }
    };

    private void updateUI(){
        L.d("Called, User : {0}", user);

        if (user == null)
            return;

        nameView.setText(user.getUsername());
        scoreView.setText(String.valueOf(user.getScore()));
        projectsView.setText(String.valueOf(
                user.getProjects() != null ? user.getProjects().size() : 0
        ));
        commitsView.setText(String.valueOf(
                user.getPushes() != null ? user.getPushes().size() : 0
        ));
    }

    @Subscribe
    public void onUserPushScheduledEvent(UserPushScheduledEvent event) {
        L.v("{0}", event);

        if (!event.success)
            Utils.createToast(this, "Een update kon niet met de server worden gesynchonizeerd", Toast.LENGTH_SHORT);
    }

    @Subscribe
    public void onUserUpdatedEvent(UsersUpdatedEvent event) {
        L.v("{0}", event);
    }

}
