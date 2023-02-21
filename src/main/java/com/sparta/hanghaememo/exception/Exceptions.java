package com.sparta.hanghaememo.exception;

import com.sparta.hanghaememo.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class Exceptions {
    private String errorMessage;
    private HttpStatus httpStatus;
}