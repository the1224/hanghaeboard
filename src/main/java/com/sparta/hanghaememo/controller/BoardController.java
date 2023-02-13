package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.BoardRequestDto;
import com.sparta.hanghaememo.entity.Board;
import com.sparta.hanghaememo.service.BoardService;
import com.sparta.hanghaememo.dto.BoardResponseDto;
import lombok.RequiredArgsConstructor;
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
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.createBoard(requestDto, request);
    }

     @GetMapping("/api/boards")
    public List<Board> getBoards() {
        return boardService.getBoards();
    }
/*     @PutMapping("/api/boards/{id}")
    public Board updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.update(id, requestDto);
    }

    @DeleteMapping("/api/boards/{id}")
    public boolean deleteBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        String password = requestDto.getPassword();
        return boardService.deleteBoard(id, password);
    }
*/
}
