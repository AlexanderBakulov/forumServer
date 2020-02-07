package net.thumbtack.forums.view;



import java.time.LocalDateTime;
import java.util.List;

public class MessageInfo {
	
    private int id;
	private String creator;
	private String subject;
	private List<String> body;
	private String priority;
	private List<String> tags;
	private LocalDateTime created;
	private float rating;
	private int rated;
	private List<CommentInfo> comments;


	public MessageInfo() {
	}

	public MessageInfo(int id, String creator, String subject, List<String> body, String priority, List<String> tags,
					   LocalDateTime created, float rating, int rated, List<CommentInfo> comments) {
		this.id = id;
		this.creator = creator;
		this.subject = subject;
		this.body = body;
		this.priority = priority;
		this.tags = tags;
		this.created = created;
		this.rating = rating;
		this.rated = rated;
		this.comments = comments;
	}

	public int getId() {
		return id;
	}

	public String getCreator() {
		return creator;
	}

	public String getSubject() {
		return subject;
	}

	public List<String> getBody() {
		return body;
	}

	public String getPriority() {
		return priority;
	}

	public List<String> getTags() {
		return tags;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public float getRating() {
		return rating;
	}

	public int getRated() {
		return rated;
	}

	public List<CommentInfo> getComments() {
		return comments;
	}
}