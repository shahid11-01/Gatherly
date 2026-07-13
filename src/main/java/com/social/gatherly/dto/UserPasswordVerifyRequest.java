package com.social.gatherly.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPasswordVerifyRequest {
    private String password;
}
