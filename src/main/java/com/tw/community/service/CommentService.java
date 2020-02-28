package com.tw.community.service;


import com.tw.community.CommentTypeEnum;
import com.tw.community.dto.CommentDTO;
import com.tw.community.exception.CustomizeErrorCode;
import com.tw.community.exception.CustomizeException;
import com.tw.community.mapper.CommentMapper;
import com.tw.community.mapper.QuestionMapper;
import com.tw.community.mapper.UserMapper;
import com.tw.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        if  ( comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        if ( comment.getType() == null || !CommentTypeEnum.contains(comment.getType()) ) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if ( comment.getType() == CommentTypeEnum.QUESTION.getType() ) {
            // 回复一个问题
            Question question = questionMapper.selectByPrimaryKey( comment.getParentId() );
            if ( question == null  ) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            question.setCommentCount( question.getCommentCount() + 1 );
            questionMapper.updateByPrimaryKeySelective(question);
            commentMapper.insert(comment);
        } else {
            // 回复一个评论
            Comment dbcomment = commentMapper.selectByPrimaryKey( comment.getParentId() );
            if ( dbcomment == null ) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }
    }

    public List<CommentDTO> getByParentId(Integer id, CommentTypeEnum typeEnum) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(typeEnum.getType());
        commentExample.setOrderByClause("gmt_create desc");
        //find all the comments that have parentId = id
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0)
            return new ArrayList<>();

        //get all the unique commentators of comments
        Set<String> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<String> userAccountIds = new ArrayList<>();
        userAccountIds.addAll(commentators);

        // get the information for each commentator
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdIn(userAccountIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getAccountId(), user -> user));

        // match each comment with the corresponding user
        List<CommentDTO> commentDTOS = comments.stream().map( comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            CommentExample sonsExample = new CommentExample();
            sonsExample.createCriteria()
                    .andParentIdEqualTo(comment.getId())
                    .andTypeEqualTo(CommentTypeEnum.COMMENT.getType());
            List<Comment> sons = commentMapper.selectByExample(sonsExample);
            commentDTO.setUser( userMap.get(comment.getCommentator()) );
            commentDTO.setCommentCount( sons.size() );
            return commentDTO;
        } ).collect(Collectors.toList());

        return commentDTOS;
    }
}
