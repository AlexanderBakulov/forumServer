package net.thumbtack.forums.api;

import net.thumbtack.forums.dto.response.PostQuantityDtoResponse;
import net.thumbtack.forums.dto.response.PostRatingDtoResponse;
import net.thumbtack.forums.dto.response.UserRatingDtoResponse;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/statistics")
public class StatisticsEndpoint {

    private StatisticsService statisticsService;

    @Autowired
    public StatisticsEndpoint(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }


    @GetMapping(value = "/post-quantity", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostQuantityDtoResponse getPostStatistics(@CookieValue(value = "JAVASESSIONID") String cookie,
                                                     @RequestParam(name = "forumid", required = false, defaultValue = "0") int forumId
                                                        ) throws ForumException {
        return statisticsService.getPostQuantity(new Session(cookie), forumId);
    }


    @GetMapping(value = "/post-ratings", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostRatingDtoResponse> getPostRatings(@CookieValue(value = "JAVASESSIONID") String cookie,
                                                      @RequestParam(name = "forumid", required = false, defaultValue = "0") int forumId,
                                                      @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
                                                      @RequestParam(name = "limit", required = false, defaultValue = "20") int limit
                                                        ) throws ForumException {
        return statisticsService.getPostRatings(new Session(cookie), forumId, offset, limit);
    }


    @GetMapping(value = "/user-ratings", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserRatingDtoResponse> getUserRatings(@CookieValue(value = "JAVASESSIONID") String cookie,
                                                      @RequestParam(name = "forumid", required = false, defaultValue = "0") int forumId,
                                                      @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
                                                      @RequestParam(name = "limit", required = false, defaultValue = "20") int limit
                                                       ) throws ForumException {
        return statisticsService.getUserRatings(new Session(cookie), forumId, offset, limit);
    }



}
