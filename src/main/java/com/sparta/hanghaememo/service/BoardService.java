package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.dto.BoardRequestDto;
import com.sparta.hanghaememo.dto.CommentRequestDto;
import com.sparta.hanghaememo.dto.CommentResponseDto;
import com.sparta.hanghaememo.entity.Board;
import com.sparta.hanghaememo.entity.Comment;
import com.sparta.hanghaememo.entity.User;
import com.sparta.hanghaememo.entity.UserRoleEnum;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.BoardRepository;
import com.sparta.hanghaememo.dto.BoardResponseDto;
import com.sparta.hanghaememo.repository.CommentRepository;
import com.sparta.hanghaememo.repository.UserRepository;
import com.sparta.hanghaememo.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
       Board board = boardRepository.saveAndFlush(new Board(requestDto, user));
       return new BoardResponseDto(board);
    }

    @Transactional(readOnly = true)
    public List<Board> getBoards() {
        return boardRepository.findAllByOrderByModifiedAtDesc();
    }

    //- 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
    //- 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user) {
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );
            if (user.getUsername().equals(board.getUsername()) || user.getRole() == UserRoleEnum.ADMIN){
                board.update(requestDto, id);
                return new BoardResponseDto(board);
            }  //해당 사용자가 작성한 글인지 확인 (토큰의 사용자 이름과 게시글 작성자의 이름 비교)
            else {
                throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
            }

    }

    //- 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능
    //- 선택한 게시글을 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
   @Transactional
    public String deleteBoard(Long id, User user) {
           Board board = boardRepository.findById(id).orElseThrow(
                   () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
           );
           if (user.getUsername().equals(board.getUsername()) || user.getRole() == UserRoleEnum.ADMIN){
               boardRepository.deleteById(id);
               return "Delete Success";
           }  //해당 사용자가 작성한 글인지 확인 (토큰의 사용자 이름과 게시글 작성자의 이름 비교)
           else {
               throw new IllegalArgumentException("사용자가 작성한 글이 아닙니다.");
           }
   }

    @Transactional(readOnly = true)
    public List<Comment> getComments() {
        return commentRepository.findAllByOrderByModifiedAtDesc();
    }

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, Long id, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰이 있는 경우에만 글 작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Comment comment = commentRepository.saveAndFlush(new Comment(requestDto, user, id));
            return new CommentResponseDto(comment);
        }
        else {
            return null;
        }
    }

    //- 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
    //- 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰이 있는 경우에만 글 작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );
            if (user.getUsername().equals(comment.getUsername()) || user.getRole() == UserRoleEnum.ADMIN){
                comment.update(requestDto, commentId);
                return new CommentResponseDto(comment);
            }  //해당 사용자가 작성한 글인지 확인 (토큰의 사용자 이름과 게시글 작성자의 이름 비교)
            else {
                throw new IllegalArgumentException("사용자가 작성한 글이 아닙니다.");
            }
        }
        else {
            return null;
        }
    }

    //- 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능
    //- 선택한 게시글을 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    @Transactional
    public String deleteComment(Long commentId, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰이 있는 경우에만 글 삭제 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );
            if (user.getUsername().equals(comment.getUsername()) || user.getRole() == UserRoleEnum.ADMIN){
                commentRepository.deleteById(commentId);
                return "Delete Success";
            }  //해당 사용자가 작성한 글인지 확인 (토큰의 사용자 이름과 게시글 작성자의 이름 비교)
            else {
                throw new IllegalArgumentException("사용자가 작성한 글이 아닙니다.");
            }
        }
        else {
            return null;
        }
    }
}

