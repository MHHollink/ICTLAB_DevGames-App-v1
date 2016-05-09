package baecon.devgames.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import baecon.devgames.database.DBHelper;

/**
 * Created by Marcel on 27-4-2016.
 */
@DatabaseTable(tableName = DBHelper.Tables.ISSUES)
public class Issue extends AbsSynchronizable implements Serializable{

    public static class Column{
        public static final String ISSUE_ID = "identifier";
        public static final String SEVERITY = "git_username";
        public static final String COMPONENT = "projects";
        public static final String START_LINE = "pushes";
        public static final String END_LINE = "gcm_registration_key";
        public static final String STATUS = "fname";
        public static final String RESOLUTION = "lname";
        public static final String MESSAGE = "tween";
        public static final String DEBT = "age";
        public static final String CREATION_DATE = "creation_date";
        public static final String UPDATE_DATE = "update_date";
        public static final String CLOSE_DATE = "close_date";
    }

    @DatabaseField(columnName = Column.ISSUE_ID)
    private long issueId;

    @DatabaseField(columnName = Column.SEVERITY)
    private String severity;

    @DatabaseField(columnName = Column.COMPONENT)
    private String component;

    @DatabaseField(columnName = Column.START_LINE)
    private int startLine;

    @DatabaseField(columnName = Column.END_LINE)
    private int endLine;

    @DatabaseField(columnName = Column.STATUS)
    private String status;

    @DatabaseField(columnName = Column.RESOLUTION)
    private String resolution;

    @DatabaseField(columnName = Column.MESSAGE)
    private String message;

    @DatabaseField(columnName = Column.DEBT)
    private int debt;

    @DatabaseField(columnName = Column.CREATION_DATE)
    private long creationDate;

    @DatabaseField(columnName = Column.UPDATE_DATE)
    private long updateDate;

    @DatabaseField(columnName = Column.CLOSE_DATE)
    private long closeDate;

    public Issue(Long id, long closeDate, String component, long creationDate, int debt, int endLine, long issueId, String message, String resolution, String severity, int startLine, String status, long updateDate) {
        super(id);
        this.closeDate = closeDate;
        this.component = component;
        this.creationDate = creationDate;
        this.debt = debt;
        this.endLine = endLine;
        this.issueId = issueId;
        this.message = message;
        this.resolution = resolution;
        this.severity = severity;
        this.startLine = startLine;
        this.status = status;
        this.updateDate = updateDate;
    }

    public Issue() {
    }

    public long getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(long closeDate) {
        this.closeDate = closeDate;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public long getIssueId() {
        return issueId;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Issue)) return false;
        if (!super.equals(o)) return false;

        Issue issue = (Issue) o;

        return getId().longValue() == issue.getId().longValue();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (getId() ^ (getId() >>> 32));
        return result;
    }

    @Override
    public boolean contentEquals(Object other) {
        return false;
    } // TODO: 09-5-2016
}
