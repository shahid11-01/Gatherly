package com.social.gatherly.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String userName;
    private String email;
}
