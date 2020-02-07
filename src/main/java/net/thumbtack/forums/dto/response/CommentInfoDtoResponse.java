package net.thumbtack.forums.dto.response;


import java.time.LocalDateTime;
import java.util.List;

public class CommentInfoDtoResponse {

    private int id;
    private String creator;
    private List<String> body;
    private LocalDateTime created;
    private float rating;
    private int rated;
    private List<CommentInfoDtoResponse> comments;

    public CommentInfoDtoResponse() {
    }

    public CommentInfoDtoResponse(int id, String creator, List<String> body, LocalDateTime created, float rating,
                                  int rated, List<CommentInfoDtoResponse> comments) {
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

    public void setId(int id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRated() {
        return rated;
    }

    public void setRated(int rated) {
        this.rated = rated;
    }

    public List<CommentInfoDtoResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentInfoDtoResponse> comments) {
        this.comments = comments;
    }
}
