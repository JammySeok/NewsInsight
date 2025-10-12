package com.example.NewsInsight.repository;

import com.example.NewsInsight.entity.InsightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsightRepository extends JpaRepository<InsightEntity, Integer> {
}
