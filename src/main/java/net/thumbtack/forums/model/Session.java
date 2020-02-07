package net.thumbtack.forums.model;

import java.util.Objects;

public class Session {

    private int id;
    private User user;
    private String token;

    public Session() {
    }

    public Session(int id, User user, String token) {
        this.id = id;
        this.user = user;
        this.token = token;
    }

    public Session(User user, String token) {
        this(0, user, token);
    }

    public Session(String token) {
        this(0,null, token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (!(o instanceof Session)) return false;
        Session session = (Session) o;
        return id == session.id &&
                Objects.equals(user, session.user) &&
                Objects.equals(token, session.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, token);
    }
}
