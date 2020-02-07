package net.thumbtack.forums.dto.request;

import net.thumbtack.forums.model.enums.PostPriority;

import javax.validation.constraints.NotBlank;
import java.util.Set;

import static net.thumbtack.forums.Config.NOT_BLANK;

public class CommentToMessageDtoRequest {

	@NotBlank(message = NOT_BLANK)
    private String subject;

	private PostPriority priority;

	private Set<String> tags;

	public CommentToMessageDtoRequest() {
	}

	public CommentToMessageDtoRequest(String subject, PostPriority priority, Set<String> tags) {
		this.subject = subject;
		this.priority = priority;
		this.tags = tags;
	}

	public CommentToMessageDtoRequest(String subject, PostPriority priority) {
		this(subject, priority, null);
	}

	public CommentToMessageDtoRequest(String subject, Set<String> tags) {
		this(subject, PostPriority.NORMAL, tags);
	}

	public CommentToMessageDtoRequest(String subject) {
		this(subject, PostPriority.NORMAL, null);
	}



	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public PostPriority getPriority() {
		return priority;
	}

	public void setPriority(PostPriority priority) {
		this.priority = priority;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
}