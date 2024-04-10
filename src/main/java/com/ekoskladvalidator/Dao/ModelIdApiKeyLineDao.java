package com.ekoskladvalidator.Dao;

import com.ekoskladvalidator.Models.ModelIdApiKeyLine;
import com.ekoskladvalidator.Models.PromApiKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelIdApiKeyLineDao extends JpaRepository<ModelIdApiKeyLine, Long> {
    Optional<ModelIdApiKeyLine> findByProductApiId(Long productApiId);

    Optional<ModelIdApiKeyLine> findByProductApiIdAndPromApiKey(Long productApiId, PromApiKey promApiKey);

    Optional<ModelIdApiKeyLine> findByProductIdAndPromApiKey(Long product, PromApiKey promApiKey);

    Page<ModelIdApiKeyLine> findAll(Pageable pageable);
}
