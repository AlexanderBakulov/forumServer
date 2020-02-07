package net.thumbtack.forums.service;

import net.thumbtack.forums.dto.response.PostQuantityDtoResponse;
import net.thumbtack.forums.dto.response.PostRatingDtoResponse;
import net.thumbtack.forums.dto.response.UserRatingDtoResponse;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;

import java.util.List;

public interface StatisticsService {


    PostQuantityDtoResponse getPostQuantity(Session session, int forumId) throws ForumException;

    List<PostRatingDtoResponse> getPostRatings(Session session, int forumId, int offset, int limit) throws ForumException;

    List<UserRatingDtoResponse> getUserRatings(Session session, int forumId, int offset, int limit) throws ForumException;
}
