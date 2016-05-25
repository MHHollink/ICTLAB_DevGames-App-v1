package baecon.devgames.util;

import java.util.Comparator;

import baecon.devgames.model.Project;

/**
 * Created by Marcel on 19-5-2016.
 */
public class ProjectScoreComparator implements Comparator<Project> {

    @Override
    public int compare(Project a, Project b) {
        double aScore = a != null ? a.getDevelopers().size() : 0;
        double bScore = b != null ? b.getDevelopers().size() : 0;

        // ascending order
        return aScore > bScore ? -1 : 1;
    }
}
