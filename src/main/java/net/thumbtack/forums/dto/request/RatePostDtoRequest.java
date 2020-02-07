package net.thumbtack.forums.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RatePostDtoRequest {


	@Min(value = 1)
	@Max(value = 5)
    private Integer value;

	public RatePostDtoRequest() {
	}

	public RatePostDtoRequest(Integer value) {
		this.value = value;
	}
	
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}