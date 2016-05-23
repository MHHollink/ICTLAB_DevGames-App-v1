package baecon.devgames.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import baecon.devgames.model.Commit;
import baecon.devgames.util.SortOption;

/**
 * TODO: write class level documentation.
 *
 * @author Marcel
 * @since 22-5-2016.
 */
public class CommitAdapter extends ModelListAdapter<Commit> {

    public CommitAdapter(Context context) {
        super(context);
    }

    public CommitAdapter(Context context, SortOption<Commit> defaultSortOption) {
        super(context, defaultSortOption);
    }

    @Override
    public View newView(int position, LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(int position, Commit model, boolean needsHeader, View view) {

    }
}
