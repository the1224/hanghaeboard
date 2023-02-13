package com.sparta.hanghaememo.entity;

import com.sparta.hanghaememo.dto.BoardRequestDto;
import com.sparta.hanghaememo.repository.UserRepository;
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


    public Board(BoardRequestDto requestDto, Long userId, String username) {
        this.contents = requestDto.getContents();
        this.userId = userId;
        this.username = username;
    }

    public void update(BoardRequestDto boardRequestDto, Long userId) {
        this.contents = boardRequestDto.getContents();
        this.userId = userId;
    }

}
