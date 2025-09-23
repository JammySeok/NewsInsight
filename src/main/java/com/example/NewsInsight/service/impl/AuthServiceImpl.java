package com.example.NewsInsight.service.impl;

import com.example.NewsInsight.dto.SignupDTO;
import com.example.NewsInsight.entity.UserEntity;
import com.example.NewsInsight.enums.ProviderType;
import com.example.NewsInsight.enums.UserRole;
import com.example.NewsInsight.mapper.UserMapper;
import com.example.NewsInsight.repository.UserRepository;
import com.example.NewsInsight.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public void signup(SignupDTO signupDTO) {

        if (!signupDTO.getPassword().equals(signupDTO.getConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if(userRepository.findByUserid(signupDTO.getUserid()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        UserEntity userEntity = userMapper.toEntityFromSignupDTO(signupDTO);
        userEntity.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        userEntity.setRole(UserRole.USER);
        userEntity.setProvider(ProviderType.LOCAL);

        userRepository.save(userEntity);
    }

}
