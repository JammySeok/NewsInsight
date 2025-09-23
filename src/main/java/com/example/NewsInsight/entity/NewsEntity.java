package com.example.NewsInsight.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="news")
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String originalUrl;
    private String title;
    private String content;
    private String content_ptr;
    private String summary;
    private String source;
    private String hash;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime publishedAt;
    private LocalDateTime summarizedAt;
    private LocalDateTime taggedAt;
}
