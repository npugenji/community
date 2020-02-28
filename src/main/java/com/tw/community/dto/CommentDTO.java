package com.tw.community.dto;

import com.tw.community.model.Comment;
import com.tw.community.model.User;
import lombok.Data;

@Data
public class CommentDTO extends Comment {
    private User user;
    private Integer commentCount;
}
