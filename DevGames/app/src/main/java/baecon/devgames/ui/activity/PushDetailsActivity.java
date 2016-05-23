package baecon.devgames.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Stack;

import baecon.devgames.R;
import baecon.devgames.connection.synchronization.CommitManager;
import baecon.devgames.connection.synchronization.DuplicationManager;
import baecon.devgames.connection.synchronization.IssueManager;
import baecon.devgames.connection.synchronization.PushManager;
import baecon.devgames.ui.fragment.PushCommitsFragment;
import baecon.devgames.ui.fragment.PushDetailFragment;
import baecon.devgames.ui.widget.SlidingTabLayout;
import baecon.devgames.util.L;
import baecon.devgames.util.ViewPageAdapter;

/**
 * Created by Marcel on 20-5-2016.
 */
public class PushDetailsActivity extends DevGamesActivity {

    public static final String PUSH_ID = "push_id";

    public static final boolean[] showSearchInActionBar  = new boolean[]{false, false};
    public static final boolean[] showSortInActionBar    = new boolean[]{false,  true};
    public static final boolean[] showFilterInActionBar  = new boolean[]{false, false};
    public static final boolean[] showRefreshInActionBar = new boolean[]{false, false};

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

        PushDetailFragment pushDetailFragment = new PushDetailFragment()
                .setTitle("Push information");
        Bundle bundle = new Bundle(1);
        long pushId = getIntent().getLongExtra(PUSH_ID, 0L);
        if(pushId == 0) throw new RuntimeException("Push id was 0, this should not be possible!");
        bundle.putLong(PUSH_ID, pushId);

        PushCommitsFragment commitsFragment = new PushCommitsFragment()
                .setTitle("Commits");

        // Add pages to the viewpager
        adapter.addTab(
                pushDetailFragment,
                commitsFragment
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(R.string.push_details);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        resumed = true;

        PushManager.get(this).startForegroundSyncMode();
        CommitManager.get(this).startForegroundSyncMode();
        IssueManager.get(this).startForegroundSyncMode();
        DuplicationManager.get(this).startForegroundSyncMode();
    }

    @Override
    protected void onPause() {
        super.onPause();

        resumed = false;

        PushManager.get(this).stopForegroundSyncMode();
//        CommitManager.get(this).stopForegroundSyncMode();
//        IssueManager.get(this).stopForegroundSyncMode();
//        DuplicationManager.get(this).stopForegroundSyncMode();
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

        return false;
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

}
