package com.social.gatherly.service;


import com.social.gatherly.configuration.JwtTokenProvider;
import com.social.gatherly.configuration.KakaoProperties;
import com.social.gatherly.dto.AuthResponseDto;
import com.social.gatherly.dto.kakao.KakaoTokenResponse;
import com.social.gatherly.dto.kakao.KakaoUserResponse;
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

import java.util.Optional;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    //외부 API와 통신하기 위한 객체입니다
    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;
    private  final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    public AuthResponseDto login(String accessToken){
            KakaoUserResponse kakaoUser = getUserInfo(accessToken);
            Users user = findOrCreateUser(kakaoUser);
            return tokenService.generateTokens(user.getEmail(), "카카오 로그인 성공했어요");
    }




    //유저 정보를 가져오기
    private KakaoUserResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response =
                restTemplate.exchange(
                        kakaoProperties.getUserInfoUri(),
                        HttpMethod.GET,
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
        if(optionalUsers.isPresent()) {
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
