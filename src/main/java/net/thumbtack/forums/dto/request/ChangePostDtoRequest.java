package net.thumbtack.forums.dto.request;


import javax.validation.constraints.NotBlank;

import static net.thumbtack.forums.Config.NOT_BLANK;

public class ChangePostDtoRequest {

	@NotBlank(message = NOT_BLANK)
    private String body;

	public ChangePostDtoRequest() {
	}
	
	public ChangePostDtoRequest(String body) {
		this.body = body;
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}