package com.ekoskladvalidator.Services;

import com.ekoskladvalidator.Models.PromApiKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PromApiKeyService {

    Optional<PromApiKey> findById(Long id);

    List<PromApiKey> findAll();

    PromApiKey save(PromApiKey promApiKey);

    Optional<PromApiKey> findByApiKey(String apiKey);

    Optional<PromApiKey> findByShopName(String name);

    void delete(Long id);

    Page<PromApiKey> findAll(Pageable pageable);

}
