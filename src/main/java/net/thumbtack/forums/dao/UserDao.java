package net.thumbtack.forums.dao;


import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.model.User;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.enums.Privilege;
import net.thumbtack.forums.view.UserInfo;

import java.util.List;


public interface UserDao {

    User createUser(User user, Session session) throws ForumException;

    Session loginUser(Session session) throws ForumException;

    void logoutUser(Session session) throws ForumException;

    void quitUser(Session session) throws ForumException;

    User getUser(Session session) throws ForumException;

    User getUser(int id) throws ForumException;

    User getUser(String name) throws ForumException;

    List<UserInfo> getUsers() throws ForumException;

    String changePassword(User user, String newPassword) throws ForumException;

    void makeSuperuser(User user) throws ForumException;

    void restrictUser(User user) throws ForumException;

    Privilege getUserPrivilege(User user) throws ForumException;

    Session getSession(User user) throws ForumException;

    void unbanUsers() throws ForumException;
}
