package com.example.NewsInsight.controller;

import com.example.NewsInsight.dto.UserDTO;
import com.example.NewsInsight.enums.ProviderType;
import com.example.NewsInsight.service.SubscriptionService;
import com.example.NewsInsight.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @GetMapping("/admin/users")
    public String userList(Model model) {
        List<UserDTO> userList = userService.getList();
        model.addAttribute("userList", userList);
        return "admin/user/userList";
    }

    @GetMapping("/admin/users/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        UserDTO user = userService.detail(id);
        model.addAttribute("user", user);
        return "admin/user/userDetails";
    }

    @PostMapping("/admin/users/{id}")
    public String update(@PathVariable("id") Integer id, UserDTO userDTO) {
        userService.update(id, userDTO);

        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/me")
    public String myPage(Model model, Principal principal) {
        String userid = principal.getName();

        UserDTO user = userService.getMyPage(userid);

        model.addAttribute("user", user);

        return "user/myPage/myPage";
    }

    @GetMapping("/users/me/update-nickname")
    public String updateNicknameForm(Model model, Principal principal) {
        UserDTO user = userService.findByUserid(principal.getName());

        model.addAttribute("user", user);

        return "user/myPage/updateNickname";
    }

    @PostMapping("/users/me/update-nickname")
    public String updateNickname(@RequestParam String nickname, Principal principal) {
        userService.updateNickname(principal.getName(), nickname);

        return "redirect:/users/me";
    }

    @GetMapping("/users/me/update-email")
    public String updateEmailForm(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        UserDTO user = userService.findByUserid(principal.getName());

        if (!ProviderType.LOCAL.equals(user.getProvider())) {
            redirectAttributes.addFlashAttribute("errorMessage", "소셜 로그인 사용자는 이메일을 변경할 수 없습니다.");
            return "redirect:/users/me";
        }

        model.addAttribute("user", user);
        return "user/myPage/updateEmail";
    }

    @PostMapping("/users/me/update-email")
    public String updateEmail(@RequestParam String email, Principal principal) {
        userService.updateEmail(principal.getName(), email);

        return "redirect:/users/me";
    }

    @GetMapping("/users/me/update-password")
    public String updatePasswordForm() {
        return "user/myPage/updatePassword";
    }

    @PostMapping("/users/me/update-password")
    public String updatePassword(Principal principal, RedirectAttributes redirectAttributes,
                                        @RequestParam String currentPassword,
                                        @RequestParam String newPassword,
                                        @RequestParam String confirmPassword) {
        try {
            userService.updatePassword(principal.getName(), currentPassword, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/users/me";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/me/update-password";
        }
    }

    @GetMapping("/users/me/delete-confirm")
    public String deleteConfirmForm() {
        return "user/myPage/deleteConfirm";
    }

    @PostMapping("/users/me/delete")
    public String deleteAccount(Principal principal, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        userService.deleteAccount(principal.getName());

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/";
    }
}
