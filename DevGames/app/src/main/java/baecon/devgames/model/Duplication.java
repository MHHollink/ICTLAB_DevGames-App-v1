package baecon.devgames.model;

import java.io.Serializable;

/**
 * Created by Marcel on 27-4-2016.
 */
public class Duplication extends AbsSynchronizable implements Serializable {


    @Override
    public boolean contentEquals(Object other) {
        return false;
    }
}
