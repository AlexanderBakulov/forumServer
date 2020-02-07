package net.thumbtack.forums.serviceimpl;


import net.thumbtack.forums.dao.CommonDao;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.service.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebugServiceImpl implements DebugService {

    private CommonDao commonDao;

    @Autowired
    public DebugServiceImpl(CommonDao commonDao) {
        this.commonDao = commonDao;
    }

    @Override
    public void clear() throws ForumException {
        commonDao.clear();
    }
}
