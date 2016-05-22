package baecon.devgames.util;

import java.util.Comparator;

import baecon.devgames.model.Project;

/**
 * Created by Marcel on 19-5-2016.
 */
public class ProjectNameComparator implements Comparator<Project> {

    @Override
    public int compare(Project a, Project b) {

        String aName = a != null ? Utils.valueOrDefault(a.getName(), "") : "";
        String bName = b != null ? Utils.valueOrDefault(b.getName(), "") : "";

        // ascending order
        return aName.compareToIgnoreCase(bName);
    }
}
