package com.social.gatherly.Dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPasswordVerifyRequest {
    private String password;
}
