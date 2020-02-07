package net.thumbtack.forums.dto.request;


import javax.validation.constraints.NotBlank;

import static net.thumbtack.forums.Config.NOT_BLANK;

public class AddCommentDtoRequest {

	@NotBlank(message = NOT_BLANK)
    private String body;

	public AddCommentDtoRequest() {
	}
	
	public AddCommentDtoRequest(String body) {
		this.body = body;
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}