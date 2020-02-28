package com.tw.community.controller;

import com.tw.community.CommentTypeEnum;
import com.tw.community.dto.CommentDTO;
import com.tw.community.dto.QuestionDTO;
import com.tw.community.mapper.QuestionMapper;
import com.tw.community.model.Question;
import com.tw.community.service.CommentService;
import com.tw.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model) {
        questionService.incViewCount(id);
        QuestionDTO questionDTO = questionService.getById(id);
        List<Question> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> commentDTOList = commentService.getByParentId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOList);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
