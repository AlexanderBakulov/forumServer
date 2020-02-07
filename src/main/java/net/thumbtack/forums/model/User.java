package net.thumbtack.forums.model;


import net.thumbtack.forums.model.enums.BanStatus;
import net.thumbtack.forums.model.enums.Privilege;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private int id;
    private String name;
    private String password;
    private String email;
    private Privilege privilege;
    private LocalDateTime timeRegistered;
    private BanStatus banStatus;
    private LocalDateTime timeBanExit;
    private int banCount;

    public User() {
    }


    public User(int id, String name, String password, String email, Privilege privilege,
                LocalDateTime timeRegistered, BanStatus banStatus, LocalDateTime timeBanExit, int banCount) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.privilege = privilege;
        this.timeRegistered = timeRegistered;
        this.banStatus = banStatus;
        this.timeBanExit = timeBanExit;
        this.banCount = banCount;
    }

    public User(String name, String password, String email) {
        this(0, name, password, email, Privilege.REGULAR, LocalDateTime.now(),
                BanStatus.FULL, null, 0);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id &&
                banCount == user.banCount &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                privilege == user.privilege &&
                Objects.equals(timeRegistered, user.timeRegistered) &&
                banStatus == user.banStatus &&
                Objects.equals(timeBanExit, user.timeBanExit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, email, privilege, timeRegistered, banStatus, timeBanExit, banCount);
    }
}
