package com.social.gatherly.Service;


import com.social.gatherly.Configuration.JwtTokenProvider;
import com.social.gatherly.Configuration.KakaoProperties;
import com.social.gatherly.Configuration.RestTemplateConfig;
import com.social.gatherly.Dto.AuthResponseDto;
import com.social.gatherly.Dto.Kakao.KakaoTokenResponse;
import com.social.gatherly.Dto.Kakao.KakaoUserResponse;
import com.social.gatherly.Entity.Users;
import com.social.gatherly.Enum.Provider;
import com.social.gatherly.Enum.Role;
import com.social.gatherly.Exception.OAuthException;
import com.social.gatherly.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;


/**
 * 카카오 OAuth 로그인 전체 과정을 담당하는 서비스입니다
 * 역할:
 * 1.Authorization Code 수신
 * 2.Access Token 요청
 * 3.사용자 정보 조회
 * 4.DB 사용자 확인
 * 5.신규 회원 생성
 * 6.JWT 생성
 */
@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    //외부 API와 통신하기 위한 객체입니다
    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;
    private  final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDto login(String authorizationCode){
        //Authorization Code - > Access Token
        KakaoTokenResponse tokenResponse = getAccessToken(authorizationCode);
        //Access Token -> UserInfo
        KakaoUserResponse kakaoUserResponse = getUserInfo(tokenResponse.getAccessToken());
        //User 확인
        Users user = findOrCreateUser(kakaoUserResponse);
        //JWT 생성
        String jwt = jwtTokenProvider.generateToken(user.getEmail());

        //response
        return new AuthResponseDto(
                jwt,
                "KakaoLogin is successful"
        );

    }

    private KakaoTokenResponse getAccessToken(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        //요청 바디 만들기
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("client_secret", kakaoProperties.getClientSecret());
        body.add("redirect_url", kakaoProperties.getRedirectUri());
        body.add("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        //JSON 아니고 FORM DATA임을 알려준다
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<KakaoTokenResponse> response =
                restTemplate.postForEntity(
                        kakaoProperties.getRedirectUri(),
                        request,
                        //JSON ->>DTO
                        KakaoTokenResponse.class
                );
        return response.getBody();
    }


    //유저 정보를 가져오기
    private KakaoUserResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response =
                restTemplate.postForEntity(
                        kakaoProperties.getUserInfoUri(),
                        request,
                        KakaoUserResponse.class
                );
        return response.getBody();
    }

    private Users findOrCreateUser(KakaoUserResponse kakaoUser) {
        String email = kakaoUser.getKakaoAccount().getEmail();

        String nickname = kakaoUser.getKakaoAccount().getProfile().getNickname();

        String providerId = String.valueOf(kakaoUser.getId());

        Optional<Users> optionalUsers = usersRepository.findByEmail(email);
        if(!optionalUsers.isPresent()) {
            Users user = optionalUsers.get();
            if(user.getProvider() != Provider.KAKAO) {
                throw new OAuthException(
                        "이 이메일" + user.getProvider() +
                            "의미 존재합니다"
                );
            }
            return user;
        }
        Users user = new Users();
        user.setUserName(nickname);
        user.setEmail(email);
        user.setProvider(Provider.KAKAO);
        user.setProviderId(providerId);
        user.setPassword(UUID.randomUUID().toString());
        user.setRole(Role.USER);
        return usersRepository.save(user);
    }







}
