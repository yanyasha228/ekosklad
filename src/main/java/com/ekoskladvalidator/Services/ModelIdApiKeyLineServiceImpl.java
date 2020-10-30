package com.ekoskladvalidator.Services;

import com.ekoskladvalidator.Dao.ModelIdApiKeyLineDao;
import com.ekoskladvalidator.Models.ModelIdApiKeyLine;
import com.ekoskladvalidator.Models.PromApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModelIdApiKeyLineServiceImpl implements ModelIdApiKeyLineService {

    @Autowired
    private ModelIdApiKeyLineDao modelIdApiKeyLineDao;

    @Override
    public Optional<ModelIdApiKeyLine> findById(Integer id) {
        return modelIdApiKeyLineDao.findById(id);
    }

    @Override
    public Optional<ModelIdApiKeyLine> findByProductApiId(Integer id) {
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
    public Optional<ModelIdApiKeyLine> findByProductApiIdAndPromApiKey(Integer productApiId, PromApiKey promApiKey) {
        return modelIdApiKeyLineDao.findByProductApiIdAndPromApiKey(productApiId, promApiKey);
    }

    @Override
    public Optional<ModelIdApiKeyLine> findByProductIdAndPromApiKey(Integer id, PromApiKey promApiKey) {
        return modelIdApiKeyLineDao.findByProductIdAndPromApiKey(id, promApiKey);
    }

    @Override
    public Page<ModelIdApiKeyLine> findAll(Pageable pageable) {
        return modelIdApiKeyLineDao.findAll(pageable);
    }
}
