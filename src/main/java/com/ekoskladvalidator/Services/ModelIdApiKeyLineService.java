package com.ekoskladvalidator.Services;

import com.ekoskladvalidator.Models.ModelIdApiKeyLine;
import com.ekoskladvalidator.Models.PromApiKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ModelIdApiKeyLineService {

    Optional<ModelIdApiKeyLine> findById(Long id);

    Optional<ModelIdApiKeyLine> findByProductApiId(Long id);

    List<ModelIdApiKeyLine> findAll();

    ModelIdApiKeyLine save(ModelIdApiKeyLine modelIdApiKeyLine);

    Optional<ModelIdApiKeyLine> findByProductApiIdAndPromApiKey(Long productApiId, PromApiKey promApiKey);

    Optional<ModelIdApiKeyLine> findByProductIdAndPromApiKey(Long product, PromApiKey promApiKey);

    Page<ModelIdApiKeyLine> findAll(Pageable pageable);

}
