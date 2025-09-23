package com.example.NewsInsight.mapper;

import com.example.NewsInsight.dto.SignupDTO;
import com.example.NewsInsight.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T14:03:52+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toEntityFromSignupDTO(SignupDTO signupDTO) {
        if ( signupDTO == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setUserid( signupDTO.getUserid() );
        userEntity.setEmail( signupDTO.getEmail() );
        userEntity.setNickname( signupDTO.getNickname() );

        return userEntity;
    }
}
