package net.thumbtack.forums.model;

import net.thumbtack.forums.model.enums.PostState;

import java.util.Objects;

public class History {

    private int id;
    private String postBody;
    private PostState postState;

    public History() { ;
    }

    public History(int id, String postBody, PostState postState) {
        this.id = id;
        this.postBody = postBody;
        this.postState = postState;
    }

    public History(String postBody, PostState postState) {
        this(0, postBody, postState);
    }

    public History(String postBody) {
        this(0, postBody, PostState.UNPUBLISHED);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public PostState getPostState() {
        return postState;
    }

    public void setPostState(PostState postState) {
        this.postState = postState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof History)) return false;
        History history = (History) o;
        return id == history.id &&
                Objects.equals(postBody, history.postBody) &&
                postState == history.postState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postBody, postState);
    }
}
