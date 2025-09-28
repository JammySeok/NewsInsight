package com.example.NewsInsight.service;

import com.example.NewsInsight.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getList();
    UserDTO detail(Integer id);
}
