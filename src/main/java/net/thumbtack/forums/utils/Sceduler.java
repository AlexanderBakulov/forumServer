package net.thumbtack.forums.utils;

import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.errors.ForumException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Sceduler {

    private UserDao userDao;

    @Autowired
    public Sceduler(UserDao userDao) {
        this.userDao = userDao;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void unbanUsers() throws ForumException {
        userDao.unbanUsers();
    }



}
