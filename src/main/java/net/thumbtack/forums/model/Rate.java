package net.thumbtack.forums.model;

import java.util.Objects;

public class Rate {

    private int id;
	private int rate;
	private User user;
	private Post post;

    public Rate() {
    }

    public Rate(int id, int rate, User user, Post post) {
        this.id = id;
        this.rate = rate;
        this.user = user;
        this.post = post;
    }

    public Rate(int rate, User user, Post post) {
        this(0, rate, user, post);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rate)) return false;
        Rate rate1 = (Rate) o;
        return id == rate1.id &&
                rate == rate1.rate &&
                Objects.equals(user, rate1.user) &&
                Objects.equals(post, rate1.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rate, user, post);
    }
}
