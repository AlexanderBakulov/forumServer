package net.thumbtack.forums.daotests;

import net.thumbtack.forums.dao.CommonDao;
import net.thumbtack.forums.dao.ForumDao;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.dto.enums.Order;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.*;
import net.thumbtack.forums.model.enums.PostPriority;
import net.thumbtack.forums.model.enums.ModerateStatus;
import net.thumbtack.forums.model.enums.PostState;
import net.thumbtack.forums.view.ForumInfo;
import net.thumbtack.forums.view.MessageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static net.thumbtack.forums.errors.ForumError.RUNTIME_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ForumDaoTest {

    @Autowired
    UserDao userDao;

    @Autowired
    CommonDao commonDao;

    @Autowired
    ForumDao forumDao;

    @BeforeEach()
    public void clearDatabase() throws ForumException {
        commonDao.clear();
    }

    @Test
    public void testCreateForum() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        assertNotNull(forumDao.getForum(forum.getId()));
    }

    @Test
    public void testCreateForum_alreadyExist() throws ForumException {
        User user1 = new User("A", "aaa", "aaa");
        User user2 = new User("B", "bbb", "bbb");
        Session session1 = new Session("1");
        userDao.createUser(user1, session1);
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        Forum forum1 = new Forum("NewForum", ModerateStatus.MODERATED, user1);
        forumDao.createForum(forum1);
        Forum forum2 = new Forum("NewForum", ModerateStatus.UNMODERATED, user2);
        try {
            forumDao.createForum(forum2);
            fail();
        } catch (ForumException ex) {
            assertEquals(ex.getErrorCode(), RUNTIME_EXCEPTION);
        }
    }

    @Test
    public void testGetForum() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session1 = new Session("1");
        userDao.createUser(user, session1);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Forum forum1 = forumDao.getForum(forum.getId());
        assertEquals(forum.getName(), forum1.getName());
    }

    @Test
    public void testDeleteForum() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session1 = new Session("1");
        userDao.createUser(user, session1);
        Forum forum = new Forum("NewForum", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum);
        forumDao.deleteForum(forum);
        assertNull(forumDao.getForum(forum.getId()));
    }


    @Test
    public void testCreateMessage() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        Post pfdb = forumDao.getPost(message.getId());
        assertNotNull(pfdb);
    }


    @Test
    public void testCreateMessageWithTags() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag("tag1");
        tags.add(tag1);
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, tags);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        assertNotEquals(0,message.getId());
        assertNotEquals(0,tag1.getId());
    }

    @Test
    public void testAddComment() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> messageHistory = new ArrayList<>();
        messageHistory.add(new History("Message body"));
        Post message = new Post(user, header, messageHistory);
        forumDao.createMessage(header, message);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        List<History> postHistory2 = new ArrayList<>();
        postHistory2.add(new History("Another comment body"));
        Post comment2 = new Post(user, header, postHistory2, message);
        forumDao.addComment(comment2);
        Post pfdb = forumDao.getPost(message.getId());
        assertEquals(comment2.getHistory().get(0).getPostBody(),
                    pfdb.getComments().get(1).getHistory().get(0).getPostBody());
    }

    @Test
    public void testAddComment2() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> messageHistory = new ArrayList<>();
        messageHistory.add(new History("Message body"));
        Post message = new Post(user, header, messageHistory);
        forumDao.createMessage(header, message);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        List<History> postHistory2 = new ArrayList<>();
        postHistory2.add(new History("Another comment body"));
        Post comment2 = new Post(user, header, postHistory2, message);
        forumDao.addComment(comment2);
        List<History> postHistory3 = new ArrayList<>();
        postHistory3.add(new History("Comment to comment"));
        Post comment3 = new Post(user,  header, postHistory3, comment2);
        forumDao.addComment(comment3);
        Post pfdb = forumDao.getPost(message.getId());
        assertEquals(comment2.getId(), forumDao.getPost(comment3.getId()).getAncestor().getId());
        assertEquals(comment3.getHistory().get(0).getPostBody(),
                pfdb.getComments().get(1).getComments().get(0).getHistory().get(0).getPostBody());
    }


    @Test
    public void testCreateMessageWithTagsAndComments() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag("tag1");
        tags.add(tag1);
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, tags);
        List<History> messageHistory = new ArrayList<>();
        messageHistory.add(new History("Message body"));
        Post message = new Post(user, header, messageHistory);
        forumDao.createMessage(header, message);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        List<History> postHistory2 = new ArrayList<>();
        postHistory2.add(new History("Another comment body"));
        Post comment2 = new Post(user, header, postHistory2, message);
        forumDao.addComment(comment2);
        List<History> postHistory3 = new ArrayList<>();
        postHistory3.add(new History("Comment to comment"));
        Post comment3 = new Post(user,  header, postHistory3, comment2);
        forumDao.addComment(comment3);
        assertNotEquals(0,message.getId());
        assertNotEquals(0,tag1.getId());
    }

    @Test
    public void testDeleteMessage() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag("tag1");
        tags.add(tag1);
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, tags);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        forumDao.deletePost(message);
        assertNull(forumDao.getPost(message.getId()));
    }

    @Test
    public void testDeleteComment() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> messageHistory = new ArrayList<>();
        messageHistory.add(new History("Message body"));
        Post message = new Post(user, header, messageHistory);
        forumDao.createMessage(header, message);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        List<History> postHistory2 = new ArrayList<>();
        postHistory2.add(new History("Another comment body"));
        Post comment2 = new Post(user, header, postHistory2, message);
        forumDao.addComment(comment2);
        forumDao.deletePost(comment2);
        assertNull(forumDao.getPost(comment2.getId()));
    }

    @Test
    public void testPublishPost() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        forumDao.publishPost(message);
        Post pfdb = forumDao.getPost(message.getId());
        assertEquals(PostState.PUBLISHED, pfdb.getHistory().get(0).getPostState());
    }

    @Test
    public void testChangePriority() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        forumDao.changePriority(PostPriority.HIGH, header);
        Post pfdb = forumDao.getPost(message.getId());
        assertEquals(PostPriority.HIGH, pfdb.getHeader().getPriority());
    }

    @Test
    public void testGetPostWithComments() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag("tag1");
        tags.add(tag1);
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, tags);
        List<History> messageHistory = new ArrayList<>();
        messageHistory.add(new History("Message body"));
        Post message = new Post(user, header, messageHistory);
        forumDao.createMessage(header, message);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        List<History> postHistory2 = new ArrayList<>();
        postHistory2.add(new History("Another comment body"));
        Post comment2 = new Post(user, header, postHistory2, message);
        forumDao.addComment(comment2);
        List<History> postHistory3 = new ArrayList<>();
        postHistory3.add(new History("Comment to comment"));
        Post comment3 = new Post(user,  header, postHistory3, comment2);
        forumDao.addComment(comment3);
        Post pfdb = forumDao.getPost(message.getId());
        assertEquals(comment.getHistory().get(0).getPostBody(),
                    pfdb.getComments().get(0).getHistory().get(0).getPostBody());
    }

    @Test
    public void testSwitchForumToReadOnly() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        forumDao.switchForumsToReadOnly(user);
        Forum ffdb = forumDao.getForum(forum.getId());
        assertTrue(ffdb.getReadOnly());
    }

    @Test
    public void testAddHistory() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        history.add(new History("New message body"));
        forumDao.addHistory(message);
        Post pfdb = forumDao.getPost(message.getId());
        assertEquals(message.getHistory().get(1).getPostBody(), pfdb.getHistory().get(1).getPostBody());
    }


    @Test
    public void testReplaceBody() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        String newBody = "New message body";
        forumDao.replaceBody(message, newBody);
        Post pfdb = forumDao.getPost(message.getId());
        assertEquals(newBody, pfdb.getHistory().get(0).getPostBody());

    }


    @Test
    public void testAddRateToMessage() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        Rate rate = new Rate(5, user2, message);
        forumDao.addRate(rate);
        assertEquals(5, rate.getRate());
    }

    @Test
    public void testAddRates() throws ForumException {
        User user = new User("A", "aaa", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        User user3 = new User("c", "ccc", "c@b.c");
        Session session3 = new Session("3");
        userDao.createUser(user3, session3);
        User user4 = new User("d", "ddd", "d@b.c");
        Session session4 = new Session("4");
        userDao.createUser(user4, session4);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        Rate rate = new Rate(5, user2, message);
        forumDao.addRate(rate);
        Rate rate2 = new Rate(4, user3, message);
        forumDao.addRate(rate2);
        Rate rate3 = new Rate(3, user4, message);
        forumDao.addRate(rate3);
        Post pfdb = forumDao.getPost(message.getId());
        assertEquals(3, pfdb.getRates().size());
    }

    @Test
    public void testAddRateToComment() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        forumDao.addRate(new Rate(5, user2, comment));
        Rate rate = forumDao.getRate(comment, user2);
        assertEquals(5, rate.getRate());
    }

    @Test
    public void testAddRateToMessage_alreadyRated() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        forumDao.addRate(new Rate(5, user, message));
        try {
            forumDao.addRate(new Rate(5, user, message));
            fail();
        } catch(ForumException ex) {
            assertEquals(ex.getErrorCode(), RUNTIME_EXCEPTION);
        }
    }

    @Test
    public void testChangeRateToMessage() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        forumDao.addRate(new Rate(2, user2, message));
        forumDao.changeRate(new Rate(5, user2, message));
        Rate rate = forumDao.getRate(message, user2);
        assertEquals(5, rate.getRate());
    }

    @Test
    public void testClearRate() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        Rate rate = new Rate(5, user2, message);
        forumDao.addRate(rate);
        forumDao.clearRate(rate);
        assertNull(forumDao.getRate(message, user2));
    }

    @Test
    public void testChangeRate() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        Rate rate = new Rate(1, user2, message);
        forumDao.addRate(rate);
        Rate rate2 = new Rate(4, user2, message);
        forumDao.changeRate(rate2);
        assertEquals(4, forumDao.getRate(message, user2).getRate());
    }


    @Test
    public void testExtractBranch() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        Header header2 = new Header(forum, "Another message subject", PostPriority.NORMAL, null);
        List<History> historyM2 = new ArrayList<>();
        historyM2.add(new History("Another message body"));
        Post message2 = new Post(user, header2, historyM2);
        forumDao.createMessage(header2, message2);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user2, header, postHistory1, message);
        forumDao.addComment(comment);
        List<History> postHistory2 = new ArrayList<>();
        postHistory2.add(new History("Comment 2 body"));
        Post comment2 = new Post(user2, header, postHistory2, comment);
        forumDao.addComment(comment2);
        List<History> postHistory3 = new ArrayList<>();
        postHistory3.add(new History("Comment 3 body"));
        Post comment3 = new Post(user2, header, postHistory3, comment);
        forumDao.addComment(comment3);
        List<History> postHistory4 = new ArrayList<>();
        postHistory4.add(new History("Comment 4 body"));
        Post comment4 = new Post(user2, header, postHistory4, comment3);
        forumDao.addComment(comment4);
        List<History> postHistory5 = new ArrayList<>();
        postHistory5.add(new History("Comment 5 body"));
        Post comment5 = new Post(user2, header2, postHistory5, message2);
        forumDao.addComment(comment5);
        Post pfdb = forumDao.getPost(comment.getId());
        Header newHeader = new Header(forum, "Extracted message subject", PostPriority.NORMAL, null);
        forumDao.extractBranch(newHeader, pfdb);
        pfdb = forumDao.getPost(comment.getId());
        assertNull(pfdb.getAncestor());
        assertEquals(newHeader.getId(), pfdb.getHeader().getId());
        assertEquals(newHeader.getId(),pfdb.getComments().get(0).getHeader().getId());
        assertEquals(pfdb.getId(), pfdb.getComments().get(0).getAncestor().getId());
    }


    @Test
    public void testExtractBranch_withTags() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session = new Session("1");
        userDao.createUser(user, session);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        Forum forum = new Forum("NewForum", ModerateStatus.MODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user2, header, postHistory1, message);
        forumDao.addComment(comment);
        List<History> postHistory2 = new ArrayList<>();
        postHistory2.add(new History("Comment 2 body"));
        Post comment2 = new Post(user2, header, postHistory2, comment);
        forumDao.addComment(comment2);
        List<History> postHistory3 = new ArrayList<>();
        postHistory3.add(new History("Comment 3 body"));
        Post comment3 = new Post(user2, header, postHistory3, comment);
        forumDao.addComment(comment3);
        Post pfdb = forumDao.getPost(comment.getId());
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag("tag1");
        tags.add(tag1);
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));
        Header newHeader = new Header(forum, "Extracted message subject", PostPriority.NORMAL, tags);
        forumDao.extractBranch(newHeader, pfdb);
        pfdb = forumDao.getPost(comment.getId());
        assertNull(pfdb.getAncestor());
        assertEquals(newHeader.getId(), pfdb.getHeader().getId());
        assertEquals(newHeader.getId(),pfdb.getComments().get(0).getHeader().getId());
        assertEquals(pfdb.getId(), pfdb.getComments().get(0).getAncestor().getId());
        assertEquals(3, pfdb.getHeader().getTags().size());
    }


    @Test
    public void testGetForumInfo() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session1 = new Session("1");
        userDao.createUser(user, session1);
        Forum forum = new Forum("NewForum", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        Header header2 = new Header(forum, "Message subject 2", PostPriority.NORMAL, null);
        List<History> history2 = new ArrayList<>();
        history2.add(new History("Message body 2"));
        history2.get(0).setPostState(PostState.PUBLISHED);
        Post message2 = new Post(user, header2, history2);
        forumDao.createMessage(header2, message2);
        history2.add(new History("Updated message body 2"));
        history2.get(1).setPostState(PostState.PUBLISHED);
        forumDao.addHistory(message2);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        ForumInfo info = forumDao.getForumInfo(forum);
        assertEquals(forum.getName(), info.getName());
        assertEquals(user.getName(), info.getCreator());
        assertEquals(1, info.getMessageCount());
    }


    @Test
    public void testGetForumInfo2() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session1 = new Session("1");
        userDao.createUser(user, session1);
        Forum forum = new Forum("NewForum", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum);
        ForumInfo info = forumDao.getForumInfo(forum);
        assertEquals(forum.getName(), info.getName());
        assertEquals(user.getName(), info.getCreator());
        assertEquals(0, info.getMessageCount());
    }


    @Test
    public void testGetForumsInfoList() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session1 = new Session("1");
        userDao.createUser(user, session1);
        Forum forum = new Forum("Forum1", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum);
        Forum forum2 = new Forum("Forum2", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum2);
        Forum forum3 = new Forum("Forum3", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum3);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        Header header2 = new Header(forum, "Message subject 2", PostPriority.NORMAL, null);
        List<History> history2 = new ArrayList<>();
        history2.add(new History("Message body 2"));
        history2.get(0).setPostState(PostState.PUBLISHED);
        Post message2 = new Post(user, header2, history2);
        forumDao.createMessage(header2, message2);
        history2.add(new History("Updated message body 2"));
        history2.get(1).setPostState(PostState.PUBLISHED);
        forumDao.addHistory(message2);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        List<ForumInfo> info = forumDao.getForumsInfoList();
        assertEquals(3, info.size());
        assertEquals(1, info.get(0).getMessageCount());
    }

    @Test
    public void testGetForumsInfoList2() throws ForumException {
        User user = new User("A", "bbb", "A@b.c");
        Session session1 = new Session("1");
        userDao.createUser(user, session1);
        Forum forum = new Forum("Forum1", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum);
        Forum forum2 = new Forum("Forum2", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum2);
        Forum forum3 = new Forum("Forum3", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum3);
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, null);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        Header header2 = new Header(forum, "Message subject 2", PostPriority.NORMAL, null);
        List<History> history2 = new ArrayList<>();
        history2.add(new History("Message body 2"));
        history2.get(0).setPostState(PostState.PUBLISHED);
        Post message2 = new Post(user, header2, history2);
        forumDao.createMessage(header2, message2);
        history2.add(new History("Updated message body 2"));
        history2.get(1).setPostState(PostState.PUBLISHED);
        forumDao.addHistory(message2);
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment body"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        Header header3 = new Header(forum3, "Message subject 3", PostPriority.NORMAL, null);
        List<History> history3 = new ArrayList<>();
        history3.add(new History("Message body"));
        history3.get(0).setPostState(PostState.PUBLISHED);
        Post message3 = new Post(user, header3, history3);
        forumDao.createMessage(header3, message3);
        List<ForumInfo> info = forumDao.getForumsInfoList();
        assertEquals(3, info.size());
        assertEquals(1, info.get(0).getMessageCount());
        assertEquals(1, info.get(2).getMessageCount());
    }

    @Test
    public void testGetMessage() throws ForumException {
        //users
        User user = new User("A", "aaa", "A@b.c");
        Session session1 = new Session("1");
        userDao.createUser(user, session1);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        User user3 = new User("C", "ccc", "C@b.c");
        Session session3 = new Session("3");
        userDao.createUser(user3, session3);
        //forums
        Forum forum = new Forum("Forum1", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum);
        Forum forum2 = new Forum("Forum2", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum2);
        Forum forum3 = new Forum("Forum3", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum3);
        //messages
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag("tag1");
        tags.add(tag1);
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, tags);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        history.get(0).setPostState(PostState.PUBLISHED);
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        history.add(new History("New message body"));
        forumDao.addHistory(message);
        Header header2 = new Header(forum, "Message subject 2", PostPriority.NORMAL, null);
        List<History> history2 = new ArrayList<>();
        history2.add(new History("Message body 2"));
        history2.get(0).setPostState(PostState.PUBLISHED);
        Post message2 = new Post(user, header2, history2);
        forumDao.createMessage(header2, message2);
        history2.add(new History("Updated message body 2"));
        history2.get(1).setPostState(PostState.PUBLISHED);
        forumDao.addHistory(message2);
        Header header3 = new Header(forum3, "Message subject 3", PostPriority.NORMAL, null);
        List<History> history3 = new ArrayList<>();
        history3.add(new History("Message body"));
        history3.get(0).setPostState(PostState.PUBLISHED);
        Post message3 = new Post(user, header3, history3);
        forumDao.createMessage(header3, message3);
        //comment
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment 1"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        List<History> postHistory2 = new ArrayList<>();
        postHistory2.add(new History("Comment 2"));
        Post comment2 = new Post(user, header, postHistory2, message);
        forumDao.addComment(comment2);
        List<History> postHistory3 = new ArrayList<>();
        postHistory3.add(new History("Comment to comment 1"));
        Post comment3 = new Post(user, header, postHistory3, comment);
        forumDao.addComment(comment3);
        List<History> postHistory4 = new ArrayList<>();
        postHistory4.add(new History("Comment to comment 2"));
        Post comment4 = new Post(user, header, postHistory4, comment2);
        forumDao.addComment(comment4);
        //rated message
        Rate rate = new Rate(5, user2, message);
        forumDao.addRate(rate);
        Rate rate2 = new Rate(4, user3, message);
        forumDao.addRate(rate2);
        Rate rate3 = new Rate(4, user3, comment);
        forumDao.addRate(rate3);
        //message info allversions
        MessageInfo info = forumDao.getMessage(message, true, false, true, Order.DESC);
        assertEquals(user.getName(), info.getCreator());
        assertEquals(2, info.getBody().size());
        assertEquals(3, info.getTags().size());
        assertEquals(4.5, info.getRating());
        assertEquals(2, info.getRated());
        MessageInfo info2 = forumDao.getMessage(message, false, false, true, Order.DESC);
        assertEquals(1, info2.getBody().size());
        //message info nocomments
        MessageInfo info3 = forumDao.getMessage(message, false, true, false, Order.DESC);
        assertEquals(0, info3.getComments().size());
        MessageInfo info4 = forumDao.getMessage(message, false, false, false, Order.DESC);
        assertEquals(2, info4.getComments().size());
        //message info order
        MessageInfo info5 = forumDao.getMessage(message, false, false, false, Order.ASC);
        assertEquals(comment.getId(), info5.getComments().get(0).getId());
        MessageInfo info6 = forumDao.getMessage(message, false, false, false, Order.DESC);
        assertEquals(comment2.getId(), info6.getComments().get(0).getId());
        //message info unpublished
        MessageInfo info7 = forumDao.getMessage(message, false, false, true, Order.DESC);
        MessageInfo info8 = forumDao.getMessage(message, false, false, false, Order.DESC);
        assertEquals(1, info7.getBody().size());
        assertEquals(0, info8.getBody().size());
    }


    @Test
    public void testGetForumMessages() throws ForumException {
        //users
        User user = new User("A", "aaa", "A@b.c");
        Session session1 = new Session("1");
        userDao.createUser(user, session1);
        User user2 = new User("B", "bbb", "B@b.c");
        Session session2 = new Session("2");
        userDao.createUser(user2, session2);
        User user3 = new User("C", "ccc", "C@b.c");
        Session session3 = new Session("3");
        userDao.createUser(user3, session3);
        //forums
        Forum forum = new Forum("Forum1", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum);
        Forum forum2 = new Forum("Forum2", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum2);
        Forum forum3 = new Forum("Forum3", ModerateStatus.UNMODERATED, user);
        forumDao.createForum(forum3);
        //messages
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag("tag1");
        tags.add(tag1);
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));
        Set<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("tag4"));
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, tags);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        history.get(0).setPostState(PostState.PUBLISHED);
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        history.add(new History("New message body"));
        forumDao.addHistory(message);
        Header header2 = new Header(forum, "Message subject 2", PostPriority.LOW, tags2);
        List<History> history2 = new ArrayList<>();
        history2.add(new History("Message body 2"));
        history2.get(0).setPostState(PostState.PUBLISHED);
        Post message2 = new Post(user, header2, history2);
        forumDao.createMessage(header2, message2);
        history2.add(new History("Updated message body 2"));
        history2.get(1).setPostState(PostState.PUBLISHED);
        forumDao.addHistory(message2);
        Header header3 = new Header(forum, "Message subject 3", PostPriority.HIGH, null);
        List<History> history3 = new ArrayList<>();
        history3.add(new History("Message body 3"));
        history3.get(0).setPostState(PostState.PUBLISHED);
        Post message3 = new Post(user, header3, history3);
        forumDao.createMessage(header3, message3);
        Header header4 = new Header(forum, "Message subject 4", PostPriority.HIGH, null);
        List<History> history4 = new ArrayList<>();
        history4.add(new History("Message body 4"));
        history4.get(0).setPostState(PostState.PUBLISHED);
        Post message4 = new Post(user, header4, history4);
        forumDao.createMessage(header4, message4);
        //comment
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment 1"));
        Post comment = new Post(user, header, postHistory1, message);
        forumDao.addComment(comment);
        List<History> postHistory2 = new ArrayList<>();
        postHistory2.add(new History("Comment 2"));
        Post comment2 = new Post(user, header, postHistory2, message);
        forumDao.addComment(comment2);
        List<History> postHistory3 = new ArrayList<>();
        postHistory3.add(new History("Comment to comment 1"));
        Post comment3 = new Post(user, header, postHistory3, comment);
        forumDao.addComment(comment3);
        List<History> postHistory4 = new ArrayList<>();
        postHistory4.add(new History("Comment to comment 2"));
        Post comment4 = new Post(user, header, postHistory4, comment2);
        forumDao.addComment(comment4);
        //rated message
        Rate rate = new Rate(5, user2, message);
        forumDao.addRate(rate);
        Rate rate2 = new Rate(4, user3, message);
        forumDao.addRate(rate2);
        Rate rate3 = new Rate(4, user3, comment);
        forumDao.addRate(rate3);
        Set<String> tags1 = new HashSet<>();
        tags1.add("tag3");
        tags1.add("tag4");
        List<MessageInfo> infos = forumDao.getForumMessages(forum, true, false, true, null,
                                                            Order.DESC, 0, 1);
        System.out.println("");
    }

//todo add more tests


}
