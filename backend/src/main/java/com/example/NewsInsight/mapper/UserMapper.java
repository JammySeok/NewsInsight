package com.example.NewsInsight.mapper;

import com.example.NewsInsight.dto.SignupDTO;
import com.example.NewsInsight.dto.UserDTO;
import com.example.NewsInsight.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // UserEntity -> UserDTO 변환 (데이터 조회 시 사용)
    UserDTO toDto(UserEntity userEntity);

    // UserDTO -> UserEntity 변환 (데이터 수정 시 사용)
    UserEntity toEntity(UserDTO userDTO);

    // SignupDTO -> UserEntity 변환 (회원가입 시 사용)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // 비밀번호는 서비스에서 별도 처리
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "provider", ignore = true)
    UserEntity toEntityFromSignupDTO(SignupDTO signupDTO);
}
