package com.example.NewsInsight.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="briefing")
public class BriefingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String section;
    private String itemsJson;
    private LocalDate briefDate;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
