package com.tw.community.service;

import com.tw.community.dto.PaginationDTO;
import com.tw.community.dto.QuestionDTO;
import com.tw.community.exception.CustomizeErrorCode;
import com.tw.community.exception.CustomizeException;
import com.tw.community.mapper.QuestionMapper;
import com.tw.community.mapper.UserMapper;
import com.tw.community.model.Question;
import com.tw.community.model.QuestionExample;
import com.tw.community.model.User;
import com.tw.community.model.UserExample;
import javafx.scene.control.Pagination;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size){
        List<Question> list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds((page - 1) * size, size));
        List<QuestionDTO> dtoList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : list) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().
                    andAccountIdEqualTo(question.getCreator());
            List<User> byId = userMapper.selectByExample(userExample);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(byId.get(0));
            dtoList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOList(dtoList);
        Integer totalCount = questionMapper.countByExample(new QuestionExample());
        paginationDTO.setPagination(totalCount, page, size);
        return paginationDTO;
    }

    public PaginationDTO listByAccountId(String accountId, Integer page, Integer size){
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(accountId);
        List<Question> list = questionMapper.selectByExampleWithRowbounds(example, new RowBounds((page - 1) * size, size));
        List<QuestionDTO> dtoList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : list) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().
                    andAccountIdEqualTo(question.getCreator());
            List<User> byId = userMapper.selectByExample(userExample);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(byId.get(0));
            dtoList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOList(dtoList);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(accountId);
        Integer countByAccountId = questionMapper.countByExample(questionExample);
        paginationDTO.setPagination(countByAccountId, page, size);
        return paginationDTO;
    }


    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);

        UserExample userExample = new UserExample();
        userExample.createCriteria().
                andAccountIdEqualTo(question.getCreator());
        List<User> byId = userMapper.selectByExample(userExample);
        questionDTO.setUser(byId.get(0));
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        }else{
            question.setGmtModified(System.currentTimeMillis());
            int update = questionMapper.updateByPrimaryKey(question);
            if (update != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incViewCount(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setViewCount( question.getViewCount() + 1 );
        QuestionExample example = new QuestionExample();
        example.createCriteria().andIdEqualTo(id);
        questionMapper.updateByExampleSelective( question, example);
    }
}
