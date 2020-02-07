package net.thumbtack.forums.dto.response;


import net.thumbtack.forums.model.enums.ModerateStatus;


public class ForumInfoDtoResponse {
	
	private int id;
	private String name;
    private ModerateStatus type;
	private String creator;
	private boolean readonly;
	private int messageCount;
	private int commentCount;

	public ForumInfoDtoResponse() {
	}

	public ForumInfoDtoResponse(int id, String name, ModerateStatus type, String creator, boolean readonly, int messageCount, int commentCount) {
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

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ModerateStatus getType() {
		return type;
	}

	public void setType(ModerateStatus type) {
		this.type = type;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

}