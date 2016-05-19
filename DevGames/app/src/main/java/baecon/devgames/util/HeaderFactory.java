package baecon.devgames.util;

import android.content.Context;

/**
 * TODO: provide class level documentation
 */
public interface HeaderFactory<Model> {

    /**
     * Returns true when a new group of items in a List begins, otherwise false.
     *
     * @return true when a new group of items in a List begins, otherwise false.
     */
    boolean needsHeader(Model current, Model previous);

    Header produce(Context context, Model model);
}
