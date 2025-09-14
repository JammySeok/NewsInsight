package com.example.NewsInsight.entity;

import jakarta.persistence.*;

@Entity
@Table(name="category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
}
