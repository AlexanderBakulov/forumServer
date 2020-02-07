package net.thumbtack.forums.servicetests;

import net.thumbtack.forums.dao.CommonDao;
import net.thumbtack.forums.dao.ForumDao;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.dto.enums.Decision;
import net.thumbtack.forums.dto.enums.Order;
import net.thumbtack.forums.dto.request.*;
import net.thumbtack.forums.dto.response.*;
import net.thumbtack.forums.errors.ForumError;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.*;
import net.thumbtack.forums.model.enums.BanStatus;
import net.thumbtack.forums.model.enums.ModerateStatus;
import net.thumbtack.forums.model.enums.PostPriority;
import net.thumbtack.forums.model.enums.PostState;
import net.thumbtack.forums.service.ForumService;
import net.thumbtack.forums.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static net.thumbtack.forums.errors.ForumError.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ForumServiceTest {

    @Autowired
    CommonDao commonDao;

    @Autowired
    ForumDao forumDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    ForumService forumService;

    @BeforeEach()
    public void clearDatabase() throws ForumException {
        commonDao.clear();
    }



    @Test
    public void testCreateForum() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        assertEquals(forumDtoResponse.getName(),forumDtoRequest.getName());
    }

    @Test
    public void testCreateForum2() throws ForumException {
        Session session = new Session(UUID.randomUUID().toString());
        LoginUserDtoRequest loginRequest = new LoginUserDtoRequest("admin","SuperUnbreakablePassword1!");
        userService.loginUser(loginRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        assertEquals(forumDtoResponse.getName(),forumDtoRequest.getName());
    }


    @Test
    public void testCreateModeratedForum() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        assertEquals(forumDtoResponse.getName(),forumDtoRequest.getName());
    }


    @Test
    public void testCreateForum_wrongSession() {
        Session session = new Session(UUID.randomUUID().toString());
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        try {
            forumService.createForum(forumDtoRequest, session);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }


    @Test
    public void testCreateForum_bannedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        User user = userDao.getUser(session);
        user.setBanStatus(BanStatus.LIMITED);
        userDao.restrictUser(user);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        try {
            forumService.createForum(forumDtoRequest, session);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_IS_RESTRICTED));
        }
    }


    @Test
    public void testCreateForum_alreadyExist() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        forumService.createForum(forumDtoRequest, session);
        try {
            forumService.createForum(forumDtoRequest, session);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex.getErrorCode(), RUNTIME_EXCEPTION);
        }
    }


    @Test
    public void testDeleteForum() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        forumService.deleteForum(forumDtoResponse.getId(), session);
        assertNull(forumDao.getForum(forumDtoResponse.getId()));
    }


    @Test
    public void testDeleteForum_BannedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        session.getUser().setBanStatus(BanStatus.LIMITED);
        session.getUser().setTimeBanExit(LocalDateTime.of(9999, 1,1,0,0,0));
        userDao.restrictUser(session.getUser());
        try {
            forumService.deleteForum(forumDtoResponse.getId(), session);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.PERMANENT_BAN));
        }
    }


    @Test
    public void testDeleteForum_WrongSession() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        Session session1 = new Session(UUID.randomUUID().toString());
        try {
            forumService.deleteForum(forumDtoResponse.getId(), session1);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }


    @Test
    public void testDeleteForum_WrongId() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        forumService.createForum(forumDtoRequest, session);
        try {
            forumService.deleteForum(9999, session);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.INCORRECT_FORUM_ID, "forumId"));
        }
    }


    @Test
    public void testCreateMessage() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        assertEquals(PostState.PUBLISHED,messageResponse.getState());
    }


    @Test
    public void testCreateMessage_wrongSession() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        Session session1 = new Session(UUID.randomUUID().toString());
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        try {
            forumService.createMessage(messageRequest, session1, forumDtoResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }


    @Test
    public void testCreateMessage_bannedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        session.getUser().setBanStatus(BanStatus.LIMITED);
        session.getUser().setTimeBanExit(LocalDateTime.of(2100, 1,1,0,0,0));
        userDao.restrictUser(session.getUser());
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        try {
            forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_IS_RESTRICTED));
        }
    }


    @Test
    public void testCreateMessage_forumReadOnly() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        forumDao.switchForumsToReadOnly(session.getUser());
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        try {
            forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.READ_ONLY_FORUM, "forumId"));
        }
    }


    @Test
    public void testAddComment() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session, messageResponse.getId());
        assertEquals(PostState.PUBLISHED,commentResponse.getState());
    }

    @Test
    public void testAddComment_unpublishedAncestor() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        try {
        forumService.addComment(commentRequest, session2, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.ANCESTOR_IS_UNPUBLISHED, "postId"));
        }
    }


    @Test
    public void testAddComment_wrongUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        Session session1 = new Session(UUID.randomUUID().toString());
        try {
            forumService.addComment(commentRequest, session1, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }


    @Test
    public void testAddComment_bannedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        session.getUser().setBanStatus(BanStatus.LIMITED);
        session.getUser().setTimeBanExit(LocalDateTime.of(2100, 1,1,0,0,0));
        userDao.restrictUser(session.getUser());
        try {
            forumService.addComment(commentRequest, session, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_IS_RESTRICTED));
        }
    }


    @Test
    public void testAddComment_forumReadOnly() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        forumDao.switchForumsToReadOnly(session.getUser());
        try {
            forumService.addComment(commentRequest, session, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.READ_ONLY_FORUM, "forumId"));
        }
    }


    @Test
    public void testDeleteMessage() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        forumService.deletePost(messageResponse.getId(), session);
        assertNull(forumDao.getPost(messageResponse.getId()));
    }


    @Test
    public void testDeleteMessage_bannedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        session.getUser().setBanStatus(BanStatus.LIMITED);
        session.getUser().setTimeBanExit(LocalDateTime.of(2100, 1,1,0,0,0));
        userDao.restrictUser(session.getUser());
        forumService.deletePost(messageResponse.getId(), session);
        assertNull(forumDao.getPost(messageResponse.getId()));
    }


    @Test
    public void testDeleteMessage_wrongSession() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        Session session1 = new Session(UUID.randomUUID().toString());
        try {
            forumService.deletePost(messageResponse.getId(), session1);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }


    @Test
    public void testDeleteMessage_wrongUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("User2", "user2@mail.ru", "222222");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        try {
            forumService.deletePost(messageResponse.getId(), session2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "JAVASESSIONID"));
        }
    }


    @Test
    public void testDeleteMessage_permanentBannedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        session.getUser().setBanStatus(BanStatus.LIMITED);
        session.getUser().setTimeBanExit(LocalDateTime.of(9999, 1,1,0,0,0));
        userDao.restrictUser(session.getUser());
        try {
            forumService.deletePost(messageResponse.getId(), session);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.PERMANENT_BAN));
        }
    }


    @Test
    public void testDeleteMessage_messageWithComments() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        forumService.addComment(commentRequest, session, messageResponse.getId());
        try {
            forumService.deletePost(messageResponse.getId(), session);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.POST_HAS_COMMENTS, "postId"));
        }
    }


    @Test
    public void testDeleteComment() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session, messageResponse.getId());
        forumService.deletePost(commentResponse.getId(), session);
        assertNull(forumDao.getPost(commentResponse.getId()));
    }


    @Test
    public void testDeleteComment_withComments() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest2 = new AddCommentDtoRequest("Some comment to comment");
        forumService.addComment(commentRequest2, session, commentResponse.getId());
        try {
            forumService.deletePost(commentResponse.getId(), session);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.POST_HAS_COMMENTS, "postId"));
        }
    }


    @Test
    public void testPublishPost_decisionYes() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        Post pfdb = forumDao.getPost(messageResponse.getId());
        assertEquals(PostState.PUBLISHED, pfdb.getHistory().get(0).getPostState());
    }

    @Test
    public void testPublishPost_messageDecisionNo() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequset = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequset, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.NO);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        Post pfdb = forumDao.getPost(messageResponse.getId());
        assertNull(pfdb);
    }

    @Test
    public void testPublishPost_commentDecisionNo() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequset = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequset, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session2, messageResponse.getId());
        PublishPostDtoRequest publishRequest2 = new PublishPostDtoRequest(Decision.NO);
        forumService.publishPost(publishRequest2, session, commentResponse.getId());
        Post pfdb = forumDao.getPost(messageResponse.getId());
        assertEquals(0, pfdb.getComments().size());
    }

    @Test
    public void testPublishPost_wrongSession() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        Session session3 = new Session(UUID.randomUUID().toString());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        try {
            forumService.publishPost(publishRequest, session3, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }

    @Test
    public void testPublishPost_wrongPostId() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        try {
            forumService.publishPost(publishRequest, session2, 99999999);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.INCORRECT_POST_ID, "id"));
        }
    }

    @Test
    public void testPublishPost_UserNotForumOwner() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        try {
            forumService.publishPost(publishRequest, session2, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "JAVASESSIONID"));
        }
    }

    @Test
    public void testPublishPost_RestrictedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequset = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequset, session2, forumDtoResponse.getId());
        session.getUser().setBanStatus(BanStatus.LIMITED);
        session.getUser().setTimeBanExit(LocalDateTime.of(2100, 1,1,0,0,0));
        userDao.restrictUser(session.getUser());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        Post pfdb = forumDao.getPost(messageResponse.getId());
        assertEquals(PostState.PUBLISHED, pfdb.getHistory().get(0).getPostState());

    }

    @Test
    public void testPublishPost_AlreadyPublished() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        try {
            forumService.publishPost(publishRequest, session, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.POST_ALREADY_PUBLISHED, "postId"));
        }
    }

    @Test
    public void testChangePriority() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        ChangePriorityDtoRequest changePriorityRequest = new ChangePriorityDtoRequest(PostPriority.HIGH);
        forumService.changePriority(changePriorityRequest, session2, messageResponse.getId());
        Post pfdb = forumDao.getPost(messageResponse.getId());
        assertEquals(changePriorityRequest.getPriority(), pfdb.getHeader().getPriority());
    }

    @Test
    public void testChangePriority_wrongUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        Session session3 = new Session(UUID.randomUUID().toString());
        ChangePriorityDtoRequest changePriorityRequest = new ChangePriorityDtoRequest(PostPriority.HIGH);
        try {
            forumService.changePriority(changePriorityRequest, session3, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }

    @Test
    public void testChangePriority_bannedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        ChangePriorityDtoRequest changePriorityRequest = new ChangePriorityDtoRequest(PostPriority.HIGH);
        session2.getUser().setBanStatus(BanStatus.LIMITED);
        session2.getUser().setTimeBanExit(LocalDateTime.of(2100, 1,1,0,0,0));
        userDao.restrictUser(session2.getUser());
        try {
            forumService.changePriority(changePriorityRequest, session2, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_IS_RESTRICTED));
        }
    }

    @Test
    public void testChangePriority_wrongPostId() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        ChangePriorityDtoRequest changePriorityRequest = new ChangePriorityDtoRequest(PostPriority.HIGH);
        try {
            forumService.changePriority(changePriorityRequest, session2, 999999999);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.INCORRECT_POST_ID, "id"));
        }
    }

    @Test
    public void testChangePriority_userNotAuthor() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        ChangePriorityDtoRequest changePriorityRequest = new ChangePriorityDtoRequest(PostPriority.HIGH);
        try {
            forumService.changePriority(changePriorityRequest, session, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "JAVASESSIONID"));
        }
    }

    @Test
    public void testChangePriority_postIsNotMessage() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session, messageResponse.getId());
        ChangePriorityDtoRequest changePriorityRequest = new ChangePriorityDtoRequest(PostPriority.HIGH);
        try {
            forumService.changePriority(changePriorityRequest, session, commentResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.POST_IS_NOT_MESSAGE, "postId"));
        }
    }

    @Test
    public void testChangePriority_samePriority() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        ChangePriorityDtoRequest changePriorityRequest = new ChangePriorityDtoRequest(PostPriority.NORMAL);
        try {
            forumService.changePriority(changePriorityRequest, session2, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.SAME_PRIORITY, "priority"));
        }
    }


    @Test
    public void testChangeMessageModeratedForum() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        ChangePostDtoResponse changeMessageResponse = forumService.changePost(changeMessageRequest, session2, messageResponse.getId());
        assertEquals(PostState.UNPUBLISHED, changeMessageResponse.getPostState());
    }

    @Test
    public void testChangeCommentModeratedForum() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session2, messageResponse.getId());
        ChangePostDtoRequest changeCommentRequest = new ChangePostDtoRequest("New comment body");
        ChangePostDtoResponse changeCommentResponse = forumService.changePost(changeCommentRequest, session2, commentResponse.getId());
        assertEquals(PostState.UNPUBLISHED, changeCommentResponse.getPostState());
    }

    @Test
    public void testChangeMessageUnmoderatedForum() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        ChangePostDtoResponse changeMessageResponse = forumService.changePost(changeMessageRequest, session2, messageResponse.getId());
        assertEquals(PostState.PUBLISHED, changeMessageResponse.getPostState());

    }

    @Test
    public void testChangeCommentUnoderatedForum() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session2, messageResponse.getId());
        ChangePostDtoRequest changeCommentRequest = new ChangePostDtoRequest("New comment body");
        ChangePostDtoResponse changeCommentResponse = forumService.changePost(changeCommentRequest, session2, commentResponse.getId());
        assertEquals(PostState.PUBLISHED, changeCommentResponse.getPostState());
    }

    @Test
    public void testChangeMessageModeratedForum_lastMessagePublished() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        ChangePostDtoResponse changeMessageResponse = forumService.changePost(changeMessageRequest, session2, messageResponse.getId());
        assertEquals(PostState.UNPUBLISHED, changeMessageResponse.getPostState());

    }

    @Test
    public void testChangeMessageModeratedForum_AuthorIsForumOwner() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        ChangePostDtoResponse changeMessageResponse = forumService.changePost(changeMessageRequest, session, messageResponse.getId());
        assertEquals(PostState.PUBLISHED, changeMessageResponse.getPostState());
    }

    @Test
    public void testChangeMessage_wrongUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        Session session3 = new Session(UUID.randomUUID().toString());
        try {
            forumService.changePost(changeMessageRequest, session3, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }

    @Test
    public void testChangeMessage_wrongID() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        try {
            forumService.changePost(changeMessageRequest, session2, 9999);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.INCORRECT_POST_ID, "id"));
        }
    }

    @Test
    public void testChangeMessage_restrictedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        userDao.restrictUser(session2.getUser());
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        try {
            forumService.changePost(changeMessageRequest, session2, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_IS_RESTRICTED));
        }
    }


    @Test
    public void testChangeMessage_UserIsNotPostAuthor() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        try {
            forumService.changePost(changeMessageRequest, session, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "JAVASESSIONID"));
        }
    }


    @Test
    public void testRateMessage() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        RatePostDtoRequest rateRequest = new RatePostDtoRequest(5);
        forumService.ratePost(rateRequest, session, messageResponse.getId());
        Rate rfdb = forumDao.getRate(forumDao.getPost(messageResponse.getId()), userDao.getUser(session));
        assertEquals(5, rfdb.getRate());
    }


    @Test
    public void testRateMessage_wrongUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        RatePostDtoRequest rateRequest = new RatePostDtoRequest(5);
        Session session3 = new Session(UUID.randomUUID().toString());
        try {
        forumService.ratePost(rateRequest, session3, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }


    @Test
    public void testRateMessage_bannedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        session.getUser().setBanStatus(BanStatus.LIMITED);
        session.getUser().setTimeBanExit(LocalDateTime.of(2100, 1,1,0,0,0));
        userDao.restrictUser(session.getUser());
        RatePostDtoRequest rateRequest = new RatePostDtoRequest(5);
        forumService.ratePost(rateRequest, session, messageResponse.getId());
        Rate rfdb = forumDao.getRate(forumDao.getPost(messageResponse.getId()), userDao.getUser(session));
        assertEquals(5, rfdb.getRate());
    }


    @Test
        public void testRateMessage_permanentBan() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        RatePostDtoRequest rateRequest = new RatePostDtoRequest(5);
        session.getUser().setBanStatus(BanStatus.LIMITED);
        session.getUser().setTimeBanExit(LocalDateTime.of(9999, 1,1,0,0,0));
        userDao.restrictUser(session.getUser());
        try {
            forumService.ratePost(rateRequest, session, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.PERMANENT_BAN));
        }
    }


    @Test
    public void testRateMessage_RateByPostAuthor() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        RatePostDtoRequest rateRequest = new RatePostDtoRequest(5);
        try {
            forumService.ratePost(rateRequest, session2, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "JAVASESSIONID"));
        }
    }


    @Test
    public void testRateMessage_emptyValue() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        RatePostDtoRequest rateRequest = new RatePostDtoRequest(null);
        try {
            forumService.ratePost(rateRequest, session, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.EMPTY_RATE, "value"));
        }
    }

    @Test
    public void testClearRate() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        RatePostDtoRequest rateRequest = new RatePostDtoRequest(5);
        forumService.ratePost(rateRequest, session, messageResponse.getId());
        RatePostDtoRequest rateRequest2 = new RatePostDtoRequest(null);
        forumService.ratePost(rateRequest2, session, messageResponse.getId());
        Rate rfdb = forumDao.getRate(forumDao.getPost(messageResponse.getId()), userDao.getUser(session));
        assertNull(rfdb);
    }

    @Test
    public void testChangeRate() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        RatePostDtoRequest rateRequest = new RatePostDtoRequest(5);
        forumService.ratePost(rateRequest, session, messageResponse.getId());
        RatePostDtoRequest rateRequest2 = new RatePostDtoRequest(4);
        forumService.ratePost(rateRequest2, session, messageResponse.getId());
        Rate rfdb = forumDao.getRate(forumDao.getPost(messageResponse.getId()), userDao.getUser(session));
        assertEquals(4, rfdb.getRate());
    }


    @Test
    public void testExtractBranch() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest2 = new AddCommentDtoRequest("Some comment 2");
        forumService.addComment(commentRequest2, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest3 = new AddCommentDtoRequest("Some comment 3");
        forumService.addComment(commentRequest3, session2, commentResponse.getId());
        ExtractBranchDtoRequest extractRequest = new ExtractBranchDtoRequest("New message subject", PostPriority.HIGH);
        ExtractBranchDtoResponse extractResponse = forumService.extractBranch(extractRequest, session, commentResponse.getId());
        assertEquals(commentResponse.getId(), extractResponse.getId());
    }

    @Test
    public void testExtractBranch_wrongSession() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest2 = new AddCommentDtoRequest("Some comment 2");
        forumService.addComment(commentRequest2, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest3 = new AddCommentDtoRequest("Some comment 3");
        forumService.addComment(commentRequest3, session2, commentResponse.getId());
        ExtractBranchDtoRequest extractRequest = new ExtractBranchDtoRequest("New message subject", PostPriority.HIGH);
        Session session3 = new Session(UUID.randomUUID().toString());
        try {
            forumService.extractBranch(extractRequest, session3, commentResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(USER_NOT_FOUND, "JAVASESSIONID"));
        }
    }


    @Test
    public void testExtractBranch_bannedUser() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest2 = new AddCommentDtoRequest("Some comment 2");
        forumService.addComment(commentRequest2, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest3 = new AddCommentDtoRequest("Some comment 3");
        forumService.addComment(commentRequest3, session2, commentResponse.getId());
        session.getUser().setBanStatus(BanStatus.LIMITED);
        session.getUser().setTimeBanExit(LocalDateTime.of(2100, 1,1,0,0,0));
        userDao.restrictUser(session.getUser());
        ExtractBranchDtoRequest extractRequest = new ExtractBranchDtoRequest("New message subject", PostPriority.HIGH);
        try {
            forumService.extractBranch(extractRequest, session, commentResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_IS_RESTRICTED));
        }
    }

    @Test
    public void testExtractBranch_wrongCommentId() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest2 = new AddCommentDtoRequest("Some comment 2");
        forumService.addComment(commentRequest2, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest3 = new AddCommentDtoRequest("Some comment 3");
        forumService.addComment(commentRequest3, session2, commentResponse.getId());
        ExtractBranchDtoRequest extractRequest = new ExtractBranchDtoRequest("New message subject", PostPriority.HIGH);
        try {
            forumService.extractBranch(extractRequest, session, 99999);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.INCORRECT_POST_ID, "id"));
        }
    }


    @Test
    public void testExtractBranch_isNotForumOwner() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest2 = new AddCommentDtoRequest("Some comment 2");
        forumService.addComment(commentRequest2, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest3 = new AddCommentDtoRequest("Some comment 3");
        forumService.addComment(commentRequest3, session2, commentResponse.getId());
        ExtractBranchDtoRequest extractRequest = new ExtractBranchDtoRequest("New message subject", PostPriority.HIGH);
        try {
            forumService.extractBranch(extractRequest, session2, commentResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "JAVASESSIONID"));
        }
    }


    @Test
    public void testExtractBranch_alreadyMessage() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("Message author", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse = forumService.addComment(commentRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest2 = new AddCommentDtoRequest("Some comment 2");
        forumService.addComment(commentRequest2, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest3 = new AddCommentDtoRequest("Some comment 3");
        forumService.addComment(commentRequest3, session2, commentResponse.getId());
        ExtractBranchDtoRequest extractRequest = new ExtractBranchDtoRequest("New message subject", PostPriority.HIGH);
        try {
            forumService.extractBranch(extractRequest, session, messageResponse.getId());
            fail();
        } catch (ForumException ex) {
            assertEquals(ex, new ForumException(ForumError.POST_IS_MESSAGE, "postId"));
        }
    }


    @Test
    public void testGetForumInfo() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("Forum Owner", "user@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("User", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        //create messages (2 published and 1 unpublished)
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        CreateMessageDtoRequest messageRequest2 = new CreateMessageDtoRequest("Subject2", "Message body2", PostPriority.NORMAL);
        forumService.createMessage(messageRequest2, session2, forumDtoResponse.getId());
        CreateMessageDtoRequest messageRequest3 = new CreateMessageDtoRequest("Subject3", "Message body3", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse3 = forumService.createMessage(messageRequest3, session, forumDtoResponse.getId());
        //add comments (2 published and 2 unpublished)
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        forumService.addComment(commentRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest2 = new AddCommentDtoRequest("Some comment2");
        forumService.addComment(commentRequest2, session2, messageResponse.getId());
        AddCommentDtoRequest commentRequest3 = new AddCommentDtoRequest("Some comment3");
        forumService.addComment(commentRequest3, session, messageResponse3.getId());
        AddCommentDtoRequest commentRequest4 = new AddCommentDtoRequest("Some comment4");
        forumService.addComment(commentRequest4, session2, messageResponse3.getId());
        //change published post
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        forumService.changePost(changeMessageRequest, session, messageResponse.getId());
        //get forum info
        ForumInfoDtoResponse forumInfo = forumService.getForumInfo(forumDtoResponse.getId(), session);
        assertEquals(forumDtoResponse.getName(), forumInfo.getName());
        assertEquals(session.getUser().getName(), forumInfo.getCreator());
        assertEquals(forumDtoResponse.getType(), forumInfo.getType());
        assertFalse(forumInfo.isReadonly());
        assertEquals(2, forumInfo.getMessageCount());
        assertEquals(2, forumInfo.getCommentCount());
    }


    @Test
    public void testGetForumsInfoList() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User1", "user1@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("User2", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        //create forums
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        CreateForumDtoRequest forumDtoRequest2 = new CreateForumDtoRequest("Forum2", ModerateStatus.UNMODERATED);
        CreateForumDtoResponse forumDtoResponse2 = forumService.createForum(forumDtoRequest2, session2);
        CreateForumDtoRequest forumDtoRequest3 = new CreateForumDtoRequest("Forum3", ModerateStatus.MODERATED);
        forumService.createForum(forumDtoRequest3, session);
        //create messages to forum 1 (2 published and 1 unpublished)
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session, forumDtoResponse.getId());
        CreateMessageDtoRequest messageRequest2 = new CreateMessageDtoRequest("Subject2", "Message body2", PostPriority.NORMAL);
        forumService.createMessage(messageRequest2, session2, forumDtoResponse.getId());
        CreateMessageDtoRequest messageRequest3 = new CreateMessageDtoRequest("Subject3", "Message body3", PostPriority.NORMAL);
        AddPostDtoResponse messageResponse3 = forumService.createMessage(messageRequest3, session, forumDtoResponse.getId());
        //add comments to forum 1 (2 published and 2 unpublished)
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        forumService.addComment(commentRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest2 = new AddCommentDtoRequest("Some comment2");
        forumService.addComment(commentRequest2, session2, messageResponse.getId());
        AddCommentDtoRequest commentRequest3 = new AddCommentDtoRequest("Some comment3");
        forumService.addComment(commentRequest3, session, messageResponse3.getId());
        AddCommentDtoRequest commentRequest4 = new AddCommentDtoRequest("Some comment4");
        forumService.addComment(commentRequest4, session2, messageResponse3.getId());
        //change published post
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        forumService.changePost(changeMessageRequest, session, messageResponse.getId());
        //create messages to forum 2 (1 published and 0 unpublished)
        CreateMessageDtoRequest messageRequest21 = new CreateMessageDtoRequest("Subject21", "Message body21", PostPriority.HIGH);
        forumService.createMessage(messageRequest21, session, forumDtoResponse2.getId());
        //get forum info
        List<ForumInfoDtoResponse> dtoResponse = forumService.getForumsInfoList(session);
        assertEquals(forumDtoResponse.getName(), dtoResponse.get(0).getName());
        assertEquals(session.getUser().getName(), dtoResponse.get(0).getCreator());
        assertEquals(3, dtoResponse.size());
        assertFalse(dtoResponse.get(0).isReadonly());
        assertEquals(2, dtoResponse.get(0).getMessageCount());
        assertEquals(2, dtoResponse.get(0).getCommentCount());
    }

    @Test
    public void testGetMessage() throws ForumException {
        CreateUserDtoRequest userDtoRequest = new CreateUserDtoRequest("User1", "user1@mail.ru", "123456");
        Session session = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest, session);
        CreateUserDtoRequest userDtoRequest2 = new CreateUserDtoRequest("User2", "user2@mail.ru", "123456");
        Session session2 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest2, session2);
        CreateUserDtoRequest userDtoRequest3 = new CreateUserDtoRequest("User3", "user3@mail.ru", "123456");
        Session session3 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest3, session3);
        CreateUserDtoRequest userDtoRequest4 = new CreateUserDtoRequest("User4", "user4@mail.ru", "123456");
        Session session4 = new Session(UUID.randomUUID().toString());
        userService.createUser(userDtoRequest4, session4);
        //create forums
        CreateForumDtoRequest forumDtoRequest = new CreateForumDtoRequest("Forum1", ModerateStatus.MODERATED);
        CreateForumDtoResponse forumDtoResponse = forumService.createForum(forumDtoRequest, session);
        //create messages to forum 1
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag("tag1");
        tags.add(tag1);
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));
        //create message 1
        CreateMessageDtoRequest messageRequest = new CreateMessageDtoRequest("Subject", "Message body", PostPriority.HIGH, tags);
        AddPostDtoResponse messageResponse = forumService.createMessage(messageRequest, session2, forumDtoResponse.getId());
        //publish message 1
        PublishPostDtoRequest publishRequest = new PublishPostDtoRequest(Decision.YES);
        forumService.publishPost(publishRequest, session, messageResponse.getId());
        //add comments to message 1
        AddCommentDtoRequest commentRequest = new AddCommentDtoRequest("Some comment");
        AddPostDtoResponse commentResponse1 = forumService.addComment(commentRequest, session, messageResponse.getId());
        AddCommentDtoRequest commentRequest3 = new AddCommentDtoRequest("Some comment3");
        forumService.addComment(commentRequest3, session2, commentResponse1.getId());
        //change published post
        ChangePostDtoRequest changeMessageRequest = new ChangePostDtoRequest("New message body");
        forumService.changePost(changeMessageRequest, session2, messageResponse.getId());
         //rate posts
        RatePostDtoRequest rateRequest = new RatePostDtoRequest(5);
        forumService.ratePost(rateRequest, session, messageResponse.getId());
        RatePostDtoRequest rateRequest2 = new RatePostDtoRequest(4);
        forumService.ratePost(rateRequest2, session3, messageResponse.getId());
        RatePostDtoRequest rateRequest3 = new RatePostDtoRequest(3);
        forumService.ratePost(rateRequest3, session4, messageResponse.getId());
        RatePostDtoRequest rateRequest4 = new RatePostDtoRequest(5);
        forumService.ratePost(rateRequest, session2, commentResponse1.getId());
        RatePostDtoRequest rateRequest5 = new RatePostDtoRequest(4);
        forumService.ratePost(rateRequest2, session3, commentResponse1.getId());
        RatePostDtoRequest rateRequest6 = new RatePostDtoRequest(3);
        forumService.ratePost(rateRequest3, session4, commentResponse1.getId());
        //get message
        MessageInfoDtoResponse info = forumService.getMessage(session, messageResponse.getId(), true,
                                                    false, false, Order.DESC);
        System.out.println(info);
    }

    //todo add more tests

}
