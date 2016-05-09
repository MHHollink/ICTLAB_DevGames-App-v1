package baecon.devgames.model;

import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import baecon.devgames.database.DBHelper;

/**
 * Created by Marcel on 27-4-2016.
 */
@DatabaseTable(tableName = DBHelper.Tables.DUPLICATIONS)
public class Duplication extends AbsSynchronizable implements Serializable {


    @Override
    public boolean contentEquals(Object other) {
        return false;
    }
}
