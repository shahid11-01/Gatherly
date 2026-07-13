package com.social.gatherly.service;



import com.social.gatherly.entity.Users;
import com.social.gatherly.exception.DuplicateEmailException;
import com.social.gatherly.exception.UserNotFoundException;
import com.social.gatherly.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws DuplicateEmailException {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("유저가 없습니다."));


        //인증 전용 객체
         return new org.springframework.security.core.userdetails.User(
                 user.getEmail(),
                 user.getPassword(),
                 List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
         );

    }


}
