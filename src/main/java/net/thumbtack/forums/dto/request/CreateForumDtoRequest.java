package net.thumbtack.forums.dto.request;


import net.thumbtack.forums.model.enums.ModerateStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static net.thumbtack.forums.Config.*;

public class CreateForumDtoRequest {

	@NotBlank(message = NOT_BLANK)
	@Pattern(regexp =  "^[A-Za-zА-Яа-я]+$", message = INVALID_NAME)
	private String name;

	@NotNull(message = NOT_NULL)
    private ModerateStatus type;

	public CreateForumDtoRequest(String name, ModerateStatus type) {
		this.name = name;
		this.type = type;
	}

	public CreateForumDtoRequest() {
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
}