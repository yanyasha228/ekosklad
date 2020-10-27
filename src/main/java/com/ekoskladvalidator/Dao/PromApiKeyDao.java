package com.ekoskladvalidator.Dao;

import com.ekoskladvalidator.Models.PromApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromApiKeyDao extends JpaRepository<PromApiKey, Integer> {
    Optional<PromApiKey> findByApiKey(String apiKey);

}
