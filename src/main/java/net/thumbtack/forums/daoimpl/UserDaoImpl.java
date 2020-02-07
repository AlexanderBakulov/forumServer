package net.thumbtack.forums.daoimpl;

import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.mappers.mybatis.UserMapper;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.model.User;
import net.thumbtack.forums.errors.ForumError;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.enums.Privilege;
import net.thumbtack.forums.view.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.List;


@Component
public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    private final UserMapper userMapper;


    public UserDaoImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public User createUser(User user, Session session) throws ForumException {
        LOGGER.debug("Create user {}", user);
        try {
            userMapper.createUser(user);
            session.setUser(user);
            userMapper.createSession(session);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't create user {}, exception ", user, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
        return user;
    }


    @Override
    public Session loginUser(Session session) throws ForumException {
        LOGGER.debug("Login user {}", session.getUser());
        try {
            userMapper.deleteSession(session);
            userMapper.createSession(session);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't login user {}, exception ", session.getUser(), ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
        return session;
    }


    @Override
    public void logoutUser(Session session) throws ForumException {
        LOGGER.debug("Logout user {}", session.getUser());
        try {
        	userMapper.deleteSession(session);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't logout user {}, exception ", session.getUser(), ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public void quitUser(Session session) throws ForumException {
        LOGGER.debug("Delete user {}", session.getUser());
        try {
            userMapper.deleteSession(session);
            userMapper.quitUser(session);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete user {}, exception ", session.getUser(), ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public String changePassword(User user, String newPassword) throws ForumException {
        LOGGER.debug("Change password to user {}", user);
        try {
            userMapper.updatePassword(user, newPassword);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't change password to user {}, exception ", user, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
        return newPassword;
    }


    @Override
    public void makeSuperuser(User user) throws ForumException {
        LOGGER.debug("Make user {} Super ", user);
        try {
            userMapper.makeSuper(user);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't make user {} super, exception ", user, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public void restrictUser(User user) throws ForumException {
        LOGGER.debug("Ban user {} ", user);
        try {
            userMapper.restrictUser(user);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't ban user {}, exception ", user, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public List<UserInfo> getUsers() throws ForumException {
        LOGGER.debug("Get users list");
        try {
            return userMapper.getFullUsersInfo();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get users list, exception ", ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public User getUser(Session session) throws ForumException {
        LOGGER.debug("Get user with token {}", session.getToken());
        try {
            return userMapper.getUserBySession(session);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get user with token {}, exception ", session.getToken(), ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public User getUser(int id) throws ForumException {
        LOGGER.debug("Get user with id {}", id);
        try {
            return userMapper.getUserById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get user with id {}, exception ", id, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public User getUser(String name) throws ForumException {
        LOGGER.debug("Get user by name {}", name);
        try {
            return userMapper.getUserByName(name);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get user with name {}, exception ", name, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public Privilege getUserPrivilege(User user) throws ForumException {
        LOGGER.debug("Get privilege for user {}", user);
        try {
            return userMapper.getUserPrivilege(user);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get privilege for user {}, exception ", user, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public Session getSession(User user) throws ForumException {
        LOGGER.debug("Get session for user {}", user);
        try {
            return userMapper.getSession(user);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get privilege for user {}, exception ", user, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public void unbanUsers() throws ForumException {
        LOGGER.debug("Try to unban users");
        try {
            userMapper.unbanUsers();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't unban users, exception  ", ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }

}
