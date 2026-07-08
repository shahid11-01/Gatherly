package com.social.gatherly.Service;


import com.social.gatherly.Configuration.GoogleProperties;
import com.social.gatherly.Dto.AuthResponseDto;
import com.social.gatherly.Dto.Google.GoogleTokenResponse;
import com.social.gatherly.Dto.Google.GoogleUserResponse;
import com.social.gatherly.Entity.Users;
import com.social.gatherly.Enum.Provider;
import com.social.gatherly.Enum.Role;
import com.social.gatherly.Exception.OAuthException;
import com.social.gatherly.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    private final GoogleProperties googleProperties;
    private final RestTemplate restTemplate;
    private final UsersRepository usersRepository;
    private final TokenService tokenService;

    public AuthResponseDto login(String authorizationCode) {
        //Authorization code -> access token
        GoogleTokenResponse tokenResponse = getAccessToken(authorizationCode);
        //Access Token -> UserInfo
        GoogleUserResponse googleUserResponse =getUserInfo(tokenResponse.getAccessToken());
        //User 확인
        Users user = findOrCreateUser(googleUserResponse);
        return tokenService.generateTokens(
                user.getEmail(),
                "Google Login Successful"
        );

    }


    private GoogleTokenResponse getAccessToken(String authorizationCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code" );
        params.add("client_id", googleProperties.getClientId());
        params.add("client_secret", googleProperties.getClientSecret());
        params.add("redirect_uri", googleProperties.getRedirectUri());
        params.add("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<GoogleTokenResponse> response =
                restTemplate.postForEntity(
                        googleProperties.getUserInfoUri(),
                        request,
                        GoogleTokenResponse.class
                );
        return response.getBody();

    }

    private GoogleUserResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

       ResponseEntity<GoogleUserResponse> response =
               restTemplate.exchange(
                       googleProperties.getTokenUri(),
                       HttpMethod.GET,
                       request,
                       GoogleUserResponse.class
               );
       return response.getBody();
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
