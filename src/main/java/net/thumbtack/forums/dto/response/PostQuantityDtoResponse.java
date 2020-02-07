package net.thumbtack.forums.dto.response;

public class PostQuantityDtoResponse {

    private int messageCount;
    private int commentCount;

    public PostQuantityDtoResponse() {
    }

    public PostQuantityDtoResponse(int messageCount, int commentCount) {
        this.messageCount = messageCount;
        this.commentCount = commentCount;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
