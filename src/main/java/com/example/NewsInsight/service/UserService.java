package com.example.NewsInsight.service;

import com.example.NewsInsight.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getList();
    UserDTO detail(Integer id);
    void update(Integer id, UserDTO userDTO);
    void delete(Integer id);

    UserDTO findByUserid(String userid);
    void updateNickname(String name, String nickname);
    void updateEmail(String name, String email);
    void updatePassword(String name, String currentPassword, String newPassword, String confirmPassword);
    void deleteAccount(String name);
}
