package net.thumbtack.forums.serviceimpl;

import net.thumbtack.forums.Config;
import net.thumbtack.forums.dao.ForumDao;
import net.thumbtack.forums.dao.UserDao;
import net.thumbtack.forums.dto.request.ChangePasswordDtoRequest;
import net.thumbtack.forums.dto.request.CreateUserDtoRequest;
import net.thumbtack.forums.dto.response.CommonUserDtoResponse;
import net.thumbtack.forums.dto.request.LoginUserDtoRequest;
import net.thumbtack.forums.dto.response.UserInfoDtoResponse;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.mappers.mapstruct.RequestMapper;
import net.thumbtack.forums.mappers.mapstruct.ResponseMapper;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.model.User;
import net.thumbtack.forums.model.enums.BanStatus;
import net.thumbtack.forums.model.enums.Privilege;
import net.thumbtack.forums.service.UserService;
import net.thumbtack.forums.view.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Service
public class UserServiceImpl extends ServiceBase implements UserService {



    @Autowired
    public UserServiceImpl(UserDao userDao, ForumDao forumDao, Config config,
                           RequestMapper requestMapper, ResponseMapper responseMapper) {
        super(userDao, forumDao, config, requestMapper, responseMapper);
    }



    @Override
    public CommonUserDtoResponse createUser(CreateUserDtoRequest request, Session session) throws ForumException {
        User user = requestMapper.createUserDtoRequestToUser(request);
        userDao.createUser(user, session);
        return new CommonUserDtoResponse(user.getId(), user.getName(), user.getEmail());
    }


    @Override
    public CommonUserDtoResponse loginUser(LoginUserDtoRequest request, Session session) throws ForumException {
        User user = getUser(request.getName());
        checkPassword(user, request.getPassword(), "password");
        session.setUser(user);
        userDao.loginUser(session);
        return new CommonUserDtoResponse(user.getId(), user.getName(), user.getEmail());
    }


    @Override
    public void logoutUser(Session session) throws ForumException {
        User user = getUser(session);
        session.setUser(user);
        userDao.logoutUser(session);
    }


    @Override
    public void quitUser(Session session) throws ForumException {
        User user = getUser(session);
        userDao.quitUser(session);
        forumDao.switchForumsToReadOnly(user);
    }


    @Override
    public CommonUserDtoResponse changePassword(ChangePasswordDtoRequest request, Session session) throws ForumException {
        User user = getUser(session);
        checkPassword(user, request.getOldPassword(), "oldPassword");
        userDao.changePassword(user, request.getNewPassword());
        return new CommonUserDtoResponse(user.getId(), user.getName(), user.getEmail());
    }


    @Override
    public void makeSuperuser(int userId, Session session) throws ForumException {
        User superUser = getUser(session);
        checkSuper(superUser);
        User user = getUser(userId);
        checkNotSuper(user);
        userDao.makeSuperuser(user);
    }


    @Override
    public void restrictUser(int userId, Session session) throws ForumException {
        User superUser = getUser(session);
        checkSuper(superUser);
        User user = getUser(userId);
        checkNotSuper(user);
        checkBan(user);
        restrictUser(user);
    }


    @Override
    public List<UserInfoDtoResponse> getUsersList(Session session) throws ForumException {
        User user = getUser(session);
        return getUsersList(user);
    }



    private void restrictUser(User user) throws ForumException {
        user.setBanCount(user.getBanCount() + 1);
        if(user.getBanCount() < config.getMaxBanCount()) {
            user.setTimeBanExit(LocalDateTime.now().plusDays(3).withHour(0).withMinute(0).withSecond(0));
        } else {
            user.setTimeBanExit(LocalDateTime.of(9999, 1,1,0,0,0));
            forumDao.switchForumsToReadOnly(user);
        }
        user.setBanStatus(BanStatus.LIMITED);
        userDao.restrictUser(user);
    }


    private List<UserInfoDtoResponse> getUsersList(User user) throws ForumException {
        List<UserInfoDtoResponse> responseList = new ArrayList<>();
        for(UserInfo info : userDao.getUsers()) {
            UserInfoDtoResponse userInfo = new UserInfoDtoResponse(info.getId(), info.getName(), info.getTimeRegistered(),
                    info.isOnline(), info.isDeleted(), info.getBanStatus(), info.getBanCount());
            if(user.getPrivilege() == Privilege.SUPER) {
                userInfo.setEmail(info.getEmail());
                userInfo.setSuperUser(info.getPrivilege() == Privilege.SUPER);
            }
            responseList.add(userInfo);
        }
        return responseList;
    }

}
