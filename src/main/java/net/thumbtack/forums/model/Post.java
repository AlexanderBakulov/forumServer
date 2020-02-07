package net.thumbtack.forums.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Post {

    private int id;
    private User user;
    private LocalDateTime publishDate;
    private List<History> history;
    private List<Post> comments;
    private List<Rate> rates;
    private Header header;
    private Post ancestor;

    public Post() {
    }

    public Post(int id, User user, LocalDateTime publishDate, List<History> history, List<Post> comments,
                List<Rate> rates, Header header, Post ancestor) {
        this.id = id;
        this.user = user;
        this.publishDate = publishDate;
        this.history = history;
        this.comments = comments;
        this.rates = rates;
        this.header = header;
        this.ancestor = ancestor;
    }

    public Post(User user, Header header,  List<History> history, Post ancestor) {
        this(0, user, LocalDateTime.now(), history, null,
                null, header, ancestor);
    }

    public Post(User user, Header header,  List<History> history) {
        this(0, user, LocalDateTime.now(), history, null,
                null, header, null);
    }

    public Post(User user, Header header, Post ancestor) {
        this(0, user, LocalDateTime.now(), new ArrayList<>(), null,
                null, header, ancestor);
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

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }

    public List<Post> getComments() {
        return comments;
    }

    public void setComments(List<Post> comments) {
        this.comments = comments;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Post getAncestor() {
        return ancestor;
    }

    public void setAncestor(Post ancestor) {
        this.ancestor = ancestor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return id == post.id &&
                Objects.equals(user, post.user) &&
                Objects.equals(publishDate, post.publishDate) &&
                Objects.equals(history, post.history) &&
                Objects.equals(comments, post.comments) &&
                Objects.equals(rates, post.rates) &&
                Objects.equals(header, post.header) &&
                Objects.equals(ancestor, post.ancestor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, publishDate, history, comments, rates, header, ancestor);
    }
}
