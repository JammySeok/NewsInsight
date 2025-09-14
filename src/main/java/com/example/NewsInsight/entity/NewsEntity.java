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
    private String summary;
    private String source;
    private LocalDateTime publishedAt;
    private LocalDateTime createAt;

}
