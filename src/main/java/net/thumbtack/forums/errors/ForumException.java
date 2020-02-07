package net.thumbtack.forums.errors;


import java.util.Objects;

public class ForumException extends Exception {


    private final ForumError forumError;
    private final String field;
    private final String message;

    public ForumException(ForumError forumError, String field, String message) {
        this.forumError = forumError;
        this.field = field;
        this.message = message;
    }

    public ForumException(ForumError forumError, String field) {
        this(forumError, field, forumError.getErrorString());
    }

    public ForumException(ForumError forumError) {
        this(forumError, null, forumError.getErrorString());
    }


    public ForumError getErrorCode() {
        return forumError;
    }

    public String getForumErrorString() {
        return forumError.getErrorString();
    }

    public String getField() {
        return field;
    }


    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForumException that = (ForumException) o;
        return forumError == that.forumError &&
                Objects.equals(field, that.field) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forumError, field, message);
    }
}