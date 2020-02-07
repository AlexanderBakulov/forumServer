package net.thumbtack.forums.daotests;

import net.thumbtack.forums.dao.CommonDao;
import net.thumbtack.forums.dao.ForumDao;
import net.thumbtack.forums.dao.StatisticsDao;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.*;
import net.thumbtack.forums.model.enums.ModerateStatus;
import net.thumbtack.forums.model.enums.PostPriority;
import net.thumbtack.forums.model.enums.PostState;
import net.thumbtack.forums.view.PostRatingInfo;
import net.thumbtack.forums.view.PostQuantityInfo;
import net.thumbtack.forums.view.UserRatingInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StatisticsDaoTest {

    @Autowired
    UserDao userDao;

    @Autowired
    CommonDao commonDao;

    @Autowired
    ForumDao forumDao;

    @Autowired
    StatisticsDao statisticsDao;


    @BeforeEach()
    public void clearDatabase() throws ForumException {
        commonDao.clear();
    }


    @Test
    public void testGetPostQuantity_Forum() throws ForumException {
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
        Header header5 = new Header(forum2, "Message subject 5", PostPriority.NORMAL, null);
        List<History> history5 = new ArrayList<>();
        history5.add(new History("Message body 5"));
        history5.get(0).setPostState(PostState.PUBLISHED);
        Post message5 = new Post(user, header5, history5);
        forumDao.createMessage(header5, message5);
        Header header6 = new Header(forum3, "Message subject 6", PostPriority.NORMAL, null);
        List<History> history6 = new ArrayList<>();
        history6.add(new History("Message body 6"));
        history6.get(0).setPostState(PostState.PUBLISHED);
        Post message6 = new Post(user, header6, history6);
        forumDao.createMessage(header6, message6);
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
        //get statistics
        PostQuantityInfo statisticInfo = statisticsDao.getPostQuantity(forum.getId());
        assertEquals(4, statisticInfo.getMessageCount());
        assertEquals(4, statisticInfo.getCommentCount());
        PostQuantityInfo statisticInfo2 = statisticsDao.getPostQuantity(forum2.getId());
        assertEquals(1, statisticInfo2.getMessageCount());
        assertEquals(0, statisticInfo2.getCommentCount());
    }

    @Test
    public void testGetPostQuantity_allServer() throws ForumException {
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
        Header header5 = new Header(forum2, "Message subject 5", PostPriority.NORMAL, null);
        List<History> history5 = new ArrayList<>();
        history5.add(new History("Message body 5"));
        history5.get(0).setPostState(PostState.PUBLISHED);
        Post message5 = new Post(user, header5, history5);
        forumDao.createMessage(header5, message5);
        Header header6 = new Header(forum3, "Message subject 6", PostPriority.NORMAL, null);
        List<History> history6 = new ArrayList<>();
        history6.add(new History("Message body 6"));
        history6.get(0).setPostState(PostState.PUBLISHED);
        Post message6 = new Post(user, header6, history6);
        forumDao.createMessage(header6, message6);
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
        //get statistics
        PostQuantityInfo statisticInfo = statisticsDao.getPostQuantity(0);
        assertEquals(6, statisticInfo.getMessageCount());
        assertEquals(4, statisticInfo.getCommentCount());
    }

    @Test
    public void testGetPostRatings_Forum() throws ForumException {
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
        Header header5 = new Header(forum2, "Message subject 5", PostPriority.NORMAL, null);
        List<History> history5 = new ArrayList<>();
        history5.add(new History("Message body 5"));
        history5.get(0).setPostState(PostState.PUBLISHED);
        Post message5 = new Post(user, header5, history5);
        forumDao.createMessage(header5, message5);
        Header header6 = new Header(forum3, "Message subject 6", PostPriority.NORMAL, null);
        List<History> history6 = new ArrayList<>();
        history6.add(new History("Message body 6"));
        history6.get(0).setPostState(PostState.PUBLISHED);
        Post message6 = new Post(user, header6, history6);
        forumDao.createMessage(header6, message6);
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
        //rate posts
        Rate rate = new Rate(5, user2, message);
        forumDao.addRate(rate);
        Rate rate2 = new Rate(4, user3, message);
        forumDao.addRate(rate2);
        Rate rate3 = new Rate(4, user3, comment);
        forumDao.addRate(rate3);
        Rate rate4 = new Rate(5, user2, message2);
        forumDao.addRate(rate4);
        Rate rate5 = new Rate(4, user3, message3);
        forumDao.addRate(rate5);
        Rate rate6 = new Rate(4, user3, comment2);
        forumDao.addRate(rate6);
        List<PostRatingInfo> ratingInfos = statisticsDao.getPostRatings(forum.getId(),0,20);
        assertEquals(8, ratingInfos.size());
    }


    @Test
    public void testGetPostRatings_Server() throws ForumException {
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
        Header header5 = new Header(forum2, "Message subject 5", PostPriority.NORMAL, null);
        List<History> history5 = new ArrayList<>();
        history5.add(new History("Message body 5"));
        history5.get(0).setPostState(PostState.PUBLISHED);
        Post message5 = new Post(user, header5, history5);
        forumDao.createMessage(header5, message5);
        Header header6 = new Header(forum3, "Message subject 6", PostPriority.NORMAL, null);
        List<History> history6 = new ArrayList<>();
        history6.add(new History("Message body 6"));
        history6.get(0).setPostState(PostState.PUBLISHED);
        Post message6 = new Post(user, header6, history6);
        forumDao.createMessage(header6, message6);
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
        //rate posts
        Rate rate = new Rate(5, user2, message);
        forumDao.addRate(rate);
        Rate rate2 = new Rate(4, user3, message);
        forumDao.addRate(rate2);
        Rate rate3 = new Rate(4, user3, comment);
        forumDao.addRate(rate3);
        Rate rate4 = new Rate(5, user2, message2);
        forumDao.addRate(rate4);
        Rate rate5 = new Rate(4, user3, message3);
        forumDao.addRate(rate5);
        Rate rate6 = new Rate(4, user3, comment2);
        forumDao.addRate(rate6);
        List<PostRatingInfo> ratingInfos = statisticsDao.getPostRatings(0,0,20);
        assertEquals(10, ratingInfos.size());
        List<PostRatingInfo> ratingInfos2 = statisticsDao.getPostRatings(0,0,7);
        assertEquals(7, ratingInfos2.size());
        List<PostRatingInfo> ratingInfos3 = statisticsDao.getPostRatings(0,3,10);
        assertEquals(7, ratingInfos3.size());
    }

    //not complete, wrong result
//    @Test
//    public void testGetUserRatings_Forum() throws ForumException {
//        //users
//        User user = new User("A", "aaa", "A@b.c");
//        Session session1 = new Session("1");
//        userDao.createUser(user, session1);
//        User user2 = new User("B", "bbb", "B@b.c");
//        Session session2 = new Session("2");
//        userDao.createUser(user2, session2);
//        User user3 = new User("C", "ccc", "C@b.c");
//        Session session3 = new Session("3");
//        userDao.createUser(user3, session3);
//        //forums
//        Forum forum = new Forum("Forum1", ModerateStatus.UNMODERATED, user);
//        forumDao.createForum(forum);
//        Forum forum2 = new Forum("Forum2", ModerateStatus.UNMODERATED, user);
//        forumDao.createForum(forum2);
//        Forum forum3 = new Forum("Forum3", ModerateStatus.UNMODERATED, user);
//        forumDao.createForum(forum3);
//        //messages
//        Set<Tag> tags = new HashSet<>();
//        Tag tag1 = new Tag("tag1");
//        tags.add(tag1);
//        tags.add(new Tag("tag2"));
//        tags.add(new Tag("tag3"));
//        Set<Tag> tags2 = new HashSet<>();
//        tags2.add(new Tag("tag4"));
//        //
//        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, tags);
//        List<History> history = new ArrayList<>();
//        history.add(new History("Message body"));
//        history.get(0).setPostState(PostState.PUBLISHED);
//        Post message = new Post(user, header, history);
//        forumDao.createMessage(header, message);
//        history.add(new History("New message body"));
//        forumDao.addHistory(message);
//        //
//        Header header2 = new Header(forum, "Message subject 2", PostPriority.LOW, tags2);
//        List<History> history2 = new ArrayList<>();
//        history2.add(new History("Message body 2"));
//        history2.get(0).setPostState(PostState.PUBLISHED);
//        Post message2 = new Post(user, header2, history2);
//        forumDao.createMessage(header2, message2);
//        history2.add(new History("Updated message body 2"));
//        history2.get(1).setPostState(PostState.PUBLISHED);
//        forumDao.addHistory(message2);
//        //
//        Header header3 = new Header(forum2, "Message subject 3", PostPriority.HIGH, null);
//        List<History> history3 = new ArrayList<>();
//        history3.add(new History("Message body 3"));
//        history3.get(0).setPostState(PostState.PUBLISHED);
//        Post message3 = new Post(user2, header3, history3);
//        forumDao.createMessage(header3, message3);
//        //
//        Header header4 = new Header(forum2, "Message subject 4", PostPriority.HIGH, null);
//        List<History> history4 = new ArrayList<>();
//        history4.add(new History("Message body 4"));
//        history4.get(0).setPostState(PostState.PUBLISHED);
//        Post message4 = new Post(user2, header4, history4);
//        forumDao.createMessage(header4, message4);
//        //
//        Header header5 = new Header(forum3, "Message subject 5", PostPriority.NORMAL, null);
//        List<History> history5 = new ArrayList<>();
//        history5.add(new History("Message body 5"));
//        history5.get(0).setPostState(PostState.PUBLISHED);
//        Post message5 = new Post(user3, header5, history5);
//        forumDao.createMessage(header5, message5);
//        //
//        Header header6 = new Header(forum3, "Message subject 6", PostPriority.NORMAL, null);
//        List<History> history6 = new ArrayList<>();
//        history6.add(new History("Message body 6"));
//        history6.get(0).setPostState(PostState.PUBLISHED);
//        Post message6 = new Post(user3, header6, history6);
//        forumDao.createMessage(header6, message6);
//        //rate posts
//        Rate rate = new Rate(5, user2, message);
//        forumDao.addRate(rate);
//        Rate rate2 = new Rate(4, user3, message2);
//        forumDao.addRate(rate2);
//        Rate rate3 = new Rate(4, user, message3);
//        forumDao.addRate(rate3);
//        Rate rate4 = new Rate(5, user3, message4);
//        forumDao.addRate(rate4);
//        Rate rate5 = new Rate(4, user, message5);
//        forumDao.addRate(rate5);
//        Rate rate6 = new Rate(4, user2, message6);
//        forumDao.addRate(rate6);
//        List<UserRatingInfo> ratingInfos = statisticsDao.getUserRatings(forum.getId(),0,20);
//        assertEquals(3, ratingInfos.size());
//        assertEquals(2, ratingInfos.get(0).getRated());
//    }


    @Test
    public void testGetUserRatings_Server() throws ForumException {
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
        //
        Header header = new Header(forum, "Message subject", PostPriority.NORMAL, tags);
        List<History> history = new ArrayList<>();
        history.add(new History("Message body"));
        history.get(0).setPostState(PostState.PUBLISHED);
        Post message = new Post(user, header, history);
        forumDao.createMessage(header, message);
        history.add(new History("New message body"));
        forumDao.addHistory(message);
        //
        Header header2 = new Header(forum2, "Message subject 2", PostPriority.LOW, tags2);
        List<History> history2 = new ArrayList<>();
        history2.add(new History("Message body 2"));
        history2.get(0).setPostState(PostState.PUBLISHED);
        Post message2 = new Post(user, header2, history2);
        forumDao.createMessage(header2, message2);
        history2.add(new History("Updated message body 2"));
        history2.get(1).setPostState(PostState.PUBLISHED);
        forumDao.addHistory(message2);
        //
        Header header3 = new Header(forum, "Message subject 3", PostPriority.HIGH, null);
        List<History> history3 = new ArrayList<>();
        history3.add(new History("Message body 3"));
        history3.get(0).setPostState(PostState.PUBLISHED);
        Post message3 = new Post(user2, header3, history3);
        forumDao.createMessage(header3, message3);
        //
        Header header4 = new Header(forum2, "Message subject 4", PostPriority.HIGH, null);
        List<History> history4 = new ArrayList<>();
        history4.add(new History("Message body 4"));
        history4.get(0).setPostState(PostState.PUBLISHED);
        Post message4 = new Post(user2, header4, history4);
        forumDao.createMessage(header4, message4);
        //
        Header header5 = new Header(forum2, "Message subject 5", PostPriority.NORMAL, null);
        List<History> history5 = new ArrayList<>();
        history5.add(new History("Message body 5"));
        history5.get(0).setPostState(PostState.PUBLISHED);
        Post message5 = new Post(user3, header5, history5);
        forumDao.createMessage(header5, message5);
        //
        Header header6 = new Header(forum3, "Message subject 6", PostPriority.NORMAL, null);
        List<History> history6 = new ArrayList<>();
        history6.add(new History("Message body 6"));
        history6.get(0).setPostState(PostState.PUBLISHED);
        Post message6 = new Post(user3, header6, history6);
        forumDao.createMessage(header6, message6);
        //comment
        List<History> postHistory1 = new ArrayList<>();
        postHistory1.add(new History("Comment 1"));
        Post comment = new Post(user2, header, postHistory1, message);
        forumDao.addComment(comment);
        //
        List<History> postHistory2 = new ArrayList<>();
        postHistory2.add(new History("Comment 2"));
        Post comment2 = new Post(user3, header, postHistory2, message);
        forumDao.addComment(comment2);
        //
        List<History> postHistory3 = new ArrayList<>();
        postHistory3.add(new History("Comment to comment 1"));
        Post comment3 = new Post(user2, header, postHistory3, comment);
        forumDao.addComment(comment3);
        //
        List<History> postHistory4 = new ArrayList<>();
        postHistory4.add(new History("Comment to comment 2"));
        Post comment4 = new Post(user3, header, postHistory4, comment2);
        forumDao.addComment(comment4);
        //rate posts
        Rate rate = new Rate(5, user2, message);
        forumDao.addRate(rate);
        Rate rate2 = new Rate(4, user3, message2);
        forumDao.addRate(rate2);
        Rate rate3 = new Rate(4, user, message3);
        forumDao.addRate(rate3);
        Rate rate4 = new Rate(5, user3, message4);
        forumDao.addRate(rate4);
        Rate rate5 = new Rate(4, user, message5);
        forumDao.addRate(rate5);
        Rate rate6 = new Rate(4, user, comment);
        forumDao.addRate(rate6);
        List<UserRatingInfo> ratingInfos = statisticsDao.getUserRatings(0,0,20);
        assertEquals(3, ratingInfos.size());
        assertEquals(2, ratingInfos.get(0).getRated());
    }

}
