package net.thumbtack.forums.mappers.mybatis;

import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.model.User;
import net.thumbtack.forums.model.enums.Privilege;
import net.thumbtack.forums.view.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {



    @Select("SELECT id, name, password, email, deleted, privilege, registered as timeRegistered, " +
            "status as banStatus, ban_exit_time as timeBanExit, ban_count as banCount " +
            "FROM user " +
            "WHERE id = #{id} AND deleted = FALSE")
    User getUserById(@Param("id") int id);


    @Select("SELECT id, name, password, email, deleted, privilege, registered as timeRegistered, " +
            "status as banStatus, ban_exit_time as timeBanExit, ban_count as banCount " +
            "FROM user WHERE id = (SELECT user_id FROM session WHERE token = #{s.token}) AND deleted = FALSE")
    User getUserBySession(@Param("s") Session session);


    @Select("SELECT id, name, password, email, deleted, privilege, registered as timeRegistered, status as banStatus, " +
            "ban_exit_time as timeBanExit, ban_count as banCount " +
            "FROM user " +
            "WHERE name = #{name} AND deleted = FALSE")
    User getUserByName(String name);


    @Select("SELECT privilege FROM user WHERE id = #{user.id}")
    Privilege getUserPrivilege(@Param("user") User user);


    @Select("SELECT user.id, name, password, email, deleted, privilege, registered as timeRegistered, " +
            "status as banStatus, ban_exit_time as timeBanExit, ban_count as banCount, IF(session.token IS NULL, FALSE, TRUE) as online " +
            "FROM user " +
            "LEFT JOIN session ON (session.user_id = user.id)")
    List<UserInfo> getFullUsersInfo();


    @Select("SELECT id, token, user_id as userId FROM session WHERE user_id = #{user.id}")
    Session getSession(@Param("user") User user);




    @Insert("INSERT INTO user (name, password, email) " +
            "VALUES(#{user.name}, #{user.password}, #{user.email})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    void createUser(@Param("user") User user);


    @Insert("INSERT INTO session (token, user_id) VALUES (#{session.token}, #{session.user.id})")
    @Options(useGeneratedKeys = true, keyProperty = "session.id")
    void createSession(@Param("session") Session session);





    @Update("UPDATE user SET deleted = true WHERE id = #{session.user.id}")
    void quitUser(@Param("session") Session session);


    @Update("UPDATE user SET password = #{pass} WHERE id = #{user.id}")
    void updatePassword(@Param("user") User user, @Param("pass") String newPassword);


    @Update("UPDATE user SET privilege = 'SUPER', status = 'FULL' WHERE id = #{user.id}")
    void makeSuper(@Param("user") User user);


    @Update("UPDATE user SET status = 'LIMITED', ban_exit_time = #{u.timeBanExit}, " +
            "ban_count = #{u.banCount} WHERE id = #{u.id}")
    void restrictUser(@Param("u") User user);


    @Update("UPDATE user SET status = 'FULL', ban_exit_time = NULL " +
            "WHERE status = 'LIMITED' AND DATE(ban_exit_time) <= DATE(now())")
    void unbanUsers();



    @Delete("DELETE FROM session WHERE user_id = #{s.user.id}")
    void deleteSession(@Param("s") Session session);



}
