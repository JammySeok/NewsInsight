package com.example.NewsInsight.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {
    private String impUid;      // 아임포트 결제 고유 번호 (rsp.imp_uid)
    private String merchantUid; // 우리 쇼핑몰의 주문 번호 (rsp.merchant_uid)
}