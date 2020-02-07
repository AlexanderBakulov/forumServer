package net.thumbtack.forums.daotests;

import net.thumbtack.forums.dao.CommonDao;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.model.User;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.enums.BanStatus;
import net.thumbtack.forums.model.enums.Privilege;
import net.thumbtack.forums.view.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static net.thumbtack.forums.errors.ForumError.RUNTIME_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDaoTest {

    @Autowired
    CommonDao commonDao;

    @Autowired
    UserDao userDao;

    @BeforeEach()
    public void clearDatabase() throws ForumException {
        commonDao.clear();
    }

    @Test
    public void testCreateUser() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        User userFromDb = userDao.getUser(user.getName());
        assertEquals(user.getName(), userFromDb.getName());
    }

    @Test
    public void testCreateUser_userAlreadyExist() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        User user2 = new User("A", "bbb", "b@b.c");
        Session session2 = new Session("78910");
        userDao.createUser(user, session);
        try {
            userDao.createUser(user2, session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex.getErrorCode(), RUNTIME_EXCEPTION);
        }
    }

    @Test
    public void testCreateUser_userAlreadyExist2() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        User user2 = new User("a", "bbb", "b@b.c");
        Session session2 = new Session("78910");
        userDao.createUser(user, session);
        try {
            userDao.createUser(user2, session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex.getErrorCode(), RUNTIME_EXCEPTION);
        }
    }

    @Test
    public void testCreateUser_sameEmail() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        User user2 = new User("B", "aaa", "A@b.c");
        Session session2 = new Session("78910");
        userDao.createUser(user, session);
        try {
            userDao.createUser(user2, session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex.getErrorCode(), RUNTIME_EXCEPTION);
        }
    }

    @Test
    public void testGetUserByName() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        User user1 = userDao.getUser(user.getName());
        assertEquals(user.getId(), user1.getId());
    }

    @Test
    public void testGetUserByName_wrongName() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        assertNull(userDao.getUser("C"));
    }

    @Test
    public void testLoginUser() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        session.setUser(user);
        userDao.logoutUser(session);
        userDao.loginUser(session);
        User user1 = userDao.getUser(user.getName());
        assertEquals(user.getId(), user1.getId());
    }

    @Test
    public void testLoginUser2() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        session.setUser(user);
        userDao.logoutUser(session);
        user.setName("a");
        userDao.loginUser(session);
        User user1 = userDao.getUser(user.getName());
        assertEquals(user.getId(), user1.getId());
    }

    @Test
    public void testLoginUser_alreadyLoggedIn() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        Session session2 = new Session(user,"12345333");
        userDao.loginUser(session2);
        Session session3 = new Session(user,"125555333");
        userDao.loginUser(session3);
        Session sfdb = userDao.getSession(user);
        assertEquals(session3.getToken(), sfdb.getToken());
    }

    @Test
    public void testLogoutUser() throws ForumException {
        User user = new User("AbaaB", "bbb", "A@b.c");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        userDao.logoutUser(session);
        assertNull(userDao.getUser(session));
    }

    @Test
    public void testDeleteUser() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        userDao.quitUser(session);
        assertNull(userDao.getUser(user.getName()));
    }

    @Test
    public void testChangePassword() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session(user, "123456");
        userDao.createUser(user, session);
        String newPassword = "ccc";
        userDao.changePassword(user, newPassword);
        User userFromDB = userDao.getUser(session);
        assertEquals(newPassword, userFromDB.getPassword());

    }


    @Test
    public void testGetUserPrivilege() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        Privilege privilege = userDao.getUserPrivilege(user);
        assertEquals(Privilege.REGULAR, privilege);
    }

    @Test
    public void testMakeSuperuser() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        userDao.makeSuperuser(user);
        User user1 = userDao.getUser(session);
        assertEquals(Privilege.SUPER, user1.getPrivilege());
        assertEquals(BanStatus.FULL, user1.getBanStatus());
    }

    @Test
    public void testMakeSuperuser_restrictedUser() throws ForumException {
        User user = new User("A", "aaa", "aaa");
        Session session = new Session("123456");
        userDao.createUser(user, session);
        user.setBanStatus(BanStatus.LIMITED);
        LocalDateTime banExitTime = LocalDateTime.now().plusDays(3);
        user.setTimeBanExit(banExitTime);
        user.setBanCount(1);
        userDao.restrictUser(user);
        userDao.makeSuperuser(user);
        User user1 = userDao.getUser(session);
        assertEquals(Privilege.SUPER, user1.getPrivilege());
        assertEquals(BanStatus.FULL, user1.getBanStatus());
    }

    @Test
    public void testGetUsers() throws ForumException {
        User user1 = new User("A", "aaa", "aaa");
        User user2 = new User("B", "bbb", "bbb");
        User user3 = new User("C", "ccc", "ccc");
        User user4 = new User("D", "ddd", "ddd");
        Session session1 = new Session("1");
        Session session2 = new Session("2");
        Session session3 = new Session("3");
        Session session4 = new Session("4");
        userDao.createUser(user1, session1);
        userDao.createUser(user2, session2);
        userDao.createUser(user3, session3);
        userDao.createUser(user4, session4);
        List<UserInfo> users = userDao.getUsers();
        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void testRestrictUser() throws ForumException {
        User user = new User("A", "aaa", "aaa");
        Session session = new Session("1");
        userDao.createUser(user, session);
        user.setBanStatus(BanStatus.LIMITED);
        LocalDateTime banExitTime = LocalDateTime.now().plusDays(3);
        user.setTimeBanExit(banExitTime);
        user.setBanCount(1);
        userDao.restrictUser(user);
        User user1 = userDao.getUser(session);
        assertEquals(BanStatus.LIMITED, user1.getBanStatus());
        assertEquals(1, user1.getBanCount());
    }


}
