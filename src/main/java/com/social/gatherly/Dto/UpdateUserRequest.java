package com.social.gatherly.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String userName;
    private String password;
    private String email;
}
