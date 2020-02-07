package net.thumbtack.forums.serviceimpl;

import net.thumbtack.forums.Config;
import net.thumbtack.forums.dao.*;
import net.thumbtack.forums.errors.ForumError;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.mappers.mapstruct.RequestMapper;
import net.thumbtack.forums.mappers.mapstruct.ResponseMapper;
import net.thumbtack.forums.model.*;
import net.thumbtack.forums.model.enums.BanStatus;
import net.thumbtack.forums.model.enums.PostPriority;
import net.thumbtack.forums.model.enums.PostState;
import net.thumbtack.forums.model.enums.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class ServiceBase {

    protected UserDao userDao;
    protected ForumDao forumDao;
    protected Config config;
    protected RequestMapper requestMapper;
    protected ResponseMapper responseMapper;



    @Autowired
    public ServiceBase(UserDao userDao, ForumDao forumDao, Config config,
                       RequestMapper requestMapper, ResponseMapper responseMapper) {
        this.userDao = userDao;
        this.forumDao = forumDao;
        this.config = config;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }



    protected User getUser(Session session) throws ForumException {
        User user = userDao.getUser(session);
        if(user == null) {
            throw new ForumException(ForumError.USER_NOT_FOUND, "JAVASESSIONID");
        }
        return user;
    }


    protected User getUser(int userId) throws ForumException {
        User user = userDao.getUser(userId);
        if (user == null) {
            throw new ForumException(ForumError.USER_NOT_FOUND, "userId");
        }
        return user;
    }


    protected User getUser(String name) throws ForumException {
        User user = userDao.getUser(name);
        if(user == null) {
            throw new ForumException(ForumError.USER_NOT_FOUND, "name");
        }
        return user;
    }


    protected void checkPassword(User user, String password, String errorField) throws ForumException {
        if(!user.getPassword().equals(password)) {
            throw new ForumException(ForumError.INCORRECT_PASSWORD, errorField);
        }
    }


    protected void checkBan(User user) throws ForumException {
        if (user.getBanStatus() == BanStatus.LIMITED) {
            throw new ForumException(ForumError.USER_IS_RESTRICTED);
        }
    }


    protected void checkPermanentBan(User user) throws ForumException {
        if (user.getBanStatus() == BanStatus.LIMITED && user.getTimeBanExit().getYear() == 9999) {
            throw new ForumException(ForumError.PERMANENT_BAN);
        }
    }

    protected void checkSuper(User user) throws ForumException {
        if(user.getPrivilege() != Privilege.SUPER) {
            throw new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "privilege");
        }
    }


    protected void checkNotSuper(User user) throws ForumException {
        if(user.getPrivilege() == Privilege.SUPER) {
            throw new ForumException(ForumError.USER_IS_SUPER, "privilege");
        }
    }


    protected Forum getForum(int forumId) throws ForumException {
        Forum forum = forumDao.getForum(forumId);
        if(forum == null) {
            throw new ForumException(ForumError.INCORRECT_FORUM_ID, "forumId");
        }
        return forum;
    }


    protected Post getPost(int id) throws ForumException {
        Post post = forumDao.getPost(id);
        if(post == null) {
            throw new ForumException(ForumError.INCORRECT_POST_ID, "id");
        }
        return post;
    }


    protected void checkPostAuthor(boolean isAuthor) throws ForumException {
        if(!isAuthor) {
            throw new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "JAVASESSIONID");
        }
    }


    protected boolean isAuthor(User user, Post post) {
        return post.getUser().equals(user);
    }


    protected void checkPostHasComments(Post post) throws ForumException {
        if(post.getComments() != null && post.getComments().size() > 0) {
            throw new ForumException(ForumError.POST_HAS_COMMENTS, "postId");
        }
    }


    protected void checkForumOwner(User user, Forum forum) throws ForumException {
        if(!forum.getUser().equals(user)) {
            throw new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS, "JAVASESSIONID");
        }
    }


    protected void checkAncestorState(Post post) throws ForumException {
        if(post.getHistory().get(post.getHistory().size() - 1).getPostState() == PostState.UNPUBLISHED) {
            throw new ForumException(ForumError.ANCESTOR_IS_UNPUBLISHED, "postId");
        }
    }


    protected void checkIsNotPublished(Post post) throws ForumException {
        if(isPublished(post)) {
            throw new ForumException(ForumError.POST_ALREADY_PUBLISHED, "postId");
        }
    }

    protected void checkIsPublished(Post post) throws ForumException {
        if(!isPublished(post)) {
            throw new ForumException(ForumError.POST_IS_NOT_PUBLISHED, "postId");
        }
    }


    protected boolean isPublished(Post post) {
        if(post.getHistory() != null && post.getHistory().size() != 0) {
            return (post.getHistory().get(post.getHistory().size() - 1).getPostState() == PostState.PUBLISHED);
        }
        return false;
    }


    protected void checkIsMessage(Post post) throws ForumException {
        if (post.getAncestor() != null) {
            throw new ForumException(ForumError.POST_IS_NOT_MESSAGE, "postId");
        }
    }

    protected void checkIsNotMessage(Post post) throws ForumException {
        if (post.getAncestor() == null) {
            throw new ForumException(ForumError.POST_IS_MESSAGE, "postId");
        }
    }


    protected void checkCurrentPriority(PostPriority priority, Header header) throws ForumException {
        if(header.getPriority() == priority) {
            throw new ForumException(ForumError.SAME_PRIORITY, "priority");
        }
    }

    protected void checkForumStatus(Forum forum) throws ForumException {
        if(forum.getReadOnly()) {
            throw new ForumException(ForumError.READ_ONLY_FORUM, "forumId");
        }
    }

}
