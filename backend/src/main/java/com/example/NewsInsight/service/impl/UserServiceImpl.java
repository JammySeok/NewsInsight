package com.example.NewsInsight.service.impl;

import com.example.NewsInsight.dto.SubscriptionDTO;
import com.example.NewsInsight.dto.UserDTO;
import com.example.NewsInsight.entity.UserEntity;
import com.example.NewsInsight.enums.ProviderType;
import com.example.NewsInsight.mapper.UserMapper;
import com.example.NewsInsight.repository.SubscriptionRepository;
import com.example.NewsInsight.repository.UserRepository;
import com.example.NewsInsight.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getList() {
        List<UserEntity> entities = userRepository.findAll();
        return entities.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO detail(Integer id) {
        UserEntity entity = userRepository.findById(id).orElseThrow(
                () -> new IllegalIdentifierException("해당 직원이 없습니다."));
        return userMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void update(Integer id, UserDTO userDTO) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalIdentifierException("해당 사용자를 찾을 수 없습니다. id: " + id));

        entity.setNickname(userDTO.getNickname());
        entity.setRole(userDTO.getRole());
        if (entity.getProvider() == ProviderType.LOCAL) {
            entity.setEmail(userDTO.getEmail());
        }

        userRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findByUserid(String userid) {
        UserEntity entity = userRepository.findByUserid(userid)
                .orElseThrow(() -> new IllegalIdentifierException("해당 사용자를 찾을 수 없습니다. userid: " + userid));

        return userMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void updateNickname(String userid, String nickname) {
        UserEntity user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new IllegalIdentifierException("사용자를 찾을 수 없습니다."));
        user.setNickname(nickname);
    }

    @Override
    @Transactional
    public void updateEmail(String userid, String email) {
        UserEntity user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new IllegalIdentifierException("사용자를 찾을 수 없습니다."));
        user.setEmail(email);
    }

    @Override
    @Transactional
    public void updatePassword(String userid, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }
        UserEntity user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new IllegalIdentifierException("사용자를 찾을 수 없습니다."));

        // 입력된 현재 비밀번호가 DB에 저장된 비밀번호와 일치하는지 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
        }

        // 새 비밀번호를 암호화하여 저장
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Override
    @Transactional
    public void deleteAccount(String userid) {
        UserEntity user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new IllegalIdentifierException("사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getMyPage(String userid) { // 메서드 이름은 실제 프로젝트에 맞게 수정하세요.
        UserEntity userEntity = userRepository.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO userDTO = userMapper.toDto(userEntity);

        subscriptionRepository.findByUserAndIsActiveTrue(userEntity)
                .ifPresent(subscriptionEntity -> {
                    SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
                    subscriptionDTO.setId(subscriptionEntity.getId());
                    subscriptionDTO.setSubscriptionType(subscriptionEntity.getSubscriptionType());
                    subscriptionDTO.setStartDate(subscriptionEntity.getStartDate());
                    subscriptionDTO.setEndDate(subscriptionEntity.getEndDate());
                    subscriptionDTO.setActive(subscriptionEntity.getIsActive());
                    userDTO.setSubscription(subscriptionDTO);
                });

        return userDTO;
    }
}
