package net.thumbtack.forums.dto.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static net.thumbtack.forums.Config.NOT_BLANK;


public class LoginUserDtoRequest {


	@NotBlank(message = NOT_BLANK)
    private String name;

	@NotBlank(message = NOT_BLANK)
    private String password;

	public LoginUserDtoRequest() {
	}

	public LoginUserDtoRequest(@NotNull @NotBlank String name,
							   @NotNull @NotBlank String password) {
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}