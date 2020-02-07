package net.thumbtack.forums.mappers.mybatis;

import net.thumbtack.forums.model.*;
import net.thumbtack.forums.model.enums.PostPriority;
import net.thumbtack.forums.view.CommentInfo;
import net.thumbtack.forums.view.ForumInfo;
import net.thumbtack.forums.view.MessageInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Set;

@Mapper
public interface ForumMapper {


    @Select("SELECT id, name, moderate as moderateStatus, read_only as readOnly, user_id " +
            "FROM forum " +
            "WHERE forum.id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.UserMapper.getUserById",
                            fetchType = FetchType.LAZY))
    })
    Forum getForum(int id);


    @Select("SELECT id, publish_date as publishDate, user_id, header_id, ancestor_id " +
            "FROM post " +
            "WHERE id = #{id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.UserMapper.getUserById",
                            fetchType = FetchType.LAZY)),
            @Result(property = "history", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getHistory",
                            fetchType = FetchType.LAZY)),
            @Result(property = "comments", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getComments",
                            fetchType = FetchType.LAZY)),
            @Result(property = "rates", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getRates",
                            fetchType = FetchType.LAZY)),
            @Result(property = "header", column = "header_id", javaType = Header.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getHeader",
                            fetchType = FetchType.LAZY)),
            @Result(property = "ancestor", column = "ancestor_id", javaType = Post.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getAncestor",
                            fetchType = FetchType.LAZY))
    })
    Post getPost(int id);


    @Select("SELECT id, post_body, published FROM history WHERE post_id = #{postId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "postBody", column = "post_body"),
            @Result(property = "postState", column = "published")
    })
    List<History> getHistory(int postId);


    @Select("SELECT id, publish_date as publishDate, user_id, header_id, ancestor_id " +
            "FROM post " +
            "WHERE ancestor_id = #{postId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.UserMapper.getUserById",
                            fetchType = FetchType.LAZY)),
            @Result(property = "history", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getHistory",
                            fetchType = FetchType.LAZY)),
            @Result(property = "comments", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getComments",
                            fetchType = FetchType.LAZY)),
            @Result(property = "rates", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getRates",
                            fetchType = FetchType.LAZY)),
            @Result(property = "header", column = "header_id", javaType = Header.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getHeader",
                            fetchType = FetchType.LAZY)),
            @Result(property = "ancestor", column = "ancestor_id", javaType = Post.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getAncestor",
                            fetchType = FetchType.LAZY))
    })
    List<Post> getComments(int postId);


    @Select("SELECT id, rate, user_id, post_id FROM rating WHERE post_id = #{postId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.UserMapper.getUserById",
                            fetchType = FetchType.LAZY)),
            @Result(property = "post", column = "post_id", javaType = Post.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getPostPartial",
                            fetchType = FetchType.LAZY))
    })
    List<Rate> getRates(int postId);


    @Select("SELECT id, subject, priority, forum_id FROM header WHERE id = #{headerId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "forum", column = "forum_id", javaType = Forum.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getForum",
                            fetchType = FetchType.LAZY)),
            @Result(property = "tags", column = "id", javaType = Set.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getTags",
                            fetchType = FetchType.LAZY))
    })
    Header getHeader(int headerId);


    @Select("SELECT id, publish_date as publishDate, user_id, header_id " +
            "FROM post " +
            "WHERE id = #{id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.UserMapper.getUserById",
                            fetchType = FetchType.LAZY)),
            @Result(property = "history", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getHistory",
                            fetchType = FetchType.LAZY)),
            @Result(property = "header", column = "header_id", javaType = Header.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getHeader",
                            fetchType = FetchType.LAZY))
    })
    Post getAncestor(int id);


    @Select("SELECT tag FROM tags WHERE header_id = #{headerId}")
    Set<Tag> getTags(int headerId);


    @Select("SELECT forum.id, forum.name, moderate as type, read_only as readonly, forum.user_id, " +
            "user.name as creator, IFNULL(`mg`.`mc`,0) as messageCount, IFNULL(`cg`.`cc`,0) as commentCount " +
            "FROM forum " +
            "LEFT JOIN user ON user.id = forum.user_id " +
            "LEFT JOIN (" +
            "SELECT count(header.id) as `mc`, forum_id FROM header " +
            "LEFT JOIN post ON header_id = header.id " +
            "LEFT JOIN history ON (history.id = (SELECT MAX(history.id) from history WHERE post_id = post.id) AND published = 'PUBLISHED') " +
            "WHERE post_id = post.id AND post.ancestor_id IS NULL " +
            "GROUP BY forum_id " +
            ") as `mg` ON `mg`.forum_id = forum.id  " +
            "LEFT JOIN (" +
            "SELECT count(history.id) as `cc`, forum_id FROM header " +
            "LEFT JOIN post ON header_id = header.id " +
            "LEFT JOIN history ON (history.id = (SELECT MAX(history.id) FROM history WHERE post_id = post.id) AND published = 'PUBLISHED') " +
            "WHERE post_id = post.id AND post.ancestor_id IS NOT NULL " +
            "GROUP BY forum_id " +
            ") as `cg` ON `cg`.forum_id = forum.id " +
            "WHERE forum.id = #{f.id}")
    ForumInfo getForumInfo(@Param("f") Forum forum);


    @Select("SELECT forum.id, forum.name, moderate as type, read_only as readonly, forum.user_id, " +
            "user.name as creator, IFNULL(`mg`.`mc`,0) as messageCount, IFNULL(`cg`.`cc`,0) as commentCount " +
            "FROM forum " +
            "LEFT JOIN user ON user.id = forum.user_id " +
            "LEFT JOIN (" +
            "SELECT count(header.id) as `mc`, forum_id FROM header " +
            "LEFT JOIN post ON header_id = header.id " +
            "LEFT JOIN history ON (history.id = (SELECT MAX(history.id) FROM history WHERE post_id = post.id) AND published = 'PUBLISHED') " +
            "WHERE post_id = post.id AND post.ancestor_id IS NULL " +
            "GROUP BY forum_id " +
            ") as `mg` ON `mg`.forum_id = forum.id  " +
            "LEFT JOIN (" +
            "SELECT count(history.id) as `cc`, forum_id FROM header " +
            "LEFT JOIN post ON header_id = header.id " +
            "LEFT JOIN history ON (history.id = (SELECT MAX(history.id) FROM history WHERE post_id = post.id) AND published = 'PUBLISHED') " +
            "WHERE post_id = post.id AND post.ancestor_id IS NOT NULL " +
            "GROUP BY forum_id " +
            ") as `cg` ON `cg`.forum_id = forum.id ")
    List<ForumInfo> getForumsInfoList();


    @Select("SELECT id, rate, post_id, user_id FROM rating WHERE post_id = #{p.id} AND user_id = #{u.id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "post", column = "post_id", javaType = Post.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getPostPartial",
                            fetchType = FetchType.LAZY)),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.UserMapper.getUserById",
                            fetchType = FetchType.LAZY))
    })
    Rate getRate(@Param("p") Post post, @Param("u") User user);


    @Select("SELECT id, publish_date as publishDate, user_id, header_id, ancestor_id " +
            "FROM post " +
            "WHERE id = #{id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.UserMapper.getUserById",
                            fetchType = FetchType.LAZY)),
            @Result(property = "history", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getHistory",
                            fetchType = FetchType.LAZY)),
            @Result(property = "header", column = "header_id", javaType = Header.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getHeader",
                            fetchType = FetchType.LAZY)),
            @Result(property = "ancestor", column = "ancestor_id", javaType = Post.class,
                    one = @One(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getAncestor",
                            fetchType = FetchType.LAZY))
    })
    Post getPostPartial(int id);



    @Select("SELECT post.id, user.name as creator, header.subject, header.priority, post.publish_date as created, " +
            "IFNULL(`r`.`ar`,0) as rating, IFNULL(`r`.`cr`,0) as rated, post.header_id, " +
            "#{allversions} as allversions, " +
            "#{nocomments} as nocomments, " +
            "#{ord} as ord, " +
            "#{unpublished} as unpublished " +
            "FROM post " +
            "JOIN header ON post.header_id = header.id " +
            "JOIN user ON user.id = header.user_id " +
            "LEFT JOIN ( " +
            "SELECT AVG(rate) as `ar`, COUNT(rate) as `cr`, post_id FROM rating " +
            "JOIN post ON post.id = rating.post_id " +
            "WHERE post_id = post.id GROUP BY post_id " +
            ") as `r` ON `r`.post_id = post.id " +
            "WHERE post.id = #{p.id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "body", column = "{postId = id, allversions = allversions, unpublished = unpublished }", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getBodies",
                            fetchType = FetchType.EAGER)),
            @Result(property = "tags", column = "header_id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getTagValues",
                            fetchType = FetchType.EAGER)),
            @Result(property = "comments", column = "{postId = id, nocomments = nocomments, ord = ord, allversions = allversions, unpublished = unpublished}", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getCommentsInfo",
                            fetchType = FetchType.EAGER))
    })
    MessageInfo getMessage(@Param("p") Post message,
                           @Param("allversions") boolean allversions,
                           @Param("nocomments") boolean nocomments,
                           @Param("unpublished") boolean unpublished,
                           @Param("ord") String order);


    @Select(
            {"<script>",
                    "SELECT CONCAT(IF(history.published = 'UNPUBLISHED', '[UNPUBLISHED] ', ''), history.post_body) as postBody FROM history ",
                    "<where>",
                    "<if test='!unpublished'> published = 'PUBLISHED' AND ",
                    "</if>",
                    "<if test='!allversions'> id = (SELECT id FROM history WHERE post_id = #{postId} ORDER BY id DESC LIMIT 1) AND ",
                    "</if>",
                    "post_id = #{postId} ORDER BY id DESC",
                    "</where>",
            "</script>"
            })
    List<String> getBodies(@Param("postId") int postId,
                           @Param("allversions") boolean allversions,
                           @Param("unpublished") boolean unpublished);


    @Select("SELECT tags.tag FROM tags WHERE header_id = #{headerId}")
    List<String> getTagValues(int headerId);


    @Select(
            {"<script>",
                    "SELECT post.id, user.name as creator, post.publish_date as created, " +
                            "IFNULL(`r`.`ar`,0) as rating, IFNULL(`r`.`cr`,0) as rated, " +
                            "#{allversions} as allversions, " +
                            "#{unpublished} as unpublished, " +
                            "#{nocomments} as nocomments, " +
                            "#{ord} as ord " +
                            "FROM post " +
                            "JOIN user ON user.id = post.user_id " +
                            "LEFT JOIN ( " +
                            "SELECT AVG(rate) as `ar`, COUNT(rate) as `cr`, post_id FROM rating " +
                            "JOIN post ON post.id = rating.post_id " +
                            "WHERE post_id = post.id GROUP BY post_id " +
                            ") as `r` ON `r`.post_id = post.id ",
                    "<where>",
                    "<if test='nocomments'> post.id IS NULL AND ",
                    "</if>",
                    "post.ancestor_id = #{postId} ORDER BY post.id ",
                    "<if test='ord.equals(\"DESC\") '> DESC ",
                    "</if>",
                    "</where>",
                            "</script>"
            })
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "body", column = "{postId = id, allversions = allversions, unpublished = unpublished }", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getBodies",
                            fetchType = FetchType.EAGER)),
            @Result(property = "comments", column = "{postId = id, nocomments = nocomments, ord = ord, allversions = allversions, unpublished = unpublished}", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getCommentsInfo",
                            fetchType = FetchType.EAGER))
    })
    List<CommentInfo> getCommentsInfo(@Param("postId") int postId,
                                      @Param("nocomments") boolean nocomments,
                                      @Param("ord") String ord,
                                      @Param("allversions") boolean allversions,
                                      @Param("unpublished") boolean unpublished);


    @Select({"<script>",
                "SELECT distinct post.id, user.name as creator, header.subject, header.priority, post.publish_date as created, " +
                "IFNULL(`r`.`ar`,0) as rating, IFNULL(`r`.`cr`,0) as rated, post.header_id, " +
                "#{allversions} as allversions, " +
                "#{nocomments} as nocomments, " +
                "#{ord} as ord, " +
                "#{unpublished} as unpublished " +
                "FROM post " +
                "JOIN header ON post.header_id = header.id " +
                "JOIN user ON user.id = header.user_id " +
                "LEFT JOIN tags ON tags.header_id = header.id " +
                "LEFT JOIN ( " +
                "SELECT AVG(rate) as `ar`, COUNT(rate) as `cr`, post_id FROM rating " +
                "JOIN post ON post.id = rating.post_id " +
                "WHERE post_id = post.id GROUP BY post_id " +
                ") as `r` ON `r`.post_id = post.id ",
                "<where>" +
                    "header.forum_id = #{f.id} AND post.ancestor_id IS NULL " +
                    "<if test='set != null'> AND header.id IN ( ",
                        "SELECT header_id FROM tags WHERE tag IN (",
                        "<foreach item='item' collection='set' separator=','>",
                        "#{item}",
                        "</foreach>",
                        ")) ",
                    "</if>",
                    " ORDER BY header.priority ASC, post.id ",
                    "<if test='ord.equals(\"DESC\")'> DESC ",
                    "</if>",
                    "LIMIT #{limit} OFFSET #{offset}",
                "</where>" +
            "</script>"
            })
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "body", column = "{postId = id, allversions = allversions, unpublished = unpublished }", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getBodies",
                            fetchType = FetchType.EAGER)),
            @Result(property = "tags", column = "header_id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getTagValues",
                            fetchType = FetchType.EAGER)),
            @Result(property = "comments", column = "{postId = id, nocomments = nocomments, ord = ord, allversions = allversions, unpublished = unpublished}", javaType = List.class,
                    many = @Many(select = "net.thumbtack.forums.mappers.mybatis.ForumMapper.getCommentsInfo",
                            fetchType = FetchType.EAGER))
    })
    List<MessageInfo> getForumMessages(@Param("f") Forum forum,//
                                       @Param("allversions") boolean allversions,//
                                       @Param("nocomments") boolean nocomments,//
                                       @Param("unpublished") boolean unpublished,//
                                       @Param("set") Set<String> tags,
                                       @Param("ord") String order,//
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);





    @Insert("INSERT INTO forum (name, moderate, user_id) " +
            "VALUES(#{f.name}, #{f.moderateStatus}, #{f.user.id})")
    @Options(useGeneratedKeys = true, keyProperty = "f.id")
    Integer createForum(@Param("f") Forum forum);


    @Insert("INSERT INTO header (subject, priority, forum_id, user_id) " +
            "VALUES(#{h.subject}, #{h.priority}, #{h.forum.id}, #{m.user.id})")
    @Options(useGeneratedKeys = true, keyProperty = "h.id")
    Integer createHeader(@Param("h") Header header, @Param("m") Post message);


    @Insert("INSERT INTO post (publish_date, header_id, user_id) " +
            "VALUES(#{m.publishDate}, #{h.id}, #{m.user.id} )")
    @Options(useGeneratedKeys = true, keyProperty = "m.id")
    Integer createMessage(@Param("h") Header header, @Param("m") Post message);


    @Insert("INSERT INTO post (publish_date, header_id, user_id, ancestor_id) " +
            "VALUES(#{c.publishDate}, #{c.header.id}, #{c.user.id}, #{c.ancestor.id})")
    @Options(useGeneratedKeys = true, keyProperty = "c.id")
    Integer addComment(@Param("c") Post comment);


    @Insert("INSERT INTO history (post_body, published, post_id) " +
            "VALUES(#{h.postBody},  #{h.postState}, #{id})")
    @Options(useGeneratedKeys = true, keyProperty = "h.id")
    Integer addHistory(@Param("id") int id, @Param("h") History history);


    @Insert({"<script>",
            "INSERT INTO tags (tag, header_id) VALUES",
            "<foreach item='item' collection='set' separator=','>",
            "( #{item.tag}, #{id} )",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "set.id")
    void addTags(@Param("set") Set<Tag> tags, @Param("id") int id);


    @Insert("INSERT INTO rating (rate, post_id, user_id) VALUES (#{r.rate}, #{r.post.id}, #{r.user.id})")
    @Options(useGeneratedKeys = true, keyProperty = "r.id")
    Integer addRate(@Param("r") Rate rate);


    @Update("UPDATE history SET published = 'PUBLISHED' WHERE post_id = #{p.id} ORDER BY id DESC LIMIT 1 ")
    void publishPost(@Param("p") Post post);

    @Update("UPDATE header SET priority = #{priority} WHERE id = #{h.id}")
    void changePriority(@Param("priority") PostPriority priority, @Param("h") Header header);


    @Update("UPDATE forum SET read_only = TRUE WHERE moderate = 'MODERATED' AND user_id = #{user.id}")
    void switchForumsToReadOnly(@Param("user") User user);


    @Update("UPDATE history SET post_body = #{nb} WHERE post_id = #{p.id} ORDER BY id DESC LIMIT 1 ")
    void replaceBody(@Param("p") Post post, @Param("nb") String newBody);

    @Update("UPDATE rating SET rate = #{rate.rate} WHERE post_id = #{rate.post.id} AND user_id = #{rate.user.id} ")
    void changeRate(@Param("rate") Rate rate);


    @Update("UPDATE post SET header_id = #{h.id}, ancestor_id = NULL WHERE id = #{c.id}")
    void createMessageFromComment(@Param("h") Header header, @Param("c") Post comment);


    @Update("UPDATE post SET header_id = #{h.id} WHERE id = #{c.id}")
    void replaceHeader(@Param("h") Header header, @Param("c") Post comment);


    @Delete("DELETE FROM forum WHERE id=#{forum.id}")
    void deleteForum(@Param("forum") Forum forum);


    @Delete("DELETE FROM post WHERE id=#{post.id}")
    void deletePost(@Param("post") Post post);


    @Delete("DELETE FROM history WHERE post_id=#{post.id} AND published = 'UNPUBLISHED' ")
    void deleteHistory(@Param("post") Post post);


    @Delete("DELETE FROM header WHERE id = #{post.header.id}")
    void deleteHeader(@Param("post") Post post);


    @Delete("DELETE FROM rating WHERE id = #{rate.id}")
    void clearRate(@Param("rate") Rate rate);


}
