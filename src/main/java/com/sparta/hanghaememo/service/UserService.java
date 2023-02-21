package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.dto.LoginRequestDto;
import com.sparta.hanghaememo.dto.SignupRequestDto;
import com.sparta.hanghaememo.entity.User;
import com.sparta.hanghaememo.entity.UserRoleEnum;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        String email = signupRequestDto.getEmail();

        // 회원 중복 확인 - username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
        //- password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성되어야 한다.
        //- DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }
        else if (username.length()<4 || username.length()>10){
            throw new IllegalArgumentException("username은  `최소 4자 이상, 10자 이하여야 합니다.");
        }
        else if (password.length()<8 || password.length()>15){
            throw new IllegalArgumentException("password는  `최소 8자 이상, 15자 이하여야 합니다.");
        }

        for (int i=0; i<username.length(); i++){
            if ((username.charAt(i) >='0' && username.charAt(i) <='9') || (username.charAt(i) >='a' && username.charAt(i) <='z')){}
            else {
                throw new IllegalArgumentException("username은  `알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 합니다.");
            }
        }

        for (int i=0; i<password.length(); i++){
            if ((password.charAt(i) >='0' && password.charAt(i) <='9') || (password.charAt(i) >='a' && password.charAt(i) <='z') ||
                    (password.charAt(i) >='A' && password.charAt(i) <='Z')){}
            else {
                throw new IllegalArgumentException("password는  알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성되어야 합니다.");
            }
        }



        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("회원을 찾을 수 없습니다.")
        );
        // 비밀번호 확인
        if(!user.getPassword().equals(password)){
            throw  new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
    }
}
