package com.example.NewsInsight.mapper;

import com.example.NewsInsight.dto.SignupDTO;
import com.example.NewsInsight.dto.UserDTO;
import com.example.NewsInsight.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-07T13:26:57+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( userEntity.getId() );
        userDTO.setUserid( userEntity.getUserid() );
        userDTO.setEmail( userEntity.getEmail() );
        userDTO.setNickname( userEntity.getNickname() );
        userDTO.setProvider( userEntity.getProvider() );
        userDTO.setRole( userEntity.getRole() );
        userDTO.setCreateAt( userEntity.getCreateAt() );
        userDTO.setUpdateAt( userEntity.getUpdateAt() );

        return userDTO;
    }

    @Override
    public UserEntity toEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( userDTO.getId() );
        userEntity.setUserid( userDTO.getUserid() );
        userEntity.setEmail( userDTO.getEmail() );
        userEntity.setNickname( userDTO.getNickname() );
        userEntity.setProvider( userDTO.getProvider() );
        userEntity.setRole( userDTO.getRole() );
        userEntity.setCreateAt( userDTO.getCreateAt() );
        userEntity.setUpdateAt( userDTO.getUpdateAt() );

        return userEntity;
    }

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
