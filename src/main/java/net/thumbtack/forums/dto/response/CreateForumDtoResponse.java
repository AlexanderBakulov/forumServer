package net.thumbtack.forums.dto.response;


import net.thumbtack.forums.model.enums.ModerateStatus;

public class CreateForumDtoResponse {
	
	private int id;
	private String name;
    private ModerateStatus type;


	public CreateForumDtoResponse(int id, String name, ModerateStatus type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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