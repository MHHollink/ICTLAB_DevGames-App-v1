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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Project toModel() {
        Project project = new Project();

        project.setName(name);
        project.setDescription(description);
        project.setOwner(owner);

        return project;
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
