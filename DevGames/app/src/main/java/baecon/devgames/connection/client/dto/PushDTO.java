package baecon.devgames.connection.client.dto;

import java.util.Set;

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
    private Set<Commit> commits;
    private Set<Issue> issues;
    private Set<Duplication> duplications;
    private long timestamp;
    private double score;

    public PushDTO(Push push) {

    }

    @Override
    public Long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
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
    public Push toModel() {
        Push push = new Push();

        push.setId(id);
        push.setScore(score);
        push.setTimestamp(timestamp);

        return push;
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
