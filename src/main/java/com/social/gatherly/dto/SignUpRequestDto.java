package com.social.gatherly.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String userName;
    private String email;
    private String password;

}
