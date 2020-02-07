package net.thumbtack.forums.model;

import net.thumbtack.forums.model.enums.ModerateStatus;

import java.util.Objects;


public class Forum {

    private int id;
    private String name;
    private ModerateStatus moderateStatus;
    private Boolean readOnly;
    private User user;

    public Forum() {
    }

    public Forum(int id, String name, ModerateStatus moderateStatus, Boolean readOnly, User user) {
        this.id = id;
        this.name = name;
        this.moderateStatus = moderateStatus;
        this.readOnly = readOnly;
        this.user = user;
    }

        public Forum(String name, ModerateStatus moderateStatus, User user) {
        this(0, name, moderateStatus, false, user);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModerateStatus getModerateStatus() {
        return moderateStatus;
    }

    public void setModerateStatus(ModerateStatus moderateStatus) {
        this.moderateStatus = moderateStatus;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Forum)) return false;
        Forum forum = (Forum) o;
        return id == forum.id &&
                Objects.equals(name, forum.name) &&
                moderateStatus == forum.moderateStatus &&
                Objects.equals(readOnly, forum.readOnly) &&
                Objects.equals(user, forum.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, moderateStatus, readOnly, user);
    }
}
