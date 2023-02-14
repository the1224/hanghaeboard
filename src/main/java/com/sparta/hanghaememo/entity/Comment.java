package com.sparta.hanghaememo.entity;

import com.sparta.hanghaememo.dto.BoardRequestDto;
import com.sparta.hanghaememo.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Long boardId;

    public Comment(CommentRequestDto requestDto, User user, Long id) {
        this.contents = requestDto.getContents();
        this.userId = user.getId();
        this.boardId = id;
        this.username = user.getUsername();
    }

    public void update(CommentRequestDto commentRequestDto, Long userId) {
        this.contents = commentRequestDto.getContents();
        this.userId = userId;
    }

}
