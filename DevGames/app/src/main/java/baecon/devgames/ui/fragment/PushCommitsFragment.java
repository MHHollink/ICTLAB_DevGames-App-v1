package baecon.devgames.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import baecon.devgames.connection.synchronization.IModelManager;
import baecon.devgames.database.loader.ModelListLoader;
import baecon.devgames.model.Commit;
import baecon.devgames.ui.widget.CommitAdapter;
import baecon.devgames.ui.widget.ModelListAdapter;
import baecon.devgames.util.SortOption;

/**
 * TODO: write class level documentation.
 *
 * @author Marcel
 * @since 22-5-2016.
 */
public class PushCommitsFragment extends ModelListFragment<Commit, Long> implements DevGamesTab {

    private String title;

    @Override
    public IModelManager getSyncManager() {
        return null;
    }

    @Override
    public List<SortOption<Commit>> getSortOptions() {
        return null;
    }

    @Override
    public List<FilterOption> getFilterOptions() {
        return null;
    }

    @Override
    protected ModelListAdapter<Commit> onCreateModelListAdapter(Context context) {
        return new CommitAdapter(context);
    }

    @Override
    public ModelListLoader<Commit, Long> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public Fragment getFragmentFromTab() {
        return getFragment();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public PushCommitsFragment setTitle(String s) {
        title = s;
        return this;
    }
}
