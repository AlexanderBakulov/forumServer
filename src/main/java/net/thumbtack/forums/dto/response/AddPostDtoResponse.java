package net.thumbtack.forums.dto.response;


import net.thumbtack.forums.model.enums.PostState;

public class AddPostDtoResponse {
	
	private int id;
	private PostState state;

	public AddPostDtoResponse(int id, PostState state) {
		this.id = id;
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PostState getState() {
		return state;
	}

	public void setState(PostState state) {
		this.state = state;
	}
}