package baecon.devgames.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Comparator;

import baecon.devgames.R;
import baecon.devgames.database.DBHelper;
import baecon.devgames.util.HeaderFactory;
import baecon.devgames.util.ProjectNameComparator;
import baecon.devgames.util.ProjectScoreComparator;
import baecon.devgames.util.SortOption;

@DatabaseTable(tableName = DBHelper.Tables.PROJECTS)
public class Project extends AbsSynchronizable implements Serializable {

    public enum Sort implements SortOption<Project> {
        NAME(R.string.sort_name, new ProjectNameComparator()),
        SCORE(R.string.sort_score, new ProjectScoreComparator());

        public final int i18nFilterName;
        public final Comparator<Project> comparator;


        Sort(int i18nFilterName, Comparator<Project> comparator) {
            this.i18nFilterName = i18nFilterName;
            this.comparator = comparator;
        }

        /**
         * Returns the {@link Sort} that this name represents. Returns null if not found.
         *
         * @param name
         *         The {@link #name()} of one of the elements in this enum
         *
         * @return The {@link Sort} that this name represents, or null if non found
         */
        public static Sort tryValueOf(String name) {
            try {
                return valueOf(name);
            }
            catch (IllegalArgumentException e) {
                return null;
            }
        }

        @Override
        public int getI18nTitleResId() {
            return i18nFilterName;
        }

        @Override
        public Comparator<Project> getComparator() {
            return comparator;
        }

        @Override
        public HeaderFactory<Project> getHeaderFactory() {
            return null;
        }
    }

    public static class Column {
        public static final String OWNER = "owner";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
    }

    @DatabaseField(columnName = Column.OWNER, dataType = DataType.SERIALIZABLE)
    private User owner;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<ProjectUser> developers;

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

    public ForeignCollection<ProjectUser> getDevelopers() {
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
