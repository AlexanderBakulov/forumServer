package net.thumbtack.forums.view;

import net.thumbtack.forums.model.enums.BanStatus;
import net.thumbtack.forums.model.enums.Privilege;

import java.time.LocalDateTime;

public class UserInfo {

    private int id;
    private String name;
    private String email;
    private Privilege privilege;
    private LocalDateTime timeRegistered;
    private BanStatus banStatus;
    private LocalDateTime timeBanExit;
    private int banCount;
    private boolean online;
    private boolean deleted;

    public UserInfo() {
    }



    public UserInfo(int id, String name, String email, Privilege privilege, LocalDateTime timeRegistered,
                    BanStatus banStatus, LocalDateTime timeBanExit, int banCount, boolean online, boolean deleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.privilege = privilege;
        this.timeRegistered = timeRegistered;
        this.banStatus = banStatus;
        this.timeBanExit = timeBanExit;
        this.banCount = banCount;
        this.online = online;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public LocalDateTime getTimeRegistered() {
        return timeRegistered;
    }

    public BanStatus getBanStatus() {
        return banStatus;
    }

    public LocalDateTime getTimeBanExit() {
        return timeBanExit;
    }

    public int getBanCount() {
        return banCount;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
