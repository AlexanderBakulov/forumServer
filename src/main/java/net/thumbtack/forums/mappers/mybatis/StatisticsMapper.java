package net.thumbtack.forums.mappers.mybatis;

import net.thumbtack.forums.view.PostRatingInfo;
import net.thumbtack.forums.view.PostQuantityInfo;
import net.thumbtack.forums.view.UserRatingInfo;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface StatisticsMapper {



    @Select({"<script>",
            "<if test='forumId > 0'>  ",
                "SELECT IFNULL(`mg`.`mc`,0) as messageCount, IFNULL(`cg`.`cc`,0) as commentCount " +
                    "FROM forum " +
                    "LEFT JOIN (" +
                    "SELECT count(header.id) as `mc`, forum_id FROM header " +
                    "LEFT JOIN post ON header_id = header.id " +
                    "LEFT JOIN history ON (history.id = (SELECT history.id FROM history WHERE post_id = post.id ORDER BY id DESC LIMIT 1)) " +
                    "WHERE post_id = post.id AND post.ancestor_id IS NULL " +
                    "GROUP BY forum_id " +
                    ") as `mg` ON `mg`.forum_id = forum.id " +
                    "LEFT JOIN (" +
                    "SELECT count(history.id) as `cc`, forum_id FROM header " +
                    "LEFT JOIN post ON header_id = header.id " +
                    "LEFT JOIN history ON (history.id = (SELECT history.id FROM history WHERE post_id = post.id ORDER BY id DESC LIMIT 1)) " +
                    "WHERE post_id = post.id AND post.ancestor_id IS NOT NULL " +
                    "GROUP BY forum_id " +
                    ") as `cg` ON `cg`.forum_id = forum.id",
                    " WHERE forum.id = #{forumId}",
            "</if>",
            "<if test='forumId == 0'>  ",
            "SELECT DISTINCT IFNULL(`mg`.`mc`,0) as messageCount, IFNULL(`cg`.`cc`,0) as commentCount " +
                    "FROM forum " +
                    "LEFT JOIN (" +
                    "SELECT count(header.id) as `mc`, 0 as fix1 FROM header " +
                    "LEFT JOIN post ON header_id = header.id " +
                    "LEFT JOIN history ON (history.id = (SELECT history.id FROM history WHERE post_id = post.id ORDER BY id DESC LIMIT 1)) " +
                    "WHERE post_id = post.id AND post.ancestor_id IS NULL " +
                    "GROUP BY fix1 " +
                    ") as `mg` ON `mg`.fix1 = 0 " +
                    "LEFT JOIN (" +
                    "SELECT count(history.id) as `cc`, 0 as fix2 FROM header " +
                    "LEFT JOIN post ON header_id = header.id " +
                    "LEFT JOIN history ON (history.id = (SELECT history.id FROM history WHERE post_id = post.id ORDER BY id DESC LIMIT 1)) " +
                    "WHERE post_id = post.id AND post.ancestor_id IS NOT NULL " +
                    "GROUP BY fix2 " +
                    ") as `cg` ON `cg`.fix2 = 0",
            "</if>",
            "</script>"
    })
    PostQuantityInfo getPostQuantity(@Param("forumId") int forumId);




    @Select({"<script>",
                "SELECT post.id, IFNULL(`r`.`ar`,0) as rating, IFNULL(`r`.`cr`,0) as rated " +
                        "FROM post " +
                        "JOIN header ON header.id = post.header_id " +
                        "JOIN forum ON forum.id = header.forum_id  " +
                        "LEFT JOIN (" +
                        "SELECT AVG(rate) as `ar`, COUNT(rate) as `cr`, post_id FROM rating " +
                        "JOIN post ON post.id = rating.post_id " +
                        "WHERE post_id = post.id GROUP BY post_id " +
                        ") as `r` ON `r`.post_id IN (post.id )",
            "<if test='forumId > 0'>  WHERE forum.id  = #{forumId} ",
            "</if>",
            " ORDER BY post.id LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<PostRatingInfo> getPostRatings(@Param("forumId") int forumId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);


    //not complete, wrong result
    @Select({"<script>",
                "SELECT user.id, user.name, `u`.`ar` as rating, `u`.`cr` as rated " +
                        "FROM post " +
                        "LEFT JOIN user on user.id = post.user_id " +
                        "JOIN header ON header.id = post.header_id " +
                        "JOIN forum ON forum.id = header.forum_id " +
                        "LEFT JOIN (" +
                        "SELECT AVG(rate) as `ar`, COUNT(rate) as `cr`, post.user_id, user.id FROM rating " +
                        "JOIN post ON post.id = rating.post_id " +
                        "JOIN user ON user.id = post.user_id " +
                        "WHERE post.user_id IN (user.id) " +
                        "GROUP BY user.id " +
                        ") as `u` ON `u`.id IN (user.id) ",
                "<if test='forumId > 0'>  WHERE forum.id  = #{forumId} ",
                "</if>",
                " GROUP BY user.id ORDER BY user.id LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<UserRatingInfo> getUserRatings(@Param("forumId") int forumId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

}
