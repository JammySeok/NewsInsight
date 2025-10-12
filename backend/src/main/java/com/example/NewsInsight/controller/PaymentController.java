package com.example.NewsInsight.controller;

import com.example.NewsInsight.dto.PaymentDTO;
import com.example.NewsInsight.security.CustomUserDetails;
import com.example.NewsInsight.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payment/verify")
    @ResponseBody
    public ResponseEntity<?> verifyPayment(
            @RequestBody PaymentDTO paymentDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer currentUserId = userDetails.getId();

            paymentService.verifyAndProcessPayment(paymentDTO, currentUserId);

            return ResponseEntity.ok("구독 처리가 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

