package net.thumbtack.forums.serviceimpl;

import net.thumbtack.forums.Config;
import net.thumbtack.forums.dao.ForumDao;
import net.thumbtack.forums.dao.StatisticsDao;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.dto.response.PostRatingDtoResponse;
import net.thumbtack.forums.dto.response.PostQuantityDtoResponse;
import net.thumbtack.forums.dto.response.UserRatingDtoResponse;
import net.thumbtack.forums.errors.ForumError;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.mappers.mapstruct.RequestMapper;
import net.thumbtack.forums.mappers.mapstruct.ResponseMapper;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.service.StatisticsService;
import net.thumbtack.forums.view.PostRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsServiceImpl extends ServiceBase implements StatisticsService {

    private StatisticsDao statisticsDao;



    @Autowired
    public StatisticsServiceImpl(UserDao userDao, ForumDao forumDao, Config config,
                                 RequestMapper requestMapper, ResponseMapper responseMapper, StatisticsDao statisticsDao) {
        super(userDao, forumDao, config, requestMapper, responseMapper);
        this.statisticsDao = statisticsDao;
    }



    @Override
    public PostQuantityDtoResponse getPostQuantity(Session session, int forumId) throws ForumException {
        getUser(session);
        checkForum(forumId);
        return responseMapper.postQuantityInfoToDto(statisticsDao.getPostQuantity(forumId));
    }


    @Override
    public List<PostRatingDtoResponse> getPostRatings(Session session, int forumId, int offset, int limit) throws ForumException {
        getUser(session);
        checkForum(forumId);
        return convertPostRatingInfoToDto(statisticsDao.getPostRatings(forumId, offset, limit));
    }


    @Override
    public List<UserRatingDtoResponse> getUserRatings(Session session, int forumId, int offset, int limit) throws ForumException {
        getUser(session);
        checkForum(forumId);
        throw new ForumException(ForumError.NOT_COMPLETE);
    }


    private void checkForum(int forumId) throws ForumException {
        if(forumId > 0) {
            getForum(forumId);
        }
    }

    private List<PostRatingDtoResponse> convertPostRatingInfoToDto(List<PostRatingInfo> infos) {
        List<PostRatingDtoResponse> ratingDtoResponses = new ArrayList<>();
        if(infos != null && !infos.isEmpty()) {
            for (PostRatingInfo info : infos) {
                ratingDtoResponses.add(responseMapper.postRatingInfoToDto(info));
            }
        }
        return ratingDtoResponses;
    }


}
