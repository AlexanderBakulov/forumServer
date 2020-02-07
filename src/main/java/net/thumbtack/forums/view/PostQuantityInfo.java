package net.thumbtack.forums.view;

public class PostQuantityInfo {

    private int messageCount;
    private int commentCount;

    public PostQuantityInfo() {
    }

    public PostQuantityInfo(int messageCount, int commentCount) {
        this.messageCount = messageCount;
        this.commentCount = commentCount;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public int getCommentCount() {
        return commentCount;
    }
}
