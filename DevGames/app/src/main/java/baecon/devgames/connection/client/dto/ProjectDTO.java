package baecon.devgames.connection.client.dto;

import java.util.Set;

import baecon.devgames.model.Commit;
import baecon.devgames.model.Project;
import baecon.devgames.model.User;


public class ProjectDTO implements ModelDTO<Project> {

    private long id;
    private String name;
    private String description;
    private User owner;
    private Set<User> developers;
    private Set<Commit> commits;

    public ProjectDTO(Project project) {

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Project toModel() {
        Project project = new Project();

        project.setId(id);
        project.setName(name);
        project.setDescription(description);
        project.setOwner(owner);

        return project;
    }

    public Set<Commit> getCommits() {
        return commits;
    }

    public void setCommits(Set<Commit> commits) {
        this.commits = commits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<User> developers) {
        this.developers = developers;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "commits=" + commits +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", developers=" + developers +
                '}';
    }
}
