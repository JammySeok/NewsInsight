package com.example.NewsInsight.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="insight")
public class InsightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    private String domain;
    private LocalDateTime createAt;

}
