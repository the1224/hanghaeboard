package com.sparta.hanghaememo.entity;

import com.sparta.hanghaememo.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String contents;

    public Board(String username, String password, String contents) {
        this.username = username;
        this.password = password;
        this.contents = contents;
    }

    public Board(BoardRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
        this.password = requestDto.getPassword();
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.username = boardRequestDto.getUsername();
        this.contents = boardRequestDto.getContents();
        this.password = boardRequestDto.getPassword();
    }

}
