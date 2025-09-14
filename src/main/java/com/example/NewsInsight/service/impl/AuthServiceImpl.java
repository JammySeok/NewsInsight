package com.example.NewsInsight.service.impl;

import com.example.NewsInsight.dto.SignupDTO;
import com.example.NewsInsight.entity.UserEntity;
import com.example.NewsInsight.repository.UserRepository;
import com.example.NewsInsight.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void signup(SignupDTO signupDTO) {
        if(userRepository.findByUserid(signupDTO.getUserid()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserid(signupDTO.getUserid());
        userEntity.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        userEntity.setEmail(signupDTO.getEmail());
        userEntity.setNickname(signupDTO.getNickname());

        userRepository.save(userEntity);
    }

}
