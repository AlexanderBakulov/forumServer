package net.thumbtack.forums.serviceimpl;

import net.thumbtack.forums.Config;
import net.thumbtack.forums.dao.ForumDao;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.daoimpl.UserDaoImpl;
import net.thumbtack.forums.dto.enums.Decision;
import net.thumbtack.forums.dto.enums.Order;
import net.thumbtack.forums.dto.request.*;
import net.thumbtack.forums.dto.response.*;
import net.thumbtack.forums.errors.ForumError;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.mappers.mapstruct.RequestMapper;
import net.thumbtack.forums.mappers.mapstruct.ResponseMapper;
import net.thumbtack.forums.model.*;
import net.thumbtack.forums.model.enums.*;
import net.thumbtack.forums.service.ForumService;
import net.thumbtack.forums.view.CommentInfo;
import net.thumbtack.forums.view.ForumInfo;
import net.thumbtack.forums.view.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class ForumServiceImpl extends ServiceBase implements ForumService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);



    @Autowired
    public ForumServiceImpl(UserDao userDao, ForumDao forumDao, Config config,
                            RequestMapper requestMapper, ResponseMapper responseMapper) {
        super(userDao, forumDao, config, requestMapper, responseMapper);
    }


    @Override
    public CreateForumDtoResponse createForum(CreateForumDtoRequest request, Session session) throws ForumException {
        User user = getUser(session);
        checkBan(user);
        Forum forum = new Forum(request.getName(), request.getType(), user);
        forumDao.createForum(forum);
        return new CreateForumDtoResponse(forum.getId(), forum.getName(), forum.getModerateStatus());
    }


    @Override
    public void deleteForum(int forumId, Session session) throws ForumException {
        User user = getUser(session);
        checkPermanentBan(user);
        Forum forum = getForum(forumId);
        deleteForum(user, forum);
    }


    @Override
    public ForumInfoDtoResponse getForumInfo(int forumId, Session session) throws ForumException {
        getUser(session);
        ForumInfo forumInfo = forumDao.getForumInfo(getForum(forumId));
        return responseMapper.forumInfoToDtoResponse(forumInfo);
    }


    @Override
    public List<ForumInfoDtoResponse> getForumsInfoList(Session session) throws ForumException {
        getUser(session);
        return convertforumsInfoListToDto(forumDao.getForumsInfoList());
    }


    @Override
    public AddPostDtoResponse createMessage(CreateMessageDtoRequest request, Session session, int forumId) throws ForumException {
        User user = getUser(session);
        checkBan(user);
        Forum forum = getForum(forumId);
        checkForumStatus(forum);
        Post message = createMessage(request, user, forum);
        return new AddPostDtoResponse(message.getId(), message.getHistory().get(0).getPostState());
    }


    @Override
    public AddPostDtoResponse addComment(AddCommentDtoRequest request, Session session, int postId) throws ForumException {
        User user = getUser(session);
        checkBan(user);
        Post post = getPost(postId);
        checkAncestorState(post);
        Forum forum = post.getHeader().getForum();
        checkForumStatus(forum);
        Post comment = addComment(request, user, post);
        return new AddPostDtoResponse(comment.getId(), comment.getHistory().get(0).getPostState());
    }


    @Override
    public void deletePost(int postId, Session session) throws ForumException {
        User user = getUser(session);
        checkPermanentBan(user);
        Post post = getPost(postId);
        checkPostAuthor(isAuthor(user, post));
        checkPostHasComments(post);
        forumDao.deletePost(post);
    }


    @Override
    public void publishPost(PublishPostDtoRequest request, Session session, int postId) throws ForumException {
        User user = getUser(session);
        checkPermanentBan(user);
        Post post = getPost(postId);
        Forum forum = getForum(post.getHeader().getForum().getId());
        checkForumOwner(user, forum);
        checkIsNotPublished(post);
        publishPost(request.getDecision(), post);
    }


    @Override
    public void changePriority(ChangePriorityDtoRequest request, Session session, int postId) throws ForumException {
        User user = getUser(session);
        checkBan(user);
        Post message = getPost(postId);
        checkPostAuthor(isAuthor(user, message));
        checkIsMessage(message);
        checkCurrentPriority(request.getPriority(), message.getHeader());
        forumDao.changePriority(request.getPriority(), message.getHeader());
    }


    @Override
    public ChangePostDtoResponse changePost(ChangePostDtoRequest request, Session session, int postId) throws ForumException{
        User user = getUser(session);
        checkBan(user);
        Post post = getPost(postId);
        checkPostAuthor(isAuthor(user, post));
        changePost(user, post, request.getBody());
        return new ChangePostDtoResponse(post.getHistory().get(post.getHistory().size() - 1).getPostState());
    }


    @Override
    public ExtractBranchDtoResponse extractBranch(ExtractBranchDtoRequest request, Session session, int postId) throws ForumException {
        User user = getUser(session);
        checkBan(user);
        Post comment = getPost(postId);
        checkIsPublished(comment);
        Forum forum = comment.getHeader().getForum();
        checkForumOwner(user, forum);
        checkIsNotMessage(comment);
        Header newHeader = new Header(forum, request.getSubject(), request.getPriority(), request.getTags());
        return new ExtractBranchDtoResponse(forumDao.extractBranch(newHeader, comment).getId());
    }


    @Override
    public void ratePost(RatePostDtoRequest request, Session session, int postId) throws ForumException {
        User user = getUser(session);
        checkPermanentBan(user);
        Post post = getPost(postId);
        checkPostAuthor(!isAuthor(user, post));
        ratePost(request.getValue(), post, user);
    }


    @Override
    public MessageInfoDtoResponse getMessage(Session session, int messageId,
                                             boolean allversions,
                                             boolean nocomments,
                                             boolean unpublished,
                                             Order order) throws ForumException {
        getUser(session);
        Post message = getPost(messageId);
        checkIsMessage(message);
        MessageInfo messageInfo = forumDao.getMessage(message, allversions, nocomments, unpublished, order);
        return convertToDto(messageInfo);
    }


    @Override
    public List<MessageInfoDtoResponse> getForumMessages(Session session, int forumId,
                                                         boolean allversions,
                                                         boolean nocomments,
                                                         boolean unpublished,
                                                         Set<String> tags,
                                                         Order order,
                                                         int offset,
                                                         int limit) throws ForumException {
        getUser(session);
        Forum forum = getForum(forumId);
        List<MessageInfo> responses = forumDao.getForumMessages(forum, allversions, nocomments, unpublished, tags, order, offset, limit);
        return convertToDto(responses);
    }


    private void deleteForum(User user, Forum forum) throws ForumException {
        if((user.getPrivilege() == Privilege.SUPER) || (user.equals(forum.getUser()))) {
            forumDao.deleteForum(forum);
        } else {
            throw new ForumException(ForumError.USER_HAVE_NO_PERMISSIONS);
        }
    }


    private List<ForumInfoDtoResponse> convertforumsInfoListToDto(List<ForumInfo> forumInfos) {
        List<ForumInfoDtoResponse> infosDto = new ArrayList<>();
        for(ForumInfo info : forumInfos) {
            infosDto.add(responseMapper.forumInfoToDtoResponse(info));
        }
    return infosDto;
    }



    private Post createMessage(CreateMessageDtoRequest request, User user, Forum forum) throws ForumException {
        Header header = new Header(forum, request.getSubject(), request.getPriority(), request.getTags());
        List<History> history = new ArrayList<>();
        history.add(new History(request.getBody(), PostState.UNPUBLISHED));
        Post message = new Post(user, header, history);
        setPostState(forum, user, message);
        forumDao.createMessage(header, message);
        return message;
    }


    private Post addComment(AddCommentDtoRequest request, User user, Post message) throws ForumException {
        Post comment = new Post(user, message.getHeader(), message);
        comment.getHistory().add(new History(request.getBody()));
        setPostState(message.getHeader().getForum(), user, comment);
        forumDao.addComment(comment);
        return comment;
    }


    private void setPostState(Forum forum, User user, Post message) {
        if(forum.getModerateStatus() == ModerateStatus.UNMODERATED || forum.getUser().equals(user)) {
            message.getHistory().get(message.getHistory().size() - 1).setPostState(PostState.PUBLISHED);
        }
    }


    private void publishPost(Decision decision, Post post) throws ForumException {
        if(decision == Decision.YES) {
            forumDao.publishPost(post);
        } else {
            forumDao.rejectPost(post);
            LOGGER.info("Send message to user with email {}: post {} - publication denied", post.getUser().getEmail(), post.getId());
        }
    }


    private void changePost(User user, Post post, String newBody) throws ForumException {
        if(post.getHistory().get(post.getHistory().size() - 1).getPostState() == PostState.UNPUBLISHED) {
            forumDao.replaceBody(post, newBody);
        } else {
            post.getHistory().add(new History(newBody));
            setPostState(post.getHeader().getForum(), user, post);
            forumDao.addHistory(post);
        }
    }


    private void ratePost(Integer value, Post post, User user) throws ForumException {
        Rate rate = forumDao.getRate(post, user);
        if(rate == null && value == null) {
            throw new ForumException(ForumError.EMPTY_RATE, "value");
        } else if(value == null) {
            forumDao.clearRate(rate);
        } else if(rate == null) {
            forumDao.addRate(new Rate(value, user, post));
        } else {
            rate.setRate(value);
            forumDao.changeRate(rate);
        }
    }


    //this method need to avoid connection between MessageInfoDtoResponse and myBatis
    private List<MessageInfoDtoResponse> convertToDto(List<MessageInfo> infos) {
        List<MessageInfoDtoResponse> infoDtos = new ArrayList<>();
        if(infos != null && !infos.isEmpty()) {
            for(MessageInfo info : infos) {
                infoDtos.add(convertToDto(info));
            }
        }
        return infoDtos;
    }


    //this method need to avoid connection between MessageInfoDtoResponse and myBatis
    private MessageInfoDtoResponse convertToDto(MessageInfo messageInfo) {
        if(messageInfo.getComments() == null || messageInfo.getComments().isEmpty()) {
            return responseMapper.messageInfoToDtoResponse(messageInfo);
        } else {
            List<CommentInfoDtoResponse> commentsDto = new ArrayList<>();
            for (CommentInfo commentInfo : messageInfo.getComments()) {
                commentsDto.add(convertCommentInfoToDto(commentInfo));
            }
            return new MessageInfoDtoResponse(messageInfo.getId(), messageInfo.getCreator(),
                    messageInfo.getSubject(), messageInfo.getBody(), messageInfo.getPriority(), messageInfo.getTags(),
                    messageInfo.getCreated(), messageInfo.getRating(), messageInfo.getRated(), commentsDto);
        }
    }

    private CommentInfoDtoResponse convertCommentInfoToDto(CommentInfo commentInfo) {
        if(commentInfo.getComments() == null || commentInfo.getComments().isEmpty()) {
            return new CommentInfoDtoResponse(commentInfo.getId(), commentInfo.getCreator(), commentInfo.getBody(),
                    commentInfo.getCreated(), commentInfo.getRating(), commentInfo.getRated(), null);
        } else {
            List<CommentInfoDtoResponse> commentInfoDtos = new ArrayList<>();
            for(CommentInfo info : commentInfo.getComments()) {
                commentInfoDtos.add(convertCommentInfoToDto(info));
            }
            return new CommentInfoDtoResponse(commentInfo.getId(), commentInfo.getCreator(), commentInfo.getBody(),
                    commentInfo.getCreated(), commentInfo.getRating(), commentInfo.getRated(), commentInfoDtos);
        }
    }

}
