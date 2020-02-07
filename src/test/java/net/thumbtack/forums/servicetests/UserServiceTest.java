package net.thumbtack.forums.servicetests;

import net.thumbtack.forums.dao.CommonDao;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.dto.request.ChangePasswordDtoRequest;
import net.thumbtack.forums.dto.request.CreateUserDtoRequest;
import net.thumbtack.forums.dto.request.LoginUserDtoRequest;
import net.thumbtack.forums.dto.response.CommonUserDtoResponse;
import net.thumbtack.forums.dto.response.UserInfoDtoResponse;
import net.thumbtack.forums.errors.ForumError;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.model.User;
import net.thumbtack.forums.model.enums.BanStatus;
import net.thumbtack.forums.model.enums.Privilege;
import net.thumbtack.forums.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserServiceTest {

    @Autowired
    CommonDao commonDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @BeforeEach()
    public void clearDatabase() throws ForumException {
        commonDao.clear();
    }


    @Test
    public void testCreateUser() throws ForumException {
        CreateUserDtoRequest request = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        CommonUserDtoResponse response = userService.createUser(request, session);
        assertEquals(request.getName(), response.getName());
    }

    @Test
    public void testLogoutLoginUser() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        userService.createUser(request1, session1);
        userService.logoutUser(session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request2 = new LoginUserDtoRequest(request1.getName(), request1.getPassword());
        userService.loginUser(request2, session2);
        assertNotNull(userDao.getUser(session2));
    }

    @Test
    public void testLogoutLoginUser_wrongName() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        userService.createUser(request1, session1);
        userService.logoutUser(session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request2 = new LoginUserDtoRequest("BadName", request1.getPassword());
        try {
            userService.loginUser(request2, session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_NOT_FOUND, "name"));
        }
    }

    @Test
    public void testLogoutLoginUser_wrongPassword() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        userService.createUser(request1, session1);
        userService.logoutUser(session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request2 = new LoginUserDtoRequest(request1.getName(), "BadPassword");
        try {
            userService.loginUser(request2, session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.INCORRECT_PASSWORD, "password"));
        }
    }

    @Test
    public void testLogoutUser_wrongSession() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(request1, session1);
        try {
            userService.logoutUser(session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }

    @Test
    public void testChangePassword() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        userService.createUser(request1, session1);
        ChangePasswordDtoRequest request2 = new ChangePasswordDtoRequest("User", "123456", "234567");
        userService.changePassword(request2, session1);
        User user = userDao.getUser(session1);
        assertEquals(user.getPassword(), request2.getNewPassword());
    }

    @Test
    public void testChangePassword_wrongSession() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        userService.createUser(request1, session1);
        ChangePasswordDtoRequest request2 = new ChangePasswordDtoRequest("User", "123456", "234567");
        Session session2 = new Session(UUID.randomUUID().toString());
        try {
            userService.changePassword(request2, session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }

    @Test
    public void testChangePassword_wrongOldPassword() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        userService.createUser(request1, session1);
        ChangePasswordDtoRequest request2 = new ChangePasswordDtoRequest("User", "BadPassword", "234567");
        try {
            userService.changePassword(request2, session1);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.INCORRECT_PASSWORD, "oldPassword"));
        }
    }

    @Test
    public void testMakeSuperuser() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        CommonUserDtoResponse response = userService.createUser(request1, session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request2 = new LoginUserDtoRequest("admin", "SuperUnbreakablePassword1!");
        userService.loginUser(request2, session2);
        userService.makeSuperuser(response.getId(), session2);
        User user = userDao.getUser(session1);
        assertEquals(Privilege.SUPER, user.getPrivilege());
    }

    @Test
    public void testMakeSuperuser_wrongSession() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        CommonUserDtoResponse response = userService.createUser(request1, session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request2 = new LoginUserDtoRequest("admin", "SuperUnbreakablePassword1!");
        userService.loginUser(request2, session2);
        Session session3 = new Session(UUID.randomUUID().toString());
        try {
            userService.makeSuperuser(response.getId(), session3);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }

    @Test
    public void testMakeSuperuser_notSuper() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        CommonUserDtoResponse response = userService.createUser(request1, session1);
        try {
            userService.makeSuperuser(response.getId(), session1);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "privilege"));
        }
    }

    @Test
    public void testMakeSuperuser_wrongId() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        userService.createUser(request1, session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request2 = new LoginUserDtoRequest("admin", "SuperUnbreakablePassword1!");
        userService.loginUser(request2, session2);
        try {
            userService.makeSuperuser(9999, session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_NOT_FOUND, "userId"));
        }
    }

    @Test
    public void testMakeSuperuser_alreadySuper() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        CommonUserDtoResponse response = userService.createUser(request1, session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request2 = new LoginUserDtoRequest("admin", "SuperUnbreakablePassword1!");
        userService.loginUser(request2, session2);
        userService.makeSuperuser(response.getId(), session2);
        try {
            userService.makeSuperuser(response.getId(), session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_IS_SUPER, "privilege"));
        }
    }

    @Test
    public void testRestrictUser() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        CommonUserDtoResponse response = userService.createUser(request1, session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request2 = new LoginUserDtoRequest("admin", "SuperUnbreakablePassword1!");
        userService.loginUser(request2, session2);
        userService.restrictUser(response.getId(), session2);
        User user = userDao.getUser(session1);
        assertEquals(BanStatus.LIMITED, user.getBanStatus());
    }

    @Test
    public void testRestrictUser_superUser() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        CommonUserDtoResponse response = userService.createUser(request1, session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request2 = new LoginUserDtoRequest("admin", "SuperUnbreakablePassword1!");
        userService.loginUser(request2, session2);
        userService.makeSuperuser(response.getId(), session2);
        try {
            userService.restrictUser(response.getId(), session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_IS_SUPER, "privilege"));
        }
    }

    @Test
    public void testRestrictUser_alreadyRestrictedUser() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        CommonUserDtoResponse response = userService.createUser(request1, session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request2 = new LoginUserDtoRequest("admin", "SuperUnbreakablePassword1!");
        userService.loginUser(request2, session2);
        userService.restrictUser(response.getId(), session2);
        try {
            userService.restrictUser(response.getId(), session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_IS_RESTRICTED));
        }
    }

    @Test
    public void testRestrictUser_restricterNotSuper() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        CommonUserDtoResponse response = userService.createUser(request1, session1);
        Session session2 = new Session(UUID.randomUUID().toString());
        CreateUserDtoRequest request2 = new CreateUserDtoRequest("User2", "user2@mail.ru", "123456");
        userService.createUser(request2, session2);
        try {
            userService.restrictUser(response.getId(), session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "privilege"));
        }
    }

    @Test
    public void testGetUsersList_RegularUser() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        userService.createUser(request1, session1);
        CreateUserDtoRequest request2 = new CreateUserDtoRequest("User2", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(request2, session2);
        List<UserInfoDtoResponse> usersList = userService.getUsersList(session1);
        assertEquals(3, usersList.size());
    }

    @Test
    public void testGetUsersList_Superuser() throws ForumException {
        CreateUserDtoRequest request1 = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session1 = new Session(UUID.randomUUID().toString());
        userService.createUser(request1, session1);
        CreateUserDtoRequest request2 = new CreateUserDtoRequest("User2", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(request2, session2);
        Session session3 = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest request3 = new LoginUserDtoRequest("admin", "SuperUnbreakablePassword1!");
        userService.loginUser(request3, session3);
        List<UserInfoDtoResponse> usersList = userService.getUsersList(session3);
        assertEquals(3, usersList.size());
    }

}
