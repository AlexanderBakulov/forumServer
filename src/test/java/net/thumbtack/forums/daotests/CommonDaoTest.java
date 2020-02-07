package net.thumbtack.forums.daotests;

import net.thumbtack.forums.dao.CommonDao;
import net.thumbtack.forums.dao.ForumDao;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.errors.ForumException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommonDaoTest {

    @Autowired
    CommonDao commonDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ForumDao forumDao;

    @Test
    public void testClear() throws ForumException {
        commonDao.clear();
        assertEquals("admin", userDao.getUser(1).getName());
        assertEquals(1, userDao.getUsers().size());
        assertEquals(0, forumDao.getForumsInfoList().size());

    }

}
