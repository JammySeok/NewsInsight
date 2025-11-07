package com.example.NewsInsight.controller;

import com.example.NewsInsight.dto.SignupDTO;
import com.example.NewsInsight.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth") // API 경로는 /api 로 구분하는 것이 좋습니다.
public class AuthApiController {

    private final AuthService authService;

    /**
     * React에서 회원가입 요청을 처리하는 API
     * @param signupDTO React에서 보낸 JSON 데이터를 받음
     * @param bindingResult @Valid 검증 결과
     * @return 성공 시 200 OK, 실패 시 400 Bad Request
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody SignupDTO signupDTO,
            BindingResult bindingResult) {

        // 1. @Valid 검증 (DTO의 @NotBlank, @Email 등)
        if (bindingResult.hasErrors()) {
            // 검증 실패 시 에러 메시지를 Map에 담아 반환
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // 2. 비밀번호 확인 로직
        if (!signupDTO.getPassword().equals(signupDTO.getConfirm())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("confirm", "비밀번호가 일치하지 않습니다."));
        }

        // 3. 서비스 호출 (아이디 중복 검사 등)
        try {
            authService.signup(signupDTO);
        } catch (IllegalArgumentException e) {
            // 아이디 중복 등 서비스 단에서 발생한 오류 처리
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("userid", e.getMessage()));
        }

        // 4. 성공
        return ResponseEntity.ok().body("회원가입이 성공적으로 완료되었습니다.");
    }
}