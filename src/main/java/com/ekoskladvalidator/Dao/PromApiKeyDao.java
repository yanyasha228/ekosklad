package com.ekoskladvalidator.Dao;

import com.ekoskladvalidator.Models.PromApiKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromApiKeyDao extends JpaRepository<PromApiKey, Long> {

    Optional<PromApiKey> findByApiKey(String apiKey);

    Optional<PromApiKey> findByShopName(String name);

    void deleteById(Long id);

    Page<PromApiKey> findAll(Pageable pageable);

}
