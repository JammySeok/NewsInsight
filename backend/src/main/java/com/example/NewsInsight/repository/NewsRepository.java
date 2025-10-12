package com.example.NewsInsight.repository;

import com.example.NewsInsight.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository <NewsEntity, Integer> {
}
