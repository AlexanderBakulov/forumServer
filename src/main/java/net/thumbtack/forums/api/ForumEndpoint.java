package net.thumbtack.forums.api;


import com.fasterxml.jackson.databind.node.ObjectNode;
import net.thumbtack.forums.dto.enums.Order;
import net.thumbtack.forums.dto.request.CreateForumDtoRequest;
import net.thumbtack.forums.dto.request.CreateMessageDtoRequest;
import net.thumbtack.forums.dto.response.*;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/forums")
public class ForumEndpoint {

    private ForumService forumService;

    @Autowired
    public ForumEndpoint(ForumService forumService) {
        this.forumService = forumService;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateForumDtoResponse createForum(@CookieValue(value = "JAVASESSIONID") String cookie,
                                             @Valid @RequestBody CreateForumDtoRequest request) throws ForumException {
        return forumService.createForum(request, new Session(cookie));
    }


    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode deleteForum(@CookieValue(value = "JAVASESSIONID") String cookie,
                                  @PathVariable("id") int forumId) throws ForumException {
        forumService.deleteForum(forumId, new Session(cookie));
        return EmptyResponse.respone;
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ForumInfoDtoResponse getForumInfo(@CookieValue(value = "JAVASESSIONID") String cookie,
                                             @PathVariable("id") int forumId) throws ForumException {
        return forumService.getForumInfo(forumId, new Session(cookie));
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ForumInfoDtoResponse> getForumsList(@CookieValue(value = "JAVASESSIONID") String cookie) throws ForumException {
        return forumService.getForumsInfoList(new Session(cookie));
    }


    @PostMapping(value = "/{id}/messages", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddPostDtoResponse createMessage(@CookieValue(value = "JAVASESSIONID") String cookie,
                                            @PathVariable("id") int forumId,
                                            @Valid @RequestBody CreateMessageDtoRequest request) throws ForumException {
        return forumService.createMessage(request, new Session(cookie), forumId);
    }


    @GetMapping(value = "/{id}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageInfoDtoResponse> getMessage(@CookieValue(value = "JAVASESSIONID") String cookie,
                                        @PathVariable("id") int forumId,
                                        @RequestParam(name = "allversions", required = false, defaultValue = "false") boolean allversions,
                                        @RequestParam(name = "nocomments", required = false, defaultValue = "false") boolean nocomments,
                                        @RequestParam(name = "unpublished", required = false, defaultValue = "false") boolean unpublished,
                                        @RequestParam(name = "tags", required = false) Set<String> tags,
                                        @RequestParam(name = "order", required = false, defaultValue = "DESC") Order order,
                                        @RequestParam(name = "offset", required = false, defaultValue = "0") @Min(0) int offset,
                                        @RequestParam(name = "limit", required = false, defaultValue = "20") @Min(0) int limit) throws ForumException {

        return forumService.getForumMessages(new Session(cookie), forumId, allversions, nocomments, unpublished, tags, order, offset, limit);
    }

}
