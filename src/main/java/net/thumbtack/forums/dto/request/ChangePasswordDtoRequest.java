package net.thumbtack.forums.dto.request;


import net.thumbtack.forums.utils.annotations.MaxLength;
import net.thumbtack.forums.utils.annotations.MinLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static net.thumbtack.forums.Config.INVALID_PASSWORD;
import static net.thumbtack.forums.Config.NOT_BLANK;

public class ChangePasswordDtoRequest {

	@NotBlank(message = NOT_BLANK)
    private String name;

	@NotBlank(message = NOT_BLANK)
    private String oldPassword;

	@NotBlank(message = NOT_BLANK)
	@MaxLength
	@MinLength
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$", message = INVALID_PASSWORD)
    private String newPassword;

	public ChangePasswordDtoRequest() {
	}
	
	public ChangePasswordDtoRequest(String name, String oldPassword, String newPassword) {
		this.name = name;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
	
	public String getName() {
		return name;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}