package net.thumbtack.forums.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class MessageInfoDtoResponse {

    private int id;
    private String creator;
    private String subject;
    private List<String> body;
    private String priority;
    private List<String> tags;
    private LocalDateTime created;
    private float rating;
    private int rated;
    private List<CommentInfoDtoResponse> comments;

    public MessageInfoDtoResponse() {
    }

    public MessageInfoDtoResponse(int id, String creator, String subject, List<String> body, String priority,
                                  List<String> tags, LocalDateTime created, float rating, int rated,
                                  List<CommentInfoDtoResponse> comments) {
        this.id = id;
        this.creator = creator;
        this.subject = subject;
        this.body = body;
        this.priority = priority;
        this.tags = tags;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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
