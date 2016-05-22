package baecon.devgames.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.R;
import baecon.devgames.model.Push;
import baecon.devgames.util.L;
import baecon.devgames.util.SortOption;

/**
 * Created by Marcel on 20-5-2016.
 */
public class PushAdapter extends ModelListAdapter<Push> {

    public PushAdapter(Context context) {
        super(context);
    }

    public PushAdapter(Context context, SortOption<Push> defaultSortOption) {
        super(context, defaultSortOption);
    }

    @Override
    public View newView(int position, LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.push_listview_item, parent, false);

        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(int position, Push push, boolean needsHeader, View view) {

        ViewHolder holder = (ViewHolder) view.getTag();

        // If the push is null, use ugly default values
        if (push == null) {
            L.w("Push is null at position {0}! Using default values.", position);
            holder.txtName.setText("");
            holder.txtTime.setText("");
            holder.txtCommits.setText("");
            holder.txtIssues.setText("");
            holder.txtDuplications.setText("");
//            holder.txtScore.setText("");
        }
        else {

            try {
                holder.txtName.setText(
                        push.getCommits()
                                .iterator()
                                .next()
                                .getTitle()
                );
            } catch (IndexOutOfBoundsException e) {
                // Iterator.next without check might throw IndexOutOfBoundsException
                holder.txtName.setText(R.string.no_name_available);
            }

//            holder.txtScore.setText(
//                    String.valueOf(push.getScore())
//            );

            holder.txtCommits.setText(
                    String.valueOf(push.getCommits() != null ? push.getCommits().size() : 0)
            );

            holder.txtDuplications.setText(
                    String.valueOf(push.getDuplications() != null ? push.getDuplications().size() : 0)
            );

            holder.txtIssues.setText(
                    String.valueOf(push.getIssues() != null ? push.getIssues().size() : 0)
            );

            holder.txtTime.setText(
                    DevGamesApplication.get(getContext())
                            .formatterDayMonthHourMinute.format(new Date(push.getTimestamp()))
            );
        }
    }

    static class ViewHolder {

        /**** CONTENT ****/
        final TextView txtName;
        final TextView txtCommits;
        final TextView txtDuplications;
        final TextView txtIssues;
//        final TextView txtScore;
        final TextView txtTime;

        ViewHolder(View view) {
            // Content stuff
            this.txtName = (TextView) view.findViewById(R.id.txt_name);
            this.txtCommits = (TextView) view.findViewById(R.id.txt_commits);
            this.txtDuplications = (TextView) view.findViewById(R.id.txt_duplications);
            this.txtIssues = (TextView) view.findViewById(R.id.txt_issues);
//            this.txtScore = (TextView) view.findViewById(R.id.txt_score);
            this.txtTime = (TextView) view.findViewById(R.id.txt_time);

        }
    }
}
