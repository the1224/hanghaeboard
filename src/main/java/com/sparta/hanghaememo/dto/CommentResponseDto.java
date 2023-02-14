package com.sparta.hanghaememo.dto;

import com.sparta.hanghaememo.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private String username;
    private String contents;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment){
        this.username = comment.getUsername();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
    }
}
