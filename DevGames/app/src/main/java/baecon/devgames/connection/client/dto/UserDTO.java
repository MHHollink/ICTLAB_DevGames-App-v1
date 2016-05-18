package baecon.devgames.connection.client.dto;

import java.util.Set;

import baecon.devgames.model.Project;
import baecon.devgames.model.Push;
import baecon.devgames.model.User;

public class UserDTO implements ModelDTO<User> {

    private Long id;
    public String username;
    public String gitUsername;

    public String firstName;
    public String tween;
    public String lastName;

    public int age;
    public String mainJob;

    public Set<Project> projects;
    public Set<Push> pushes;

    public String gcmId;

    public UserDTO(User user){
        id = user.getId();
        username = user.getUsername();
        gitUsername = user.getGitUsername();
        firstName = user.getFirstName();
        tween = user.getTween();
        lastName = user.getLastName();
        age = user.getAge();
        mainJob = user.getMainJob();
        gcmId = user.getGcmId();
    }

    public UserDTO() {
    }

    @Override
    public User toModel() {

        User user = new User();

        user.setId(id);
        user.setUsername(username);
        user.setGitUsername(gitUsername);
        user.setGcmId(gcmId);
        user.setAge(age);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return user;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getGitUsername() {
        return gitUsername;
    }

    public void setGitUsername(String gitUsername) {
        this.gitUsername = gitUsername;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMainJob() {
        return mainJob;
    }

    public void setMainJob(String mainJob) {
        this.mainJob = mainJob;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Push> getPushes() {
        return pushes;
    }

    public void setPushes(Set<Push> pushes) {
        this.pushes = pushes;
    }

    public String getTween() {
        return tween;
    }

    public void setTween(String tween) {
        this.tween = tween;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "age=" + age +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", gitUsername='" + gitUsername + '\'' +
                ", firstName='" + firstName + '\'' +
                ", tween='" + tween + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mainJob='" + mainJob + '\'' +
                ", projects=" + projects +
                ", pushes=" + pushes +
                ", gcmId='" + gcmId + '\'' +
                '}';
    }
}
