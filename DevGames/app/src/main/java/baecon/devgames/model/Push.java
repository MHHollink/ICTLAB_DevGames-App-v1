package baecon.devgames.model;

import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Set;

@DatabaseTable(tableName = "pushes")
public class Push extends AbsSynchronizable implements Serializable {

    public static class Column{
        public static final String PROJECT = "project";
        public static final String COMMITS = "commits";
        public static final String ISSUES = "issues";
        public static final String DUPLICATION = "duplications";
        public static final String TIMESTAMP = "timestamp";
        public static final String SCORE = "score";
    }

    private Project project;
    private Set<Commit> commits;
    private Set<Issue> issues;
    private Set<Duplication> duplications;

    private long timestamp;
    private double score;

    public Push() {
    }

    public Push(Long id, Set<Commit> commits, Set<Duplication> duplications, Set<Issue> issues, Project project, double score, long timestamp) {
        super(id);
        this.commits = commits;
        this.duplications = duplications;
        this.issues = issues;
        this.project = project;
        this.score = score;
        this.timestamp = timestamp;
    }

    public Set<Commit> getCommits() {
        return commits;
    }

    public void setCommits(Set<Commit> commits) {
        this.commits = commits;
    }

    public Set<Duplication> getDuplications() {
        return duplications;
    }

    public void setDuplications(Set<Duplication> duplications) {
        this.duplications = duplications;
    }

    public Set<Issue> getIssues() {
        return issues;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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
