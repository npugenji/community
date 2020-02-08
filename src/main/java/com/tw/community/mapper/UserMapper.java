package com.tw.community.mapper;


import com.tw.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(name, account_id, token, gmt_create, gmt_modified, avatar_url) values(#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl})")
    void insert(User user);

    @Select("SELECT * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("Select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set name = #{name}, token = #{token}, gmt_modified = #{gmtModified}, avatar_url = #{avatarUrl} where account_id = #{accountId}")
    void update(User user);
}
