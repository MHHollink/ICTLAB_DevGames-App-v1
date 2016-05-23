package baecon.devgames.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import baecon.devgames.R;
import baecon.devgames.model.Project;
import baecon.devgames.util.L;
import baecon.devgames.util.SortOption;

/**
 * Created by Marcel on 19-5-2016.
 */
public class ProjectsAdapter extends ModelListAdapter<Project> {

    protected final int avatarBackgroundColorOdd, avatarBackgroundColorEven;

    public ProjectsAdapter(Context context, SortOption<Project> defaultSortOption) {
        super(context, defaultSortOption);

        avatarBackgroundColorOdd = context.getResources().getColor(R.color.almond_light_odd_list_item);
        avatarBackgroundColorEven = context.getResources().getColor(R.color.almond_light);

        setHeadersEnabled(false);
    }

    @Override
    public View newView(int position, LayoutInflater inflater, ViewGroup parent) {

        View view = inflater.inflate(R.layout.project_listview_item, parent, false);

        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(int position, Project project, boolean needsHeader, View view) {

        ViewHolder holder = (ViewHolder) view.getTag();

        // Apply different colors to odd and even rows
        if (position % 2 == 1) {
            view.setBackgroundResource(R.drawable.list_entry_almond_even);
            holder.avatar.setBackgroundColor(avatarBackgroundColorOdd);
        }
        else {
            view.setBackgroundResource(R.drawable.list_entry_almond_odd);
            holder.avatar.setBackgroundColor(avatarBackgroundColorEven);
        }

        // If the project is null, use ugly default values
        if (project == null) {
            L.w("Project is null at position {0}! Using default values.", position);
            holder.txtName.setText("");
            holder.txtDevelopers.setText("");
            holder.txtScore.setText("");
            holder.txtScore.setText("");
            holder.avatar.setImageResource(R.drawable.no_profile_avatar);
        }
        else {

            // Full name of the client
            holder.txtName.setText(project.getName());
            holder.txtScore.setText(String.valueOf(project.getScore()));
            holder.txtDevelopers.setText(
                    project.getDevelopers() != null ? String.valueOf( project.getDevelopers().size()) : "?"
            );
            holder.avatar.setImageResource(R.drawable.no_project_avatar);
        }
    }

    static class ViewHolder {

        /**** CONTENT ****/
        final ImageView avatar;
        final TextView txtName;
        final TextView txtDevelopers;
        final TextView txtScore;

        ViewHolder(View view) {
            // Content stuff
            this.avatar = (ImageView) view.findViewById(R.id.avatar_view);
            this.txtName = (TextView) view.findViewById(R.id.txt_name);
            this.txtDevelopers = (TextView) view.findViewById(R.id.txt_developers);
            this.txtScore = (TextView) view.findViewById(R.id.txt_score);

        }
    }
}

