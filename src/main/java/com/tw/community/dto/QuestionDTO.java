package com.tw.community.dto;

import com.tw.community.model.Question;
import com.tw.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO extends Question {
    private User user;
}
