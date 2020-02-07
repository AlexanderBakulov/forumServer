package net.thumbtack.forums.dto.request;

import net.thumbtack.forums.utils.annotations.MaxLength;
import net.thumbtack.forums.utils.annotations.MinLength;

import javax.validation.constraints.*;

import static net.thumbtack.forums.Config.*;


public class CreateUserDtoRequest {

	@NotBlank(message = NOT_BLANK)
	@MaxLength
	@Pattern(regexp =  "^[A-Za-zА-Яа-я0-9]+$", message = INVALID_NAME)
    private String name;

	@Pattern(regexp = "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$", message = INVALID_EMAIL_FORMAT)
    private String email;

	@NotBlank(message = NOT_BLANK)
	@MaxLength
	@MinLength
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$", message = INVALID_PASSWORD)
	private String password;
	
	public CreateUserDtoRequest() {
	}

	public CreateUserDtoRequest(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}