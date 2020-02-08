package com.tw.community.controller;

import com.tw.community.dto.PaginationDTO;
import com.tw.community.mapper.UserMapper;
import com.tw.community.model.User;
import com.tw.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          @RequestParam( name = "page", defaultValue = "1") Integer page,
                          @RequestParam( name = "size", defaultValue = "5") Integer size,
                          Model model,
                          HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            return "redirect:/";

        if ("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        }else if ( "replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        PaginationDTO paginationDTO = questionService.listByAccountId(user.getAccountId(), page, size);
        model.addAttribute("pagination", paginationDTO);

        return "profile";
    }
}
