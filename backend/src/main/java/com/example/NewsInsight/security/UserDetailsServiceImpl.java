package com.example.NewsInsight.security;

import com.example.NewsInsight.entity.UserEntity;
import com.example.NewsInsight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾지 못함" + userid));

        return new CustomUserDetails(userEntity);
    }
}
