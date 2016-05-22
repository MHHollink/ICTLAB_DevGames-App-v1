package baecon.devgames.util;

import java.util.Comparator;

import baecon.devgames.model.Push;

/**
 * Created by Marcel on 20-5-2016.
 */
public class PushTimeComparator implements Comparator<Push> {
    @Override
    public int compare(Push a, Push b) {

        long aTime = a != null ? a.getTimestamp() : 0;
        long bTime = b != null ? b.getTimestamp() : 0;

        return aTime > bTime ? -1 : 1;
    }
}
