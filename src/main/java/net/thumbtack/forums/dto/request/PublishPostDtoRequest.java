package net.thumbtack.forums.dto.request;

import net.thumbtack.forums.dto.enums.Decision;

import javax.validation.constraints.NotNull;

import static net.thumbtack.forums.Config.NOT_NULL;

public class PublishPostDtoRequest {

	@NotNull(message = NOT_NULL)
    private Decision decision;


	public PublishPostDtoRequest() {
	}

	public PublishPostDtoRequest(Decision decision) {
		this.decision = decision;
	}
	
	
	public Decision getDecision() {
		return decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}
}