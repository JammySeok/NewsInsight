package com.example.NewsInsight.controller;

import com.example.NewsInsight.dto.LoginDTO;
import com.example.NewsInsight.dto.SignupDTO;
import com.example.NewsInsight.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupDTO", new SignupDTO());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String addUser(@Valid @ModelAttribute SignupDTO signupDTO, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            return "auth/signup";
        }

        if(!signupDTO.getPassword().equals(signupDTO.getConfirm())) {
            bindingResult.rejectValue("confirm", "password.no_match", "비밀번호가 일치하지 않습다.");
            return "auth/signup";
        }

        try {
            authService.signup(signupDTO);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("userid", "userid.duplicate", e.getMessage());
            return "auth/signup";
        }

        return "redirect:/auth/login";
    }


}
