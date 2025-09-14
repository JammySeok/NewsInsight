package com.example.NewsInsight.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="subscription")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    private UserEntity user;
    private String subscriptionType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;

}
