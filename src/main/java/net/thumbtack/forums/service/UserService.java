package net.thumbtack.forums.service;


import net.thumbtack.forums.dto.request.ChangePasswordDtoRequest;
import net.thumbtack.forums.dto.request.CreateUserDtoRequest;
import net.thumbtack.forums.dto.request.LoginUserDtoRequest;
import net.thumbtack.forums.dto.response.CommonUserDtoResponse;
import net.thumbtack.forums.dto.response.UserInfoDtoResponse;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;

import java.util.List;


public interface UserService {

    CommonUserDtoResponse createUser(CreateUserDtoRequest request, Session session) throws ForumException;

    CommonUserDtoResponse loginUser(LoginUserDtoRequest request, Session session) throws ForumException;

    void logoutUser(Session session) throws ForumException;

    void quitUser(Session session) throws ForumException;

    CommonUserDtoResponse changePassword(ChangePasswordDtoRequest request, Session session) throws ForumException;

    void makeSuperuser(int userId, Session session) throws ForumException;

    void restrictUser(int userId, Session session) throws ForumException;

    List<UserInfoDtoResponse> getUsersList(Session session) throws ForumException;


}
