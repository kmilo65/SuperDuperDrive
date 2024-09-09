package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.data.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Select("SELECT DISTINCT username FROM USERS")
    List<String> getUserNames();


    @Select("SELECT * FROM USERS WHERE userid = #{userid}")
    User getUserById(Integer userid);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstname}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insertUser(User user);

    @Update("UPDATE USERS SET username=#{username}, firstname=#{firstname}, lastname=#{lastname} WHERE userid=#{userid}")
    void updateUser(User user);

    @Delete("DELETE FROM USERS WHERE userid=#{userid}")
    void deleteUser(Integer userid);

    @Delete("DELETE FROM USERS WHERE username=#{username}")
    void deleteByUsername(String username);


}

