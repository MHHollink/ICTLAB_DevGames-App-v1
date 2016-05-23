package baecon.devgames.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
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
import baecon.devgames.ui.fragment.DevGamesTab;
import baecon.devgames.ui.fragment.ProfileFragment;
import baecon.devgames.ui.fragment.ProjectsFragment;
import baecon.devgames.ui.fragment.UserPushesFragment;
import baecon.devgames.ui.widget.SlidingTabLayout;
import baecon.devgames.util.L;
import baecon.devgames.util.Utils;
import baecon.devgames.util.ViewPageAdapter;

public class MainActivity extends DevGamesActivity implements SearchView.OnQueryTextListener {

    public static final boolean[] showSearchInActionBar  = new boolean[]{false,  true,  true};
    public static final boolean[] showSortInActionBar    = new boolean[]{false,  true,  true};
    public static final boolean[] showFilterInActionBar  = new boolean[]{false, false, false};
    public static final boolean[] showRefreshInActionBar = new boolean[]{false,  true,  true};

    /**
     * The class responsible for transitions between tab-fragments.
     */
    private SlidingTabLayout indicator = null;
    private ViewPager pager;
    private ViewPageAdapter adapter;

    private SearchView searchView;
    private MenuItem searchMenuItem;

    private Handler handler;
    private boolean resumed = false;

    private ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {

            L.d("onPageSelected ({0})", position);

            hideSearchView();

            previousFragmentPositions.push(position);

            supportInvalidateOptionsMenu();
        }
    };

    /**
     * A stack keeping track of the position indexes of the tab-fragments.
     */
    private Stack<Integer> previousFragmentPositions = new Stack<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

        L.d("onCreate");

        if (!isLoggedIn())
            return; // If user is not logged in, stop running the onCreate to avoid exceptions

        setContentView(R.layout.main_activity);

        adapter = new ViewPageAdapter(getSupportFragmentManager());

        // Create Profile TAB
        ProfileFragment profile = new ProfileFragment()
                .setTitle(
                        DevGamesApplication.get(this).getString(R.string.profile)
                );
        Bundle args = new Bundle(1);
        args.putLong(
                ProfileFragment.USER_LOCAL_ID,
                DevGamesApplication.get(this).getLoggedInUser().getId()
        );
        profile.getFragment().setArguments(args);

        // Create Projects Tab
        ProjectsFragment projects = new ProjectsFragment()
                .setTitle(DevGamesApplication.get(this).getString(R.string.project));

        // Create Pushes Tab
        UserPushesFragment pushes = new UserPushesFragment()
                .setTitle(DevGamesApplication.get(this).getString(R.string.pushes));

        // Add pages to the viewpager
        adapter.addTab(
                profile,
                projects,
                pushes
        );

        // Add the index of the first fragment to the stack.
        previousFragmentPositions.push(0);

        // Attach the adapter to the layout's fragment viewer.
        pager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        pager.setAdapter(adapter);

        // Attach the fragment viewer to the tabs.
        indicator = (SlidingTabLayout) findViewById(R.id.tabs);
        indicator.setDistributeEvenly(true);
        indicator.setSelectedIndicatorColors(getResources().getColor(R.color.highland));
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

        // Add the Search item
        searchMenuItem = menu.findItem(R.id.menu_btn_search);
        searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setActionView(searchMenuItem, searchView);

        return true;
    }

    /**
     * Hides the search view if possible.
     * TODO: this implementation is not good. Performance wise very bad, get a better solution!
     */
    public void hideSearchView() {
        if (searchView != null && searchMenuItem != null && !searchView.isIconified()) {
            searchView.setQuery("", false);
            MenuItemCompat.collapseActionView(searchMenuItem);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (!isFinishing()) {
            L.d("selectedPage: " + pager.getCurrentItem());

            menu.findItem(R.id.menu_btn_sort).setVisible(showSortInActionBar[pager.getCurrentItem()]);
            menu.findItem(R.id.menu_btn_search).setVisible(showSearchInActionBar[pager.getCurrentItem()]);
            menu.findItem(R.id.menu_btn_filter).setVisible(showFilterInActionBar[pager.getCurrentItem()]);
            menu.findItem(R.id.menu_btn_refresh).setVisible(showRefreshInActionBar[pager.getCurrentItem()]);
        }

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
        if (i == R.id.menu_btn_sort) {
            L.v("item=menu_btn_sort");
            if (lastFragment != null) {
                if (lastFragment instanceof DevGamesTab.OnSortActionListener) {
                    // If it is the StatusFragment, pass it on
                    ((DevGamesTab.OnSortActionListener) lastFragment).onSortRequest();
                }
                else {
                    L.w("Sort action was clicked, but lastFragment does not implement TeamUpTabPage.OnSortActionListener!");
                    return false;
                }
            }
            return true;
        }
        else if (i == R.id.menu_btn_filter) {
            L.v("item=menu_btn_filter");
            if (lastFragment != null) {
                if (lastFragment instanceof DevGamesTab.OnFilterRequestListener) {
                    ((DevGamesTab.OnFilterRequestListener) lastFragment).onFilterRequest();
                }
                else {
                    L.w("Filter action was clicked, but lastFragment does not implement TeamUpTabPage.OnFilterRequestListener!");
                    return false;
                }
            }
            return true;
        }
        else if (i == R.id.menu_btn_refresh) {
            L.v("item=menu_btn_refresh");
            if (lastFragment != null) {
                if (lastFragment instanceof DevGamesTab.OnRefreshRequestListener) {
                    ((DevGamesTab.OnRefreshRequestListener) lastFragment).onRefreshRequest();
                }
                else {
                    L.w("Refresh action was clicked, but lastFragment does not implement TeamUpTabPage.OnRefreshRequestListener!");
                    return false;
                }
            }
            return true;
        }
        else if (i == R.id.menu_btn_settings) {
            //startActivity(new Intent(this, SettingsActivity.class));

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

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (adapter != null && pager != null) {
            Fragment lastFragment = (Fragment) adapter.instantiateItem(pager, pager.getCurrentItem());

            if (lastFragment != null) {
                if (lastFragment instanceof DevGamesTab.OnSearchActionListener) {
                    ((DevGamesTab.OnSearchActionListener) lastFragment).onQueryTextSubmit(query);
                }
                else {
                    L.w("onQueryTextSubmit called, but lastFragment does not implement OnSearchActionListener");
                }
            }
            else {
                L.w("onQueryTextSubmit called, but lastFragment is null!");
            }
        } else {
            L.w("adapter ({0}) or pager ({1}) is null!", adapter, pager);
        }

        if (searchView != null) {
            searchView.clearFocus();
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (adapter != null && pager != null) {
            Fragment lastFragment = (Fragment) adapter.instantiateItem(pager, pager.getCurrentItem());

            if (lastFragment != null) {
                if (lastFragment instanceof DevGamesTab.OnSearchActionListener) {
                    ((DevGamesTab.OnSearchActionListener) lastFragment).onQueryTextChange(newText);
                }
                else {
                    L.w("onQueryTextChange called, but lastFragment does not implement OnSearchActionListener");
                }
            }
            else {
                L.w("onQueryTextChange called, but lastFragment is null!");
            }
        } else {
            L.w("adapter ({0}) or pager ({1}) is null!", adapter, pager);
        }

        return true;
    }

    @Subscribe
    @Override
    public void onLogoutEvent(LogoutEvent event) {
        super.onLogoutEvent(event);
    }
}
