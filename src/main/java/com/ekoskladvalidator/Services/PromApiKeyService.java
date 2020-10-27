package com.ekoskladvalidator.Services;

import com.ekoskladvalidator.Models.PromApiKey;

import java.util.List;
import java.util.Optional;

public interface PromApiKeyService {

    Optional<PromApiKey> findById(Integer id);

    List<PromApiKey> findAll();

    PromApiKey save(PromApiKey promApiKey);

    Optional<PromApiKey> findByApiKey(String apiKey);

}
