package com.example.NewsInsight.controller;

import com.example.NewsInsight.dto.UserDTO;
import com.example.NewsInsight.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class IndexApiController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInfo(Principal principal) {
        // 1. principal이 null이면 비로그인 상태
        if (principal == null) {
            // 401 Unauthorized 응답
            return ResponseEntity.status(401).build();
        }

        // 2. 로그인 상태면 principal.getName()으로 userid를 가져옴
        String userid = principal.getName();
        UserDTO user = userService.getMyPage(userid); // UserServiceImpl의 getMyPage 재활용

        // 3. 사용자 정보를 JSON으로 반환
        return ResponseEntity.ok(user);
    }

    /**
     * React에서 뉴스 요약을 요청하는 API (임시)
     * @param request React가 보낸 { "article": "..." } JSON
     * @return 요약 결과 { "summary": "..." } JSON
     */
    @PostMapping("/summarize")
    public ResponseEntity<Map<String, String>> summarizeArticle(
            @RequestBody SummarizeRequest request) {

        // TODO: (중요) 여기에 실제 AI 요약 서비스 로직을 연결해야 합니다.
        // 지금은 React가 API를 호출할 수 있도록 임시 응답을 만듭니다.
        String summary;
        if (request.getArticle() == null || request.getArticle().isBlank()) {
            summary = "요약할 내용이 없습니다.";
        } else {
            summary = "AI가 기사를 요약했습니다: "
                    + request.getArticle().substring(0, Math.min(request.getArticle().length(), 50))
                    + "...";
        }

        // React가 기대하는 { "summary": "..." } 형태로 반환
        return ResponseEntity.ok(Map.of("summary", summary));
    }

    /**
     * /api/summarize 요청 본문을 받기 위한 내부 DTO
     */
    @Data
    static class SummarizeRequest {
        private String article;
    }
}