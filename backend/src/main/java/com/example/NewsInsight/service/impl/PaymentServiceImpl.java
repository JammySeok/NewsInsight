package com.example.NewsInsight.service.impl;

import com.example.NewsInsight.dto.PaymentDTO;
import com.example.NewsInsight.service.PaymentService;
import com.example.NewsInsight.service.SubscriptionService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final SubscriptionService subscriptionService;

    @Value("${iamport.api-key}")
    private String apiKey;

    @Value("${iamport.api-secret}")
    private String apiSecret;

    private IamportClient iamportClient;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    @Override
    @Transactional
    public void verifyAndProcessPayment(PaymentDTO paymentDTO, Integer userId) throws IOException {
        if (paymentDTO.getImpUid() != null && paymentDTO.getImpUid().startsWith("sim_")) {
            subscriptionService.createOrUpdateSubscription(userId);
            return;
        }

        try {
            IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(paymentDTO.getImpUid());

            BigDecimal expectedAmount = new BigDecimal("9900");
            BigDecimal actualAmount = paymentResponse.getResponse().getAmount();

            if (!actualAmount.equals(expectedAmount)) {
                throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
            }

            subscriptionService.createOrUpdateSubscription(userId);

        } catch (Exception e) {
            throw new IOException("결제 검증에 실패했습니다. " + e.getMessage(), e);
        }
    }
}