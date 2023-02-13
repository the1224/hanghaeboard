package com.sparta.hanghaememo.dto;

import com.sparta.hanghaememo.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private String username;
    private String contents;
    private LocalDateTime createdAt;

    public BoardResponseDto(Board board){
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
    }
}
