package com.tw.community.service;

import com.tw.community.dto.PaginationDTO;
import com.tw.community.dto.QuestionDTO;
import com.tw.community.mapper.QuestionMapper;
import com.tw.community.mapper.UserMapper;
import com.tw.community.model.Question;
import com.tw.community.model.User;
import javafx.scene.control.Pagination;
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
        List<Question> list = questionMapper.list((page - 1) * size, size);
        List<QuestionDTO> dtoList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : list) {
            User byId = userMapper.findByAccountId(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(byId);
            dtoList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOList(dtoList);
        Integer totalCount = questionMapper.getCount();
        paginationDTO.setPagination(totalCount, page, size);
        return paginationDTO;
    }

    public PaginationDTO listByAccountId(String accountId, Integer page, Integer size){
        List<Question> list = questionMapper.listByAccountId(accountId, (page - 1) * size, size);
        List<QuestionDTO> dtoList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : list) {
            User byId = userMapper.findByAccountId(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(byId);
            dtoList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOList(dtoList);
        Integer countByAccountId = questionMapper.getCountByAccountId(accountId);
        paginationDTO.setPagination(countByAccountId, page, size);
        return paginationDTO;
    }


    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);

        User user = userMapper.findByAccountId(questionDTO.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else{
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }
}
