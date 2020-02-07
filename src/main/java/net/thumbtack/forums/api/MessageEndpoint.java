package net.thumbtack.forums.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.thumbtack.forums.dto.enums.Order;
import net.thumbtack.forums.dto.request.*;
import net.thumbtack.forums.dto.response.*;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/messages/")
public class MessageEndpoint {

    private ForumService forumService;

    @Autowired
    public MessageEndpoint(ForumService forumService) {
        this.forumService = forumService;
    }


    @PostMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddPostDtoResponse addComment(@CookieValue(value = "JAVASESSIONID") String cookie,
                                         @PathVariable("id") int forumId,
                                         @Valid @RequestBody AddCommentDtoRequest request) throws ForumException {
        return forumService.addComment(request, new Session(cookie), forumId);
    }


    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode deletePost(@CookieValue(value = "JAVASESSIONID") String cookie,
                                  @PathVariable("id") int postId) throws ForumException {
        forumService.deletePost(postId, new Session(cookie));
        return EmptyResponse.respone;
    }


    @PutMapping(value = "/{id}/publish", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode publishPost(@CookieValue(value = "JAVASESSIONID") String cookie,
                                  @PathVariable("id") int postId,
                                  @Valid @RequestBody PublishPostDtoRequest request) throws ForumException {
        forumService.publishPost(request, new Session(cookie), postId);
        return EmptyResponse.respone;
    }


    @PutMapping(value = "/{id}/priority", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode changePriority(@CookieValue(value = "JAVASESSIONID") String cookie,
                                    @PathVariable("id") int postId,
                                    @Valid @RequestBody ChangePriorityDtoRequest request) throws ForumException {
        forumService.changePriority(request, new Session(cookie), postId);
        return EmptyResponse.respone;
    }


    @PostMapping(value = "{id}/rating", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode ratePost(@CookieValue(value = "JAVASESSIONID") String cookie,
                                       @PathVariable("id") int postId,
                                       @Valid @RequestBody RatePostDtoRequest request) throws ForumException {
        forumService.ratePost(request, new Session(cookie), postId);
        return EmptyResponse.respone;
    }


    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChangePostDtoResponse changePost(@CookieValue(value = "JAVASESSIONID") String cookie,
                                            @PathVariable("id") int postId,
                                            @Valid @RequestBody ChangePostDtoRequest request) throws ForumException {
        return forumService.changePost(request, new Session(cookie), postId);
    }


    @PutMapping(value = "/{id}/up", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ExtractBranchDtoResponse extractBranch(@CookieValue(value = "JAVASESSIONID") String cookie,
                                                  @PathVariable("id") int postId,
                                                  @Valid @RequestBody ExtractBranchDtoRequest request) throws ForumException {
        return forumService.extractBranch(request, new Session(cookie), postId);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageInfoDtoResponse getMessage(@CookieValue(value = "JAVASESSIONID") String cookie,
                                             @PathVariable("id") int messageId,
                                             @RequestParam(name = "allversions", required = false, defaultValue = "false") boolean allversions,
                                             @RequestParam(name = "nocomments", required = false, defaultValue = "false") boolean nocomments,
                                             @RequestParam(name = "unpublished", required = false, defaultValue = "false") boolean unpublished,
                                             @RequestParam(name = "order", required = false, defaultValue = "DESC") Order order
                                  ) throws ForumException {
        return forumService.getMessage(new Session(cookie), messageId, allversions, nocomments, unpublished, order);
    }


}
