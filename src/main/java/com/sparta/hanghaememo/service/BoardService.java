package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.dto.BoardRequestDto;
import com.sparta.hanghaememo.entity.Board;
import com.sparta.hanghaememo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Board createBoard(BoardRequestDto requestDto) {
        Board board = new Board(requestDto);
        boardRepository.save(board);
        return board;
    }

    @Transactional(readOnly = true)
    public List<Board> getBoards() {
        return boardRepository.findAllByOrderByModifiedAtDesc();
    }

    @Transactional
    public Long update(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if (board.getPassword().equals(requestDto.getPassword())){
        board.update(requestDto);
        return board.getId();}
        else {
            return 999999L;
        }
    }

    @Transactional
    public boolean deleteBoard(Long id, String password) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if (board.getPassword().equals(password)){
        boardRepository.deleteById(id);
            System.out.println("비밀번호 일치");
        return true;
        }
        else {
            System.out.println("비밀번호 불일치");
            return false;
        }
    }

}