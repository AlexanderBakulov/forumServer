package net.thumbtack.forums.service;

import net.thumbtack.forums.dto.enums.Order;
import net.thumbtack.forums.dto.request.*;
import net.thumbtack.forums.dto.response.*;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;

import java.util.List;
import java.util.Set;

public interface ForumService {

    CreateForumDtoResponse createForum(CreateForumDtoRequest request, Session session) throws ForumException;

    void deleteForum(int forumId, Session session) throws ForumException;

    ForumInfoDtoResponse getForumInfo(int forumId, Session session) throws ForumException;

    List<ForumInfoDtoResponse> getForumsInfoList(Session session) throws ForumException;

    AddPostDtoResponse createMessage(CreateMessageDtoRequest request, Session session, int forumId) throws ForumException;

    AddPostDtoResponse addComment(AddCommentDtoRequest request, Session session, int postId) throws ForumException;

    void deletePost(int postId, Session session) throws ForumException;

    void publishPost(PublishPostDtoRequest request, Session session, int postId) throws ForumException;

    void changePriority(ChangePriorityDtoRequest priority, Session session, int postId) throws ForumException;

    ChangePostDtoResponse changePost(ChangePostDtoRequest request, Session session, int postId) throws ForumException;

    ExtractBranchDtoResponse extractBranch(ExtractBranchDtoRequest request, Session session, int postId) throws ForumException;

    void ratePost(RatePostDtoRequest request, Session session, int postId) throws ForumException;

    MessageInfoDtoResponse getMessage(Session session, int messageId, boolean allversions, boolean nocomments, boolean unpublished, Order order) throws ForumException;

    List<MessageInfoDtoResponse> getForumMessages(Session session, int forumId, boolean allversions, boolean nocomments,
                                                  boolean unpublished, Set<String> tags, Order order, int offset, int limit) throws ForumException;

}
