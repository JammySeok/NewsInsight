package com.example.NewsInsight.repository;

import com.example.NewsInsight.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    Optional<UserEntity> findByUserid(String userid);
}
