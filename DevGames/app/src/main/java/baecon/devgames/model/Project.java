package baecon.devgames.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.HashMap;

@DatabaseTable(tableName = "projects")
public class Project extends AbsSynchronizable implements Serializable {

    public static class Column {
        public static final String OWNER = "owner";
        public static final String DEVELOPERS = "developers";
        public static final String PUSHES = "pushes";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
    }
    @DatabaseField(columnName = Column.OWNER, dataType = DataType.SERIALIZABLE, foreign = true, foreignAutoRefresh = true)
    private User owner;

    @DatabaseField(columnName = Column.DEVELOPERS, dataType = DataType.SERIALIZABLE)
    private HashMap<Long, User> developers;

    @DatabaseField(columnName = Column.PUSHES, dataType = DataType.SERIALIZABLE)
    private HashMap<Long, Push> pushes;

    @DatabaseField(columnName = Column.NAME)
    private String name;

    @DatabaseField(columnName = Column.DESCRIPTION)
    private String description;

    public Project(){
    }

    public Project(Long id, String description, HashMap<Long, User> developers, String name, User owner, HashMap<Long, Push> pushes) {
        super(id);
        this.description = description;
        this.developers = developers;
        this.name = name;
        this.owner = owner;
        this.pushes = pushes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<Long, User> getDevelopers() {
        return developers;
    }

    public void setDevelopers(HashMap<Long, User> developers) {
        this.developers = developers;
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

    public HashMap<Long, Push> getPushes() {
        return pushes;
    }

    public void setPushes(HashMap<Long, Push> pushes) {
        this.pushes = pushes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        if (!super.equals(o)) return false;

        Project project = (Project) o;

        return !(getId() != null ? !(getId().longValue() == project.getId().longValue()) : project.getId() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getOwner() != null ? getOwner().hashCode() : 0);
        return result;
    }

    @Override
    public boolean contentEquals(Object other) {
        return false;
    }

}
