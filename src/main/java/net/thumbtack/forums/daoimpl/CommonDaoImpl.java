package net.thumbtack.forums.daoimpl;


import net.thumbtack.forums.dao.CommonDao;
import net.thumbtack.forums.errors.ForumError;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.mappers.mybatis.CommonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class CommonDaoImpl implements CommonDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);

    private final CommonMapper commonMapper;


    public CommonDaoImpl(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    public void clear() throws ForumException {
        LOGGER.debug("Clear Database");
        try {
            commonMapper.deleteFromRating();
            commonMapper.deleteFromTags();
            commonMapper.deleteFromHistory();
            commonMapper.deleteFromMessage();
            commonMapper.deleteFromHeader();
            commonMapper.deleteFromForum();
            commonMapper.deleteFromSession();
            commonMapper.deleteFromUser();
       } catch (RuntimeException ex) {
            LOGGER.info("Can't clear database");
            throw new ForumException(ForumError.RUNTIME_EXCEPTION, ex.toString());
        }
    }
}
