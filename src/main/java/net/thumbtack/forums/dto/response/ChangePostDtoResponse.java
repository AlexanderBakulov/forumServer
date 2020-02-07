package net.thumbtack.forums.dto.response;


import net.thumbtack.forums.model.enums.PostState;

public class ChangePostDtoResponse {
	
    private PostState postState;

	public ChangePostDtoResponse(PostState postState) {
		this.postState = postState;
	}

	public PostState getPostState() {
		return postState;
	}

	public void setPostState(PostState postState) {
		this.postState = postState;
	}
}