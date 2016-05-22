package baecon.devgames.util;

import java.util.Comparator;

import baecon.devgames.model.Push;

public class PushNameComparator implements Comparator<Push> {

    @Override
    public int compare(Push a, Push b) {
        String aName = a != null && a.getProject() != null ? a.getProject().getName() : "";
        String bName = b != null && b.getProject() != null ? b.getProject().getName() : "";

        return aName.compareToIgnoreCase(bName);
    }
}
