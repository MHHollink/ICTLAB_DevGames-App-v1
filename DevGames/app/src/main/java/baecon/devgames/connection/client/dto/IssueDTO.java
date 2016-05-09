package baecon.devgames.connection.client.dto;

import baecon.devgames.model.Issue;

/**
 * Created by Marcel on 09-5-2016.
 */
public class IssueDTO implements ModelDTO<Issue> {

    private long id;
    private long issueId;

    private String severity;

    private String component;
    private int startLine;
    private int endLine;

    private String status;
    private String resolution;
    private String message;

    private int debt;

    private long creationDate;
    private long updateDate;
    private long closeDate;

    public IssueDTO() {
    }

    public IssueDTO(Issue issue) {
        id = issue.getId();
        issueId = issue.getIssueId();
        severity = issue.getSeverity();
        component = issue.getComponent();
        startLine = issue.getStartLine();
        endLine = issue.getEndLine();
        status = issue.getStatus();
        resolution = issue.getResolution();
        message = issue.getMessage();
        debt = issue.getDebt();
        creationDate = issue.getCreationDate();
        updateDate = issue.getUpdateDate();
        closeDate = issue.getCloseDate();
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

    public void setId(long id) {
        this.id = id;
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
    public Long getId() {
        return id;
    }

    @Override
    public Issue toModel() {
        Issue issue = new Issue();

        issue.setId(id);

        issue.setIssueId(issueId);

        issue.setSeverity(severity);

        issue.setComponent(component);
        issue.setStartLine(startLine);
        issue.setEndLine(endLine);

        issue.setMessage(message);
        issue.setStatus(status);
        issue.setResolution(resolution);
        issue.setDebt(debt);

        issue.setCreationDate(creationDate);
        issue.setUpdateDate(updateDate);
        issue.setCloseDate(closeDate);

        return issue;
    }

    @Override
    public String toString() {
        return "IssueDTO{" +
                "closeDate=" + closeDate +
                ", id=" + id +
                ", issueId=" + issueId +
                ", severity='" + severity + '\'' +
                ", component='" + component + '\'' +
                ", startLine=" + startLine +
                ", endLine=" + endLine +
                ", status='" + status + '\'' +
                ", resolution='" + resolution + '\'' +
                ", message='" + message + '\'' +
                ", debt=" + debt +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
