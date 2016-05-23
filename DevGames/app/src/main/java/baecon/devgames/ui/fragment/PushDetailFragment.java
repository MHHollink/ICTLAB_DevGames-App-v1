package baecon.devgames.ui.fragment;

import android.support.v4.app.Fragment;

/**
 * TODO: write class level documentation.
 *
 * @author Marcel
 * @since 22-5-2016.
 */
public class PushDetailFragment extends DevGamesFragment implements DevGamesTab {

    private String title = "";

    @Override
    public Fragment getFragmentFromTab() {
        return getFragment();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public PushDetailFragment setTitle(String s) {
        title = s;
        return this;
    }
}
