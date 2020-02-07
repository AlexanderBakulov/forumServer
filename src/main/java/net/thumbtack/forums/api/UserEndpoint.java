package net.thumbtack.forums.api;


import com.fasterxml.jackson.databind.node.ObjectNode;
import net.thumbtack.forums.dto.request.ChangePasswordDtoRequest;
import net.thumbtack.forums.dto.request.CreateUserDtoRequest;
import net.thumbtack.forums.dto.response.CommonUserDtoResponse;
import net.thumbtack.forums.dto.response.UserInfoDtoResponse;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.service.UserService;
import net.thumbtack.forums.dto.response.EmptyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/users")
public class UserEndpoint {

    private UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonUserDtoResponse createUser(@Valid @RequestBody CreateUserDtoRequest request,
                                            HttpServletResponse response) throws ForumException {
        Session session = new Session(UUID.randomUUID().toString());
        response.addCookie(new Cookie("JAVASESSIONID", session.getToken()));
        return userService.createUser(request, session);
    }


    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode quitUser(@CookieValue(value = "JAVASESSIONID") String cookie) throws ForumException {
        userService.quitUser(new Session(cookie));
        return EmptyResponse.respone;
    }


    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonUserDtoResponse changePassword(@Valid @RequestBody ChangePasswordDtoRequest request,
                                                   @CookieValue(value = "JAVASESSIONID") String cookie,
                                                   HttpServletResponse response) throws ForumException {
        Session session = new Session(cookie);
        response.addCookie(new Cookie("JAVASESSIONID", session.getToken()));
        return userService.changePassword(request, session);
    }


    @PutMapping(value = "/{id}/super", produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode makeSuperuser(@CookieValue(value = "JAVASESSIONID") String cookie,
                                    @PathVariable("id") int id) throws ForumException {
        userService.makeSuperuser(id, new Session(cookie));
        return EmptyResponse.respone;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserInfoDtoResponse> getUsersList(@CookieValue(value = "JAVASESSIONID") String cookie) throws ForumException {
        return userService.getUsersList(new Session(cookie));
    }


    @PostMapping(value = "/{id}/restrict", produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode restrictUser(@CookieValue(value = "JAVASESSIONID") String cookie,
                                   @PathVariable("id") int id) throws ForumException {
        userService.restrictUser(id, new Session(cookie));
        return EmptyResponse.respone;
    }

}


