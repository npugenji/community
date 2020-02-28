package com.tw.community.controller;

import com.tw.community.dto.PaginationDTO;
import com.tw.community.dto.QuestionDTO;
import com.tw.community.mapper.UserMapper;
import com.tw.community.model.Question;
import com.tw.community.model.User;
import com.tw.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam( name = "page", defaultValue = "1") Integer page,
                        @RequestParam( name = "size", defaultValue = "5") Integer size,
                        @RequestParam( name = "search", required = false ) String search) {
        PaginationDTO paginationDTO = questionService.list(search, page, size);
        model.addAttribute("pagination", paginationDTO);
        model.addAttribute("search", search);
        return "index";
    }
}
