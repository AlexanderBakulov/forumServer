package net.thumbtack.forums.daoimpl;

import net.thumbtack.forums.dao.ForumDao;
import net.thumbtack.forums.dto.enums.Order;
import net.thumbtack.forums.errors.ForumError;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.mappers.mybatis.ForumMapper;
import net.thumbtack.forums.model.*;
import net.thumbtack.forums.model.enums.PostPriority;
import net.thumbtack.forums.view.ForumInfo;
import net.thumbtack.forums.view.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component
public class ForumDaoImpl implements ForumDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    private final ForumMapper forumMapper;

    public ForumDaoImpl(ForumMapper forumMapper) {
        this.forumMapper = forumMapper;
    }



    @Override
    public Forum createForum(Forum forum) throws ForumException {
        LOGGER.debug("Create forum {}", forum);
        try {
            forumMapper.createForum(forum);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't create forum {}, exception ", forum, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
        return forum;
    }


    @Override
    public void deleteForum(Forum forum) throws ForumException {
        LOGGER.debug("Delete forum {}", forum);
        try {
            forumMapper.deleteForum(forum);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete forum {}, exception ", forum, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public ForumInfo getForumInfo(Forum forum) throws ForumException {
        LOGGER.debug("Get info for forum {}", forum);
        try {
            return forumMapper.getForumInfo(forum);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get info for forum {}, exception ", forum, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public List<ForumInfo> getForumsInfoList() throws ForumException {
        LOGGER.debug("Get forums info");
        try {
            return forumMapper.getForumsInfoList();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get forums info, exception ", ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public Forum getForum(int forumId) throws ForumException {
        LOGGER.debug("Get forum with id {}", forumId);
        try {
            return forumMapper.getForum(forumId);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get forum with id {}, exception ", forumId, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public Post createMessage(Header header, Post message) throws ForumException {
        LOGGER.debug("Create message {}", message);
        try {
            createHeader(header, message);
            forumMapper.createMessage(header, message);
            forumMapper.addHistory(message.getId(), message.getHistory().get(0));
        } catch (RuntimeException ex) {
            LOGGER.info("Can't create message {}, exception ", message, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
        return message;
    }


    @Override
    public Post addComment(Post comment) throws ForumException {
        LOGGER.debug("Add comment {}", comment);
        try {
            forumMapper.addComment(comment);
            forumMapper.addHistory(comment.getId(), comment.getHistory().get(0));
        } catch (RuntimeException ex) {
            LOGGER.info("Can't add comment {}, exception ", comment, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
        return comment;
    }


    @Override
    public void switchForumsToReadOnly(User user) throws ForumException {
        LOGGER.debug("Switch moderated forums of user {} to read-only mode", user);
        try {
            forumMapper.switchForumsToReadOnly(user);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't switch moderated forums of user {} to read-only mode, exception ", user, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public void deletePost(Post post) throws ForumException {
        LOGGER.debug("Delete post {}", post);
        try {
            delete(post);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete post {}, exception ", post, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


   @Override
    public void publishPost(Post post) throws ForumException {
        LOGGER.debug("Publish post {}", post);
        try {
            forumMapper.publishPost(post);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't publish post {}, exception ", post, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public void changePriority(PostPriority priority, Header header) throws ForumException {
        LOGGER.debug("Change priority to message header {}", header);
        try {
            forumMapper.changePriority(priority, header);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't change priority to message header {}, exception ", header, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public void addHistory(Post post) throws ForumException {
        LOGGER.debug("Add to history from post {}", post);
        try {
            forumMapper.addHistory(post.getId(), post.getHistory().get(post.getHistory().size() - 1));
        } catch (RuntimeException ex) {
            LOGGER.info("Can't add to history from post {}, exception ", post, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }

    @Override
    public void replaceBody(Post post, String newBody) throws ForumException {
        LOGGER.debug("Replace body to post {}", post);
        try {
            forumMapper.replaceBody(post, newBody);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't replace body to post {}, exception ", post, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public void rejectPost(Post post) throws ForumException {
        LOGGER.debug("Delete from post history {}", post);
        try {
            forumMapper.deleteHistory(post);
            delete(post);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete from post history {}, exception ", post, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public Post extractBranch(Header newHeader, Post comment) throws ForumException {
        LOGGER.debug("Return comment {} to new message", comment);
        try {
            createHeader(newHeader, comment);
            replaceHeader(comment,newHeader);
            forumMapper.createMessageFromComment(newHeader, comment);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't return comment {} to new message, exception ", comment, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
        return comment;
    }


    @Override
    public Post getPost(int postId) throws ForumException {
        LOGGER.debug("Get post with id {}", postId);
        try {
            return forumMapper.getPost(postId);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get post with id {}, exception ", postId, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public Rate getRate(Post post, User user) throws ForumException{
        LOGGER.debug("Get rate for post {} by user {}", post, user);
        try {
            return forumMapper.getRate(post, user);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't rate for post {} by user {}, exception ", post, user, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public void clearRate(Rate rate) throws ForumException {
        LOGGER.debug("Clear rate {}", rate);
        try {
            forumMapper.clearRate(rate);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't clear rate {}, exception ", rate, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public Rate addRate(Rate rate) throws ForumException {
        LOGGER.debug("Add rate {}", rate);
        try {
            forumMapper.addRate(rate);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't  add rate {}, exception ", rate, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
        return rate;
    }


    @Override
    public void changeRate(Rate rate) throws ForumException {
        LOGGER.debug("Change rate {}", rate);
        try {
            forumMapper.changeRate(rate);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't change rate {}, exception ", rate, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public MessageInfo getMessage(Post message,
                                  boolean allversions,
                                  boolean nocomments,
                                  boolean unpublished,
                                  Order order) throws ForumException {

        LOGGER.debug("Get message {}", message);
        try {
            return forumMapper.getMessage(message, allversions, nocomments, unpublished, order.toString());
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get message {}, exception ", message, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public List<MessageInfo> getForumMessages(Forum forum,
                                              boolean allversions,
                                              boolean nocomments,
                                              boolean unpublished,
                                              Set<String> tags,
                                              Order order,
                                              int offset,
                                              int limit) throws ForumException {
        LOGGER.debug("Get messages from forum {}", forum);
        try {
            return forumMapper.getForumMessages(forum, allversions, nocomments, unpublished, tags, order.toString(), offset, limit);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get messages from forum {}, exception ",forum, ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    private void delete(Post post) {
        forumMapper.deletePost(post);
        if(post.getAncestor() == null) {
            forumMapper.deleteHeader(post);
        }
    }

    private void createHeader(Header header, Post post) {
        forumMapper.createHeader(header, post);
        if (header.getTags() != null) {
            forumMapper.addTags(header.getTags(), header.getId());
        }
    }

    private void replaceHeader(Post post, Header header) {
        forumMapper.replaceHeader(header, post);
        if (post.getComments() != null && post.getComments().size() > 0) {
            for(Post comment : post.getComments()) {
                replaceHeader(comment, header);
            }
        }
    }

}
