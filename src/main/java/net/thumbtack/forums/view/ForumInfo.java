package net.thumbtack.forums.view;

import net.thumbtack.forums.model.enums.ModerateStatus;

public class ForumInfo {

    private int id;
    private String name;
    private ModerateStatus type;
    private String creator;
    private boolean readonly;
    private int messageCount;
    private int commentCount;

    public ForumInfo() {
    }

    public ForumInfo(int id, String name, ModerateStatus type, String creator, boolean readonly,
                     int messageCount, int commentCount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.creator = creator;
        this.readonly = readonly;
        this.messageCount = messageCount;
        this.commentCount = commentCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ModerateStatus getType() {
        return type;
    }

    public String getCreator() {
        return creator;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public int getCommentCount() {
        return commentCount;
    }


}
