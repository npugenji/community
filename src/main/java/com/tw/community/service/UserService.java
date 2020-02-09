package com.tw.community.service;


import com.tw.community.dto.GithubUser;
import com.tw.community.mapper.UserMapper;
import com.tw.community.model.User;
import com.tw.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void createOrUpdate(GithubUser githubUser,
                               HttpServletResponse response){
        String accountId = String.valueOf(githubUser.getId());
        String token = UUID.randomUUID().toString();
        UserExample example = new UserExample();
        example.createCriteria()
                .andAccountIdEqualTo(accountId);
        List<User> dbUser = userMapper.selectByExample(example);
        response.addCookie(new Cookie("token", token));
        if (dbUser.size() == 0){
            User user = new User();
            user.setAccountId(accountId);
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            user.setName(githubUser.getName());
            user.setToken(token);
            userMapper.insert(user);
        }else{
            User user = dbUser.get(0);

            user.setGmtModified(System.currentTimeMillis());
            user.setAvatarUrl(githubUser.getAvatar_url());
            user.setName(githubUser.getName());
            user.setToken(token);

            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andIdEqualTo(user.getId());
            userMapper.updateByExampleSelective(user, userExample);
        }
    }
}
