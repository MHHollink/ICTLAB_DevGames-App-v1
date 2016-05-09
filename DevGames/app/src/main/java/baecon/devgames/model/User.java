package baecon.devgames.model;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.HashMap;

import baecon.devgames.database.DBHelper;
import baecon.devgames.util.Utils;

@DatabaseTable(tableName = DBHelper.Tables.USERS)
public class User extends AbsSynchronizable implements Serializable {

    public static class Column{
        public static final String USERNAME = "username";
        public static final String GIT_USER = "git_username";
        public static final String PROJECTS = "projects";
        public static final String PUSH = "pushes";
        public static final String GCM_KEY = "gcm_registration_key";
        public static final String FIRST_NAME = "fname";
        public static final String LAST_NAME = "lname";
        public static final String TWEEN = "tween";
        public static final String AGE = "age";
        public static final String JOB = "job";
    }

    @DatabaseField(columnName = Column.USERNAME)
    private String username;

    @DatabaseField(columnName = Column.GIT_USER)
    private String gitUsername;

    @DatabaseField(columnName = Column.GCM_KEY)
    private String gcmKey;

    @DatabaseField(columnName = Column.FIRST_NAME)
    private String firstName;

    @DatabaseField(columnName = Column.TWEEN)
    private String tween;

    @DatabaseField(columnName = Column.LAST_NAME)
    private String lastName;

    @DatabaseField(columnName = Column.AGE)
    private int age;

    @DatabaseField(columnName = Column.JOB)
    private String mainJob;

    @DatabaseField(columnName = Column.PROJECTS, dataType = DataType.SERIALIZABLE)
    private HashMap<Long, Project> projects;

    @DatabaseField(columnName = Column.PUSH, dataType = DataType.SERIALIZABLE)
    private HashMap<Long, Push> pushes;

    public User(Long id, String username, String gitUsername, HashMap<Long, Project> projects, String gcmKey, String firstName, String tween, String lastName, int age, String mainJob, HashMap<Long, Push> pushes) {
        super(id);
        this.username = username;
        this.gitUsername = gitUsername;
        this.projects = projects;
        this.gcmKey = gcmKey;
        this.firstName = firstName;
        this.tween = tween;
        this.lastName = lastName;
        this.age = age;
        this.mainJob = mainJob;
        this.pushes = pushes;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGitUsername() {
        return gitUsername;
    }

    public void setGitUsername(String gitUsername) {
        this.gitUsername = gitUsername;
    }

    public HashMap<Long, Project> getProjects() {
        return projects;
    }

    public void setProjects(HashMap<Long, Project> projects) {
        this.projects = projects;
    }

    public String getGcmKey() {
        return gcmKey;
    }

    public void setGcmKey(String gcmKey) {
        this.gcmKey = gcmKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTween() {
        return tween;
    }

    public void setTween(String tween) {
        this.tween = tween;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMainJob() {
        return mainJob;
    }

    public void setMainJob(String mainJob) {
        this.mainJob = mainJob;
    }

    public HashMap<Long, Push> getPushes() {
        return pushes;
    }

    public void setPushes(HashMap<Long, Push> pushes) {
        this.pushes = pushes;
    }

    public void merge(User user) {
        if(!Utils.isEmpty(user.getUsername())) {
            username = user.getUsername();
        }
        if(!Utils.isEmpty(user.getGitUsername())) {
            gitUsername = user.getGitUsername();
        }
        if(!Utils.isEmpty(user.getFirstName())) {
            firstName = user.getFirstName();
        }
        if(!Utils.isEmpty(user.getTween())) {
            tween = user.getTween();
        }
        if(!Utils.isEmpty(user.getLastName())) {
            lastName = user.getLastName();
        }
        if(age != 0) {
            age = user.getAge();
        }
        if(!Utils.isEmpty(user.getProjects())) {
            projects = user.getProjects();
        }
        if(!Utils.isEmpty(user.getPushes())) {
            pushes = user.getPushes();
        }
        if(!Utils.isEmpty(user.getGcmKey())) {
            gcmKey = user.getGcmKey();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        return id.longValue() == user.getId().longValue();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (gitUsername != null ? gitUsername.hashCode() : 0);
        result = 31 * result + (projects != null ? projects.hashCode() : 0);
        result = 31 * result + (gcmKey != null ? gcmKey.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (tween != null ? tween.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (mainJob != null ? mainJob.hashCode() : 0);
        result = 31 * result + (pushes != null ? pushes.hashCode() : 0);
        return result;
    }

    public double getScore() {
        double score=0;
        if(pushes != null && !pushes.isEmpty()) {
            for (Push push : pushes.values())
                score += push.getScore();
        }
        return score;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", gitUsername='" + gitUsername + '\'' +
                ", projects=" + projects +
                ", gcmKey='" + gcmKey + '\'' +
                ", firstName='" + firstName + '\'' +
                ", tween='" + tween + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", mainJob='" + mainJob + '\'' +
                ", pushes=" + pushes +
                "} " + super.toString();
    }

    @Override
    public boolean contentEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        return getAge() == user.getAge() &&
                !(getUsername() != null ? !getUsername().equals(user.getUsername()) : user.getUsername() != null) &&
                !(getGitUsername() != null ? !getGitUsername().equals(user.getGitUsername()) : user.getGitUsername() != null) &&
                !(getProjects() != null ? !getProjects().equals(user.getProjects()) : user.getProjects() != null) &&
                !(getGcmKey() != null ? !getGcmKey().equals(user.getGcmKey()) : user.getGcmKey() != null) &&
                !(getFirstName() != null ? !getFirstName().equals(user.getFirstName()) : user.getFirstName() != null) &&
                !(getTween() != null ? !getTween().equals(user.getTween()) : user.getTween() != null) &&
                !(getLastName() != null ? !getLastName().equals(user.getLastName()) : user.getLastName() != null) &&
                !(getMainJob() != null ? !getMainJob().equals(user.getMainJob()) : user.getMainJob() != null) &&
                !(getPushes() != null ? !getPushes().equals(user.getPushes()) : user.getPushes() != null);
    }

    public void logout() {
        setGcmKey(null);
    }
}
