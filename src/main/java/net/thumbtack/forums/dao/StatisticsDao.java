package net.thumbtack.forums.dao;

import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.view.PostQuantityInfo;
import net.thumbtack.forums.view.PostRatingInfo;
import net.thumbtack.forums.view.UserRatingInfo;

import java.util.List;

public interface StatisticsDao {

    PostQuantityInfo getPostQuantity(int forumId) throws ForumException;

    List<PostRatingInfo> getPostRatings(int forumId, int offset, int limit) throws ForumException;

    List<UserRatingInfo> getUserRatings(int forumId, int offset, int limit) throws ForumException;

}
