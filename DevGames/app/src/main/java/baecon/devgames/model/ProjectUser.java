package baecon.devgames.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import baecon.devgames.database.DBHelper;

@DatabaseTable(tableName = DBHelper.Tables.PROJECT_USER)
public class ProjectUser implements Serializable {

    public static class Column {
        public static final String ID = "id";
        public static final String USER = "user";
        public static final String PROJECT = "project";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    private long id;

    @DatabaseField(columnName = Column.USER, foreign = true)
    private User user;

    @DatabaseField(columnName = Column.PROJECT, foreign = true)
    private Project project;

    public ProjectUser() {
        // Empty constructor for ORMLite
    }

    public ProjectUser(User user, Project project) {
        this.user = user;
        this.project = project;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Project getProject() {
        return project;
    }
}