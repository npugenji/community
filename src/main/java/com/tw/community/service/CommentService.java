package com.tw.community.service;


import com.tw.community.CommentTypeEnum;
import com.tw.community.exception.CustomizeErrorCode;
import com.tw.community.exception.CustomizeException;
import com.tw.community.mapper.CommentMapper;
import com.tw.community.mapper.QuestionMapper;
import com.tw.community.model.Comment;
import com.tw.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    CommentMapper commentMapper;

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
}
