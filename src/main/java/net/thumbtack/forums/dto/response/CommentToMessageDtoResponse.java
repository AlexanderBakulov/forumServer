package net.thumbtack.forums.dto.response;

public class CommentToMessageDtoResponse {
	
    private int id;
	private String state;
	
	public CommentToMessageDtoResponse(int id, String state) {
		this.id = id;
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}