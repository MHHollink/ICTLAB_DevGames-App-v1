package baecon.devgames.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import java.lang.ref.WeakReference;

/**
 * Sub-class of {@link SwipeRefreshLayout} for use in this
 * {@link android.support.v4.app.ListFragment}. The reason that this is needed is because
 * {@link SwipeRefreshLayout} only supports a single child, which it
 * expects to be the one which triggers refreshes. In our case the layout's child is the content
 * view returned from
 * {@link android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
 * which is a {@link android.view.ViewGroup}.
 *
 * <p>To enable 'swipe-to-refresh' support via the {@link ListView} we need to
 * override the default behavior and properly signal when a gesture is possible. This is done by
 * overriding {@link #canChildScrollUp()}.
 */
public class DevGamesSwipeRefreshLayout extends SwipeRefreshLayout {

    private WeakReference<ListView> listView;

    public DevGamesSwipeRefreshLayout(Context context) {
        super(context);
    }

    public DevGamesSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListView(ListView listView) {
        this.listView = new WeakReference<>(listView);
    }

    /**
     * As mentioned above, we need to override this method to properly signal when a
     * 'swipe-to-refresh' is possible.
     *
     * @return true if the {@link ListView} is visible and can scroll up.
     */
    @Override
    public boolean canChildScrollUp() {
        final ListView listView = this.listView.get();
        return listView != null && listView.getVisibility() == View.VISIBLE && canListViewScrollUp(listView);
    }

    /**
     * Utility method to check whether a {@link ListView} can scroll up from it's current position.
     * Handles platform version differences, providing backwards compatible functionality where
     * needed.
     */
    private static boolean canListViewScrollUp(ListView listView) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            // For ICS and above we can call canScrollVertically() to determine this
            return ViewCompat.canScrollVertically(listView, -1);
        }
        else {
            // Pre-ICS we need to manually check the first visible item and the child view's top
            // value
            return listView.getChildCount() > 0 &&
                    (listView.getFirstVisiblePosition() > 0
                            || listView.getChildAt(0).getTop() < listView.getPaddingTop());
        }
    }
}
