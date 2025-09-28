package com.example.NewsInsight.controller;

import com.example.NewsInsight.dto.UserDTO;
import com.example.NewsInsight.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public String userList(Model model) {
        List<UserDTO> userList = userService.getList();
        model.addAttribute("userList", userList);
        return "user/userList";
    }

    @GetMapping("/admin/users/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        UserDTO user = userService.detail(id);
        model.addAttribute("user", user);
        return "user/userDetails";
    }

}
