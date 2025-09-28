package com.example.NewsInsight.service.impl;

import com.example.NewsInsight.dto.UserDTO;
import com.example.NewsInsight.entity.UserEntity;
import com.example.NewsInsight.mapper.UserMapper;
import com.example.NewsInsight.repository.UserRepository;
import com.example.NewsInsight.service.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getList() {
        List<UserEntity> entities = userRepository.findAll();
        return entities.stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDTO detail(Integer id) {
        UserEntity entity = userRepository.findById(id).orElseThrow(
                () -> new IllegalIdentifierException("해당 직원이 없습니다."));
        return userMapper.toDto(entity);
    }

}
