package com.social.gatherly.service;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.social.gatherly.configuration.GoogleProperties;
import com.social.gatherly.dto.AuthResponseDto;
import com.social.gatherly.dto.google.GoogleTokenResponse;
import com.social.gatherly.dto.google.GoogleUserResponse;
import com.social.gatherly.entity.Users;
import com.social.gatherly.Enum.Provider;
import com.social.gatherly.Enum.Role;
import com.social.gatherly.exception.OAuthException;
import com.social.gatherly.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    private final GoogleProperties googleProperties;
    private final RestTemplate restTemplate;
    private final UsersRepository usersRepository;
    private final TokenService tokenService;

    public AuthResponseDto login(String idTokenString) {
            //빌딩 Verifier
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleProperties.getClientId()))
                .build();

        //Verify signature + expiry + audience
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        }catch (Exception e) {
            throw new OAuthException("Google ID 토큰 검증 실패");
        }
        if(idToken == null) {
            throw new OAuthException("유효하지 않은 Google ID 토큰입니다");
        }
        //토큰안에서 유저 정보를 가져오기
        GoogleIdToken.Payload payload = idToken.getPayload();
        GoogleUserResponse googleUser = new GoogleUserResponse();
        googleUser.setSub(payload.getSubject());
        googleUser.setEmail(payload.getEmail());
        googleUser.setEmailVerified(payload.getEmailVerified());
        googleUser.setName((String) payload.get("name"));
        googleUser.setPicture((String) payload.get("picture"));

        Users user = findOrCreateUser(googleUser);
        return tokenService.generateTokens(user.getEmail(), "구글 로그인 성공했습니다");


    }


    private Users findOrCreateUser(GoogleUserResponse googleUser)  {
        if(googleUser.getEmail() == null) {
            throw new OAuthException("Google 이메일 정보를 가져올 수 없습니다");
        }
        String name = googleUser.getName();
        String email = googleUser.getEmail();
        String providerId = googleUser.getSub();

        Optional<Users> optionalUsers = usersRepository.findByEmail(email);
        if(optionalUsers.isPresent()) {
            Users user = optionalUsers.get();
            if(user.getProvider() != Provider.GOOGLE) {
                throw  new OAuthException("이 이메일은 " + user.getProvider() +
                        "계정으로 가입되어 있습니다");
            }
            return user;
        }
        Users user = new Users();
        user.setUserName(name);
        user.setEmail(email);
        user.setProvider(Provider.GOOGLE);
        user.setProviderId( providerId);
        user.setPassword(UUID.randomUUID().toString());
        user.setRole(Role.USER);
        return usersRepository.save(user);

    }




}
