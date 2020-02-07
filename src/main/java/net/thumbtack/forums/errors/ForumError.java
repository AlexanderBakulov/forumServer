package net.thumbtack.forums.errors;

public enum ForumError {

    NOT_COMPLETE("We are sorry, but this functionality not complete yet."),
	USER_NOT_FOUND("User not found."),
    USER_IS_SUPER("Can't do this to superuser"),
    USER_IS_RESTRICTED("User banned."),
    PERMANENT_BAN("User banned permanently and can only read."),
    RUNTIME_EXCEPTION("Runtime Exception occurred."),
    USER_HAVE_NO_PERMISSIONS("User have no permissions."),
    INCORRECT_PASSWORD("Incorrect password."),
    INCORRECT_FORUM_ID("Forum with this id isn't exist."),
    READ_ONLY_FORUM("This forum is read only."),
    INCORRECT_POST_ID("Post with this id isn't exist."),
    POST_HAS_COMMENTS("You can't delete post with comments."),
    POST_ALREADY_PUBLISHED("Post with this id is already published."),
    POST_IS_NOT_PUBLISHED("Post is not published."),
    ANCESTOR_IS_UNPUBLISHED("Can't comment unpublished post."),
    POST_IS_NOT_MESSAGE("This post not message, but comment."),
    POST_IS_MESSAGE("This post is already message."),
    SAME_PRIORITY("Post already has this priority."),
	EMPTY_RATE("Rate value is empty.");


    private String errorString;

    ForumError(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return this.errorString;
    }	
	
	
	
}
