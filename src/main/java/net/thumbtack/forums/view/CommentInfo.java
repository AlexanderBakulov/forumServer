package net.thumbtack.forums.view;


import java.time.LocalDateTime;
import java.util.List;

public class CommentInfo {

    private int id;
    private String creator;
    private List<String> body;
    private LocalDateTime created;
    private float rating;
    private int rated;
    private List<CommentInfo> comments;

    public CommentInfo() {
    }

    public CommentInfo(int id, String creator, List<String> body, LocalDateTime created, float rating, int rated,
                       List<CommentInfo> comments) {
        this.id = id;
        this.creator = creator;
        this.body = body;
        this.created = created;
        this.rating = rating;
        this.rated = rated;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public String getCreator() {
        return creator;
    }

    public List<String> getBody() {
        return body;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public float getRating() {
        return rating;
    }

    public int getRated() {
        return rated;
    }

    public List<CommentInfo> getComments() {
        return comments;
    }
}
