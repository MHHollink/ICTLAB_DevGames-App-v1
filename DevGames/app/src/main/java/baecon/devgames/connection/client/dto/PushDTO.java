package baecon.devgames.connection.client.dto;

import java.util.HashMap;

import baecon.devgames.model.Commit;
import baecon.devgames.model.Duplication;
import baecon.devgames.model.Issue;
import baecon.devgames.model.Project;
import baecon.devgames.model.Push;

/**
 * Created by Marcel on 27-4-2016.
 */
public class PushDTO implements ModelDTO<Push> {

    private long id;
    private Project project;
    private HashMap<Long, Commit> commits;
    private HashMap<Long, Issue> issues;
    private HashMap<Long, Duplication> duplications;
    private long timestamp;
    private double score;

    @Override
    public Long getId() {
        return id;
    }

    public HashMap<Long, Commit> getCommits() {
        return commits;
    }

    public void setCommits(HashMap<Long, Commit> commits) {
        this.commits = commits;
    }

    public HashMap<Long, Duplication> getDuplications() {
        return duplications;
    }

    public void setDuplications(HashMap<Long, Duplication> duplications) {
        this.duplications = duplications;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HashMap<Long, Issue> getIssues() {
        return issues;
    }

    public void setIssues(HashMap<Long, Issue> issues) {
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
    public Push toModel() {
        return null;
    } // TODO: 09-5-2016

    @Override
    public String toString() {
        return "PushDTO{" +
                "commits=" + commits +
                ", id=" + id +
                ", project=" + project +
                ", issues=" + issues +
                ", duplications=" + duplications +
                ", timestamp=" + timestamp +
                ", score=" + score +
                '}';
    }
}
