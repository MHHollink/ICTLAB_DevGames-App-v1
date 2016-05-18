package baecon.devgames.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import baecon.devgames.database.DBHelper;

@DatabaseTable(tableName = DBHelper.Tables.PROJECTS)
public class Project extends AbsSynchronizable implements Serializable {

    public static class Column {
        public static final String OWNER = "owner";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
    }
    @DatabaseField(columnName = Column.OWNER, dataType = DataType.SERIALIZABLE, foreign = true, foreignAutoRefresh = true)
    private User owner;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<User> developers;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<Push> pushes;

    @DatabaseField(columnName = Column.NAME)
    private String name;

    @DatabaseField(columnName = Column.DESCRIPTION)
    private String description;

    public Project(){
    }

    public Project(Long id, String description, String name, User owner) {
        super(id);
        this.description = description;
        this.name = name;
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ForeignCollection<User> getDevelopers() {
        return developers;
    }

    public ForeignCollection<Push> getPushes() {
        return pushes;
    }

    public double getScore() {
        double score=0;
        if(pushes != null && !pushes.isEmpty()) {
            for (Push push : pushes)
                score += push.getScore();
        }
        return score;
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
    } // TODO: 09-5-2016

}
