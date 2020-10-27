package com.ekoskladvalidator.Dao;

import com.ekoskladvalidator.Models.ModelIdApiKeyLine;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Models.PromApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelIdApiKeyLineDao extends JpaRepository<ModelIdApiKeyLine, Integer> {
    Optional<ModelIdApiKeyLine> findByProductApiId(Integer productApiId);
    Optional<ModelIdApiKeyLine> findByProductApiIdAndPromApiKey(Integer productApiId , PromApiKey promApiKey);

    Optional<ModelIdApiKeyLine> findByProductIdAndPromApiKey(Integer product, PromApiKey promApiKey);
}
