package com.tw.community.controller;


import com.tw.community.CommentTypeEnum;
import com.tw.community.dto.CommentDTO;
import com.tw.community.dto.ResultDTO;
import com.tw.community.exception.CustomizeErrorCode;
import com.tw.community.mapper.CommentMapper;
import com.tw.community.model.Comment;
import com.tw.community.model.User;
import com.tw.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @ResponseBody
    @RequestMapping( value = "/comment", method = RequestMethod.POST)
    public ResultDTO post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if ( commentDTO == null || StringUtils.isBlank(commentDTO.getContent()) ) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setLikeCount(0);
        comment.setCommentator(user.getAccountId());
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping( value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO <List<CommentDTO>> comments(@PathVariable( name = "id" ) Integer id) {
        List<CommentDTO> commentDTOS = commentService.getByParentId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
