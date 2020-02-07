package net.thumbtack.forums.mappers.mybatis;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {


    @Delete("DELETE FROM session")
    void deleteFromSession();


    @Delete("DELETE FROM user WHERE name != 'admin'")
    void deleteFromUser();


    @Delete("DELETE FROM forum")
    void deleteFromForum();


    @Delete("DELETE FROM header")
    void deleteFromHeader();


    @Delete("DELETE FROM post")
    void deleteFromMessage();


    @Delete("DELETE FROM history")
    void deleteFromHistory();


    @Delete("DELETE FROM tags")
    void deleteFromTags();


    @Delete("DELETE FROM rating")
    void deleteFromRating();


}
