package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.BoardRequestDto;
import com.sparta.hanghaememo.dto.CommentRequestDto;
import com.sparta.hanghaememo.dto.CommentResponseDto;
import com.sparta.hanghaememo.entity.Board;
import com.sparta.hanghaememo.entity.Comment;
import com.sparta.hanghaememo.security.UserDetailsImpl;
import com.sparta.hanghaememo.service.BoardService;
import com.sparta.hanghaememo.dto.BoardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @PostMapping("/api/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoard(requestDto, userDetails.getUser());
    }

     @GetMapping("/api/boards")
    public List<Board> getBoards() {
        return boardService.getBoards();
    }
     @PutMapping("/api/boards/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updateBoard(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/api/boards/{id}")
    public String deleteBoard(@PathVariable Long id,  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(id, userDetails.getUser());
    }

   @GetMapping("/api/boards/{id}")
    public List<Comment> getComments(@PathVariable Long id) {
        return boardService.getComments();
    }

    @PostMapping("/api/boards/{id}")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long id, HttpServletRequest request) {
        return boardService.createComment(requestDto, id, request);
    }
    @PutMapping("/api/boards/{id}/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long id, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return boardService.updateComment(commentId, requestDto, request);
    }

    @DeleteMapping("/api/boards/{id}/{commentId}")
    public String deleteComment(@PathVariable Long id, @PathVariable Long commentId, HttpServletRequest request) {
        return boardService.deleteComment(commentId, request);
    }

}
