package baecon.devgames.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import baecon.devgames.database.DBHelper;

@DatabaseTable(tableName = DBHelper.Tables.PUSHES)
public class Push extends AbsSynchronizable implements Serializable {

    public static class Column{
        public static final String PROJECT = "project";
        public static final String PUSHER = "pusher";
        public static final String TIMESTAMP = "timestamp";
        public static final String SCORE = "score";
    }

    @DatabaseField(columnName = Column.PROJECT, dataType = DataType.SERIALIZABLE, foreign = true, foreignAutoRefresh = true)
    private Project project;

    @DatabaseField(columnName = Column.PUSHER, dataType = DataType.SERIALIZABLE, foreign = true, foreignAutoRefresh = true)
    private User pusher;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<Commit> commits;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<Issue> issues;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<Duplication> duplications;

    @DatabaseField(columnName = Column.TIMESTAMP)
    private long timestamp;

    @DatabaseField(columnName = Column.SCORE)
    private double score;

    public Push() {
    }

    public Push(Long id, Project project, User pusher, double score, long timestamp) {
        super(id);
        this.project = project;
        this.pusher = pusher;
        this.score = score;
        this.timestamp = timestamp;
    }

    public ForeignCollection<Commit> getCommits() {
        return commits;
    }

    public ForeignCollection<Duplication> getDuplications() {
        return duplications;
    }

    public ForeignCollection<Issue> getIssues() {
        return issues;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getPusher() {
        return pusher;
    }

    public void setPusher(User pusher) {
        this.pusher = pusher;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Push)) return false;
        if (!super.equals(o)) return false;

        Push push = (Push) o;

        return !(getId() != null ? !(getId().longValue() == push.getId().longValue()) : push.getId() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }

    @Override
    public boolean contentEquals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Push)) return false;
        if (!super.equals(o)) return false;

        Push push = (Push) o;

        return getTimestamp() == push.getTimestamp() &&
                Double.compare(push.getScore(), getScore()) == 0 &&
                !(getProject() != null ? !getProject().equals(push.getProject()) : push.getProject() != null) &&
                !(getCommits() != null ? !getCommits().equals(push.getCommits()) : push.getCommits() != null) &&
                !(getIssues() != null ? !getIssues().equals(push.getIssues()) : push.getIssues() != null) &&
                !(getDuplications() != null ? !getDuplications().equals(push.getDuplications()) : push.getDuplications() != null);
    }
}
