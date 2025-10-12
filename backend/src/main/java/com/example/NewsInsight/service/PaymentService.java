package com.example.NewsInsight.service;

import com.example.NewsInsight.dto.PaymentDTO;

import java.io.IOException;

public interface PaymentService {
    void verifyAndProcessPayment(PaymentDTO paymentDTO, Integer userId) throws IOException;
}
