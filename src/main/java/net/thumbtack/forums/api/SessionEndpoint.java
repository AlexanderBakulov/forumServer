package net.thumbtack.forums.api;


import com.fasterxml.jackson.databind.node.ObjectNode;
import net.thumbtack.forums.dto.request.LoginUserDtoRequest;
import net.thumbtack.forums.dto.response.CommonUserDtoResponse;
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
import java.util.UUID;

@RestController
@RequestMapping("api/sessions")
public class SessionEndpoint {

    private UserService userService;

    @Autowired
    public SessionEndpoint(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonUserDtoResponse loginUser(@Valid @RequestBody LoginUserDtoRequest request,
                                           HttpServletResponse response) throws ForumException {
        Session session = new Session(UUID.randomUUID().toString());
        response.addCookie(new Cookie("JAVASESSIONID", session.getToken()));
        return userService.loginUser(request, session);
    }


    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode logoutUser(@CookieValue(value = "JAVASESSIONID") String cookie) throws ForumException {
        userService.logoutUser(new Session(cookie));
        return EmptyResponse.respone;
    }

}
