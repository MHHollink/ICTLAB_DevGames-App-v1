package baecon.devgames.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import baecon.devgames.database.DBHelper;

/**
 * Created by Marcel on 27-4-2016.
 */
@DatabaseTable(tableName = DBHelper.Tables.DUPLICATIONS)
public class Duplication extends AbsSynchronizable implements Serializable {

    public static class Column {
        public static final String PUSHED_IN = "pushedIn";
    }

    @DatabaseField(columnName = Column.PUSHED_IN, foreign = true, foreignAutoRefresh = true)
    private Push pushedIn;

    public Duplication() {
        // Empty for ORM lite
    }

    public Duplication(Long id, Push pushedIn) {
        super(id);
        this.pushedIn = pushedIn;
    }

    public Push getPushedIn() {
        return pushedIn;
    }

    public void setPushedIn(Push pushedIn) {
        this.pushedIn = pushedIn;
    }

    @Override
    public boolean contentEquals(Object other) {
        return false;
    } // TODO: 09-5-2016

    @Override
    public String toString() {
        return "Duplication{" +
                "pushedIn=" + pushedIn +
                "} " + super.toString();
    }
}
