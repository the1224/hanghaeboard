package com.sparta.hanghaememo.entity;

import com.sparta.hanghaememo.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String username;


    public Board(BoardRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.userId = user.getId();
        this.username = user.getUsername();
    }

    public void update(BoardRequestDto boardRequestDto, Long userId) {
        this.contents = boardRequestDto.getContents();
        this.userId = userId;
    }

}
