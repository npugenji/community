package com.tw.community.dto;


import lombok.Data;

@Data
public class QuestionQueryDTO extends QuestionDTO {
    private String search;
    private Integer page;
    private Integer size;
}
