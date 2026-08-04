package com.social.gatherly.dto;

import com.social.gatherly.Enum.Provider;
import com.social.gatherly.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;

    private String userName;

    private String email;

    private String userPhone;

    private  String profileImageUrl;

    private Provider provider;

    public static UserResponseDto from (Users user, String domain) {
        return  UserResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .userPhone(user.getUserPhone())
                .profileImageUrl(user.getProfileImageUrl() != null ? domain + user.getProfileImageUrl() : null)
                .provider(user.getProvider())
                .build();
    }
}
