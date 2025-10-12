package com.example.NewsInsight.repository;

import com.example.NewsInsight.entity.BriefingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BriefingRepository extends JpaRepository<BriefingEntity, Integer> {

}
