package net.thumbtack.forums.daoimpl;

import net.thumbtack.forums.dao.StatisticsDao;
import net.thumbtack.forums.errors.ForumError;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.mappers.mybatis.StatisticsMapper;
import net.thumbtack.forums.view.PostQuantityInfo;
import net.thumbtack.forums.view.PostRatingInfo;
import net.thumbtack.forums.view.UserRatingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticsDaoImpl implements StatisticsDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    private StatisticsMapper statisticsMapper;


    @Autowired
    public StatisticsDaoImpl(StatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }


    @Override
    public PostQuantityInfo getPostQuantity(int forumId) throws ForumException {
        LOGGER.debug("Get post statistics");
        try {
            return statisticsMapper.getPostQuantity(forumId);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get post statistics ", ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public List<PostRatingInfo> getPostRatings(int forumId, int offset, int limit) throws ForumException {
        LOGGER.debug("Get post ratings");
        try {
            return statisticsMapper.getPostRatings(forumId, offset, limit);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get post ratings ", ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }


    @Override
    public List<UserRatingInfo> getUserRatings(int forumId, int offset, int limit) throws ForumException {
        LOGGER.debug("Get post statistics");
        try {
            return statisticsMapper.getUserRatings(forumId, offset, limit);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get post statistics ", ex);
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }

}
