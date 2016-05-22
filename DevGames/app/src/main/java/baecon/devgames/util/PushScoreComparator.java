package baecon.devgames.util;

import java.util.Comparator;

import baecon.devgames.model.Push;

public class PushScoreComparator implements Comparator<Push> {
    @Override
    public int compare(Push a, Push b) {
        double aScore = a != null ? a.getScore() : 0;
        double bScore = b != null ? b.getScore() : 0;

        return aScore > bScore ? -1 : 1;
    }
}
