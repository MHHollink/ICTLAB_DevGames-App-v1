package baecon.devgames.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.squareup.otto.Subscribe;

import java.util.Stack;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.R;
import baecon.devgames.connection.synchronization.ProjectManager;
import baecon.devgames.connection.synchronization.PushManager;
import baecon.devgames.connection.synchronization.UserManager;
import baecon.devgames.connection.task.GcmRegistrationTask;
import baecon.devgames.events.LogoutEvent;
import baecon.devgames.ui.fragment.ProfileFragment;
import baecon.devgames.ui.fragment.ProjectsFragment;
import baecon.devgames.ui.fragment.UserPushesFragment;
import baecon.devgames.ui.widget.SlidingTabLayout;
import baecon.devgames.util.L;
import baecon.devgames.util.Utils;
import baecon.devgames.util.ViewPageAdapter;

public class MainActivity extends DevGamesActivity {

    /**
     * The class responsible for transitions between tab-fragments.
     */
    private SlidingTabLayout indicator = null;
    private ViewPager pager;
    private ViewPageAdapter adapter;

    private Handler handler;

    private ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {

            L.d("onPageSelected ({0})", position);

            // hideSearchView(); // TODO: 17-5-2016 when search view eneabled on lists views.

            previousFragmentPositions.push(position);

            supportInvalidateOptionsMenu();
        }
    };

    /**
     * A stack keeping track of the position indexes of the tab-fragments.
     */
    private Stack<Integer> previousFragmentPositions = new Stack<>();

    private boolean resumed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        L.d("onCreate");

        if (isLoggedIn())
        return; // If user is not logged in, stop running the onCreate to avoid exceptions

        setContentView(R.layout.main_activity);

        adapter = new ViewPageAdapter(getSupportFragmentManager());

        // Create Profile TAB
        ProfileFragment profile = new ProfileFragment();
        profile.setTitle(DevGamesApplication.get(this).getString(R.string.profile));
        Bundle args = new Bundle(1);
        args.putLong(
                ProfileFragment.USER_LOCAL_ID,
                DevGamesApplication.get(this).getLoggedInUser().getId()
        );
        profile.getFragment().setArguments(args);

        // Create Projects Tab
        ProjectsFragment projects = new ProjectsFragment();
        projects.setTitle(DevGamesApplication.get(this).getString(R.string.project));

        // Create Pushes Tab
        UserPushesFragment pushes = new UserPushesFragment();
        pushes.setTitle(DevGamesApplication.get(this).getString(R.string.pushes));

        // Add pages to the viewpager
        adapter.addTab(
                profile, projects, pushes // TODO: 17-5-2016 more tabs
        );

        // Add the index of the first fragment to the stack.
        previousFragmentPositions.push(0);

        // Attach the adapter to the layout's fragment viewer.
        pager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        pager.setAdapter(adapter);

        // Attach the fragment viewer to the tabs.
        indicator = (SlidingTabLayout) findViewById(R.id.tabs);
        indicator.setDistributeEvenly(true);
        indicator.setSelectedIndicatorColors(getResources().getColor(R.color.cornflower_light));
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(pageChangeListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        resumed = true;

        if (Utils.playServicesAvailable(this)) {

            // Check registration id
            new GcmRegistrationTask(this).executeThreaded();

        } else {

            L.w("Google Play Services is not available on this device! Showing dialog = {0}",
                    getPreferenceManager().getShowPlayServicesDialog());

            // Don't display the dialog again if the user already dismissed it earlier
            if (!getPreferenceManager().getShowPlayServicesDialog()) {
                return;
            }

            final int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);

            if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.common_google_play_services_unsupported_title)
                        .setMessage(R.string.play_services_user_recoverable)
                        .setPositiveButton(R.string.play_services_install, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                GooglePlayServicesUtil.getErrorDialog(result, MainActivity.this, 0);

                                // Hide this dialog next time
                                getPreferenceManager().setShowPlayServicesDialog(false);

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.play_services_continue_without_installing, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Hide this dialog next time
                                getPreferenceManager().setShowPlayServicesDialog(false);

                                dialog.dismiss();
                            }
                        })
                        .show();
            }
            else {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.common_google_play_services_unsupported_title)
                        .setMessage(R.string.play_services_non_user_recoverable)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Hide this dialog next time
                                getPreferenceManager().setShowPlayServicesDialog(false);

                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }

        UserManager.get(this).startForegroundSyncMode();
        PushManager.get(this).startForegroundSyncMode();
        ProjectManager.get(this).startForegroundSyncMode();
    }

    @Override
    protected void onPause() {
        super.onPause();

        resumed = false;

        UserManager.get(this).stopForegroundSyncMode();
        PushManager.get(this).stopForegroundSyncMode();
        ProjectManager.get(this).stopForegroundSyncMode();
    }

    @Override
    protected void onDestroy() {
        adapter = null;
        indicator = null;

        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        supportInvalidateOptionsMenu();

        // Listen for fragment changes and push them on the stack
        indicator.setOnPageChangeListener(pageChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        L.d("onOptionsItemSelected, selectedPage: {0}, item: {1}", pager.getCurrentItem(), item.getTitle());

        if (adapter == null) {
            L.e("Menu Options item selected could not be executed, because the adapter is null!");
            return false;
        }

        Fragment lastFragment = (Fragment) adapter.instantiateItem(pager, pager.getCurrentItem());

        int i = item.getItemId();
        if (i == R.id.menu_btn_settings) {
            // startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        if (previousFragmentPositions == null) {
            super.onBackPressed();
        }

        if (previousFragmentPositions.isEmpty()) {
            super.onBackPressed(); // No history, exit the app.
        } else {

            // Go back to the previous fragment.
            previousFragmentPositions.pop();

            if (previousFragmentPositions.isEmpty()) {
                onBackPressed();
            } else {
                final int previousTabIndex = previousFragmentPositions.peek();
                handler.post(new Runnable() {
                    public void run() {
                        pager.setCurrentItem(previousTabIndex, resumed);
                    }
                });
            }
        }
    }

    @Subscribe
    @Override
    public void onLogoutEvent(LogoutEvent event) {
        super.onLogoutEvent(event);
    }
}
