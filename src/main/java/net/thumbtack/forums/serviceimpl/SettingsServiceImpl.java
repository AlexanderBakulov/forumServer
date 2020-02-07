package net.thumbtack.forums.serviceimpl;

import net.thumbtack.forums.Config;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.dto.response.SettingsDtoResponse;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.model.User;
import net.thumbtack.forums.model.enums.Privilege;
import net.thumbtack.forums.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService {

    private Config config;
    private UserDao userDao;

    @Autowired
    public SettingsServiceImpl(Config config, UserDao userDao) {
        this.config = config;
        this.userDao = userDao;
    }

    @Override
    public SettingsDtoResponse getForumSettings(Session session) throws ForumException {
        return getSettings(session);
    }


    private SettingsDtoResponse getSettings(Session session) throws ForumException {
        User user = userDao.getUser(session);
        if(session.getToken() == null || user == null) {
            return new SettingsDtoResponse(config.getMaxNameLength(), config.getMinPasswordLength());
        }
        if (user.getPrivilege() == Privilege.SUPER) {
            return new SettingsDtoResponse(config.getMaxNameLength(), config.getMinPasswordLength(),
                    config.getBanTime(), config.getMaxBanCount());
        } else {
            return new SettingsDtoResponse(config.getMaxNameLength(), config.getMinPasswordLength());
        }
    }

}
