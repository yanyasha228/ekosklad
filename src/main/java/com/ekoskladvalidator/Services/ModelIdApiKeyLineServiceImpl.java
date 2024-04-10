package com.ekoskladvalidator.Services;

import com.ekoskladvalidator.Dao.ModelIdApiKeyLineDao;
import com.ekoskladvalidator.Models.ModelIdApiKeyLine;
import com.ekoskladvalidator.Models.PromApiKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModelIdApiKeyLineServiceImpl implements ModelIdApiKeyLineService {

    private final ModelIdApiKeyLineDao modelIdApiKeyLineDao;

    public ModelIdApiKeyLineServiceImpl(ModelIdApiKeyLineDao modelIdApiKeyLineDao) {
        this.modelIdApiKeyLineDao = modelIdApiKeyLineDao;
    }

    @Override
    public Optional<ModelIdApiKeyLine> findById(Long id) {
        return modelIdApiKeyLineDao.findById(id);
    }

    @Override
    public Optional<ModelIdApiKeyLine> findByProductApiId(Long id) {
        return modelIdApiKeyLineDao.findByProductApiId(id);
    }

    @Override
    public List<ModelIdApiKeyLine> findAll() {
        return modelIdApiKeyLineDao.findAll();
    }

    @Override
    public ModelIdApiKeyLine save(ModelIdApiKeyLine modelIdApiKeyLine) {
        return modelIdApiKeyLineDao.save(modelIdApiKeyLine);
    }

    @Override
    public Optional<ModelIdApiKeyLine> findByProductApiIdAndPromApiKey(Long productApiId, PromApiKey promApiKey) {
        return modelIdApiKeyLineDao.findByProductApiIdAndPromApiKey(productApiId, promApiKey);
    }

    @Override
    public Optional<ModelIdApiKeyLine> findByProductIdAndPromApiKey(Long id, PromApiKey promApiKey) {
        return modelIdApiKeyLineDao.findByProductIdAndPromApiKey(id, promApiKey);
    }

    @Override
    public Page<ModelIdApiKeyLine> findAll(Pageable pageable) {
        return modelIdApiKeyLineDao.findAll(pageable);
    }
}
