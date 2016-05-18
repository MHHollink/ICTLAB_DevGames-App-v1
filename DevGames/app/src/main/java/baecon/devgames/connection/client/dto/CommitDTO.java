package baecon.devgames.connection.client.dto;

import baecon.devgames.model.Commit;
import baecon.devgames.model.Push;

public class CommitDTO implements ModelDTO<Commit> {

    private long id;
    private String branch;
    private Push pushedIn;
    private int filesChanged;
    private String hash;

    private long time;
    private String title;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Commit toModel() {
        Commit commit = new Commit();

        commit.setId(id);
        commit.setBranch(branch);
        commit.setPushedIn(pushedIn);
        commit.setFilesChanges(filesChanged);
        commit.setHash(hash);
        commit.setTimestamp(time);
        commit.setTitle(title);

        return commit;
    }

    public CommitDTO() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getFilesChanged() {
        return filesChanged;
    }

    public void setFilesChanged(int filesChanged) {
        this.filesChanged = filesChanged;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Push getPushedIn() {
        return pushedIn;
    }

    public void setPushedIn(Push pushedIn) {
        this.pushedIn = pushedIn;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CommitDTO{" +
                "branch='" + branch + '\'' +
                ", id=" + id +
                ", pushedIn=" + pushedIn +
                ", filesChanged=" + filesChanged +
                ", hash='" + hash + '\'' +
                ", time=" + time +
                ", title='" + title + '\'' +
                '}';
    }
}
