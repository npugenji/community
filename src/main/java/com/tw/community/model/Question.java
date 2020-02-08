package com.tw.community.model;


import lombok.Data;

@Data
public class Question {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private String creator;
    private Integer ViewCount;
    private Integer CommentCount;
    private Integer LikeCount;
}
