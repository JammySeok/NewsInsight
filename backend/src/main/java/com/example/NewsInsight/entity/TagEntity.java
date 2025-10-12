package com.example.NewsInsight.entity;

import jakarta.persistence.*;

@Entity
@Table(name="tag")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
