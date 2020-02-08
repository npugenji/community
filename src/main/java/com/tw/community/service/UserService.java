package com.tw.community.service;


import com.tw.community.dto.GithubUser;
import com.tw.community.mapper.UserMapper;
import com.tw.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void createOrUpdate(GithubUser githubUser,
                               HttpServletResponse response){
        String accountId = String.valueOf(githubUser.getId());
        String token = UUID.randomUUID().toString();
        User dbUser = userMapper.findByAccountId(accountId);
        response.addCookie(new Cookie("token", token));
        if (dbUser == null){
            dbUser = new User();
            dbUser.setAccountId(accountId);
            dbUser.setGmtCreate(System.currentTimeMillis());
            dbUser.setGmtModified(dbUser.getGmtCreate());
            dbUser.setAvatarUrl(githubUser.getAvatar_url());
            dbUser.setName(githubUser.getName());
            dbUser.setToken(token);
            userMapper.insert(dbUser);
        }else{
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(githubUser.getAvatar_url());
            dbUser.setName(githubUser.getName());
            dbUser.setToken(token);
            userMapper.update(dbUser);
        }
    }
}
