package net.thumbtack.forums.servicetests;


import net.thumbtack.forums.dao.ForumDao;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.service.DebugService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DebugServiceTest {

    @Autowired
    DebugService debugService;

    @Autowired
    UserDao userDao;

    @Autowired
    ForumDao forumDao;


    @Test
    public void testClear() throws ForumException {
        debugService.clear();
        assertEquals("admin", userDao.getUser(1).getName());
        assertEquals(1, userDao.getUsers().size());
        assertEquals(0, forumDao.getForumsInfoList().size());
    }
}
