package baecon.devgames.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import baecon.devgames.database.DBHelper;

@DatabaseTable(tableName = DBHelper.Tables.COMMITS)
public class Commit extends AbsSynchronizable implements Serializable {

    public static class Column {
        public static final String PUSHED_IN = "pushedIn";
        public static final String TITLE = "title";
        public static final String HASH = "hash";
        public static final String BRANCH = "branch";
        public static final String FILES_CHANGED = "filesChanges";
        public static final String TIME = "timestamp";
    }


    @DatabaseField(columnName = Column.PUSHED_IN, foreign = true, foreignAutoRefresh = true)
    private Push pushedIn;

    @DatabaseField(columnName = Column.TITLE)
    private String title;

    @DatabaseField(columnName = Column.HASH)
    private String hash;

    @DatabaseField(columnName = Column.BRANCH)
    private String branch;

    @DatabaseField(columnName = Column.FILES_CHANGED)
    private int filesChanges;

    @DatabaseField(columnName = Column.TIME)
    private long timestamp;

    public Commit(long id, Push pushedIn, String title, String hash, String branch, int filesChanges, long timestamp) {
        super(id);
        this.pushedIn = pushedIn;
        this.title = title;
        this.hash = hash;
        this.branch = branch;
        this.filesChanges = filesChanges;
        this.timestamp = timestamp;
    }

    public Commit() {

    }

    public Push getPushedIn() {
        return pushedIn;
    }

    public void setPushedIn(Push pushedIn) {
        this.pushedIn = pushedIn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getFilesChanges() {
        return filesChanges;
    }

    public void setFilesChanges(int filesChanges) {
        this.filesChanges = filesChanges;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public boolean contentEquals(Object other) {

        Commit o = (Commit) other;

        return id.equals(o.getId()) &&
                timestamp == o.getTimestamp() &&
                pushedIn.equals(o.getPushedIn()) &&
                title.equals(o.getTitle()) &&
                hash.equals(o.getHash()) &&
                branch.equals(o.getBranch()) &&
                filesChanges == o.getFilesChanges();

    }

}
