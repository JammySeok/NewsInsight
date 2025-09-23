package com.example.NewsInsight.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="event_audit")
public class EventAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String eventType;
    private String payloadJson;
    private String actor;
    private LocalDateTime createAt;
}
