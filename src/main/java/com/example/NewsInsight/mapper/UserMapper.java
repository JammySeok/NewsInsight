package com.example.NewsInsight.mapper;

import com.example.NewsInsight.dto.SignupDTO;
import com.example.NewsInsight.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // SignupDTO -> UserEntity 변환
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // 비밀번호는 서비스에서 별도 처리
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "provider", ignore = true)
    UserEntity toEntityFromSignupDTO(SignupDTO signupDTO);
}
