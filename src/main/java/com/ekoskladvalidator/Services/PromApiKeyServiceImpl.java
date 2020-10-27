package com.ekoskladvalidator.Services;

import com.ekoskladvalidator.Dao.PromApiKeyDao;
import com.ekoskladvalidator.Models.PromApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromApiKeyServiceImpl implements PromApiKeyService {

    @Autowired
    private PromApiKeyDao promApiKeyDao;

    @Override
    public Optional<PromApiKey> findById(Integer id) {
        return promApiKeyDao.findById(id);
    }

    @Override
    public List<PromApiKey> findAll() {
        return promApiKeyDao.findAll();
    }

    @Override
    public PromApiKey save(PromApiKey promApiKey) {
        return promApiKeyDao.save(promApiKey);
    }

    @Override
    public Optional<PromApiKey> findByApiKey(String apiKey) {
        return promApiKeyDao.findByApiKey(apiKey);
    }


}
