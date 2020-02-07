package net.thumbtack.forums.dto.request;


import net.thumbtack.forums.model.Tag;
import net.thumbtack.forums.model.enums.PostPriority;
import net.thumbtack.forums.utils.annotations.SetNotEmpty;

import javax.validation.constraints.NotBlank;
import java.util.Set;

import static net.thumbtack.forums.Config.NOT_BLANK;

public class CreateMessageDtoRequest {

	@NotBlank(message = NOT_BLANK)
	private String subject;

	@NotBlank(message = NOT_BLANK)
    private String body;

	private PostPriority priority;
	@SetNotEmpty
	private Set<Tag> tags;

	public CreateMessageDtoRequest() {
	}
	
	public CreateMessageDtoRequest(String subject, String body, PostPriority priority, Set<Tag> tags) {
		this.subject = subject;
		this.body = body;
		this.priority = priority;
		this.tags = tags;
	}
	
	public CreateMessageDtoRequest(String subject, String body) {
		this(subject, body, PostPriority.NORMAL, null);
	}

	public CreateMessageDtoRequest(String subject, String body, PostPriority priority) {
		this(subject, body, priority, null);
	}

	public CreateMessageDtoRequest(String subject, String body, Set<Tag> tags) {
		this(subject, body, PostPriority.NORMAL, tags);
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getBody() {
		return body;
	}

	public PostPriority getPriority() {
		return priority;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setPriority(PostPriority priority) {
		this.priority = priority;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
}