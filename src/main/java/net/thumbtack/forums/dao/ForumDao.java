package net.thumbtack.forums.dao;

import net.thumbtack.forums.dto.enums.Order;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.*;
import net.thumbtack.forums.model.enums.PostPriority;
import net.thumbtack.forums.view.ForumInfo;
import net.thumbtack.forums.view.MessageInfo;

import java.util.List;
import java.util.Set;

public interface ForumDao {

    Forum createForum(Forum forum) throws ForumException;

    void deleteForum(Forum forum) throws ForumException;

    ForumInfo getForumInfo(Forum forum) throws ForumException;

    List<ForumInfo> getForumsInfoList() throws ForumException;

    Forum getForum(int forumId) throws ForumException;

    Post createMessage(Header header, Post message) throws ForumException;

    void deletePost(Post post) throws ForumException;

    void publishPost(Post post) throws ForumException;

    void changePriority(PostPriority priority, Header header) throws ForumException;

    void addHistory(Post post) throws ForumException;

    void replaceBody(Post post, String newBody) throws ForumException;

    void rejectPost(Post post) throws ForumException;

    Post extractBranch(Header header, Post comment) throws ForumException;

    Post getPost(int id) throws ForumException;

    Post addComment(Post comment) throws ForumException;

    void switchForumsToReadOnly(User user) throws ForumException;

    Rate getRate(Post post, User user) throws ForumException;

    void clearRate(Rate rate) throws ForumException;

    Rate addRate(Rate rate) throws ForumException;

    void changeRate(Rate rate) throws ForumException;

    MessageInfo getMessage(Post message, boolean allversions, boolean nocomments, boolean unpublished, Order order) throws ForumException;

    List<MessageInfo> getForumMessages(Forum forum, boolean allversions, boolean nocomments, boolean unpublished, Set<String> tags, Order order, int offset, int limit) throws ForumException;
}
