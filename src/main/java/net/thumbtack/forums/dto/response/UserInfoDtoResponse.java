package net.thumbtack.forums.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.thumbtack.forums.model.enums.BanStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoDtoResponse {
	
	private int id;
	private String name;
    private String email;	
	private LocalDateTime timeRegistered;
	private boolean online;
	private boolean deleted;
	@JsonProperty("super")
	private Boolean superUser;
	@JsonProperty("status")
	private BanStatus banStatus;
	private LocalDateTime timeBanExit;
	private int banCount;


	public UserInfoDtoResponse(int id, String name, String email, LocalDateTime timeRegistered,
							   boolean online, boolean deleted, Boolean superUser, BanStatus banStatus,
							   LocalDateTime timeBanExit, int banCount) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.timeRegistered = timeRegistered;
		this.online = online;
		this.deleted = deleted;
		this.superUser = superUser;
		this.banStatus = banStatus;
		this.timeBanExit = timeBanExit;
		this.banCount = banCount;
	}

	public UserInfoDtoResponse(int id, String name, LocalDateTime timeRegistered, boolean online, boolean deleted,
							   BanStatus banStatus, int banCount) {
		this(id, name, null, timeRegistered, online, deleted, null, banStatus, null, banCount);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getTimeRegistered() {
		return timeRegistered;
	}

	public void setTimeRegistered(LocalDateTime timeRegistered) {
		this.timeRegistered = timeRegistered;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Boolean isSuperUser() {
		return superUser;
	}

	public void setSuperUser(Boolean superUser) {
		this.superUser = superUser;
	}

	public BanStatus getBanStatus() {
		return banStatus;
	}

	public void setBanStatus(BanStatus banStatus) {
		this.banStatus = banStatus;
	}

	public LocalDateTime getTimeBanExit() {
		return timeBanExit;
	}

	public void setTimeBanExit(LocalDateTime timeBanExit) {
		this.timeBanExit = timeBanExit;
	}

	public int getBanCount() {
		return banCount;
	}

	public void setBanCount(int banCount) {
		this.banCount = banCount;
	}
}