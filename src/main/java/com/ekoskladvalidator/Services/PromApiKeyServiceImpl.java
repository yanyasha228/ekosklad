package com.ekoskladvalidator.Services;

import com.ekoskladvalidator.Dao.PromApiKeyDao;
import com.ekoskladvalidator.Models.PromApiKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromApiKeyServiceImpl implements PromApiKeyService {

    private final PromApiKeyDao promApiKeyDao;

    public PromApiKeyServiceImpl(PromApiKeyDao promApiKeyDao) {
        this.promApiKeyDao = promApiKeyDao;
    }

    @Override
    public Optional<PromApiKey> findById(Long id) {
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

    @Override
    public Optional<PromApiKey> findByShopName(String name) {
        return promApiKeyDao.findByShopName(name);
    }

    @Override
    public void delete(Long id) {
        promApiKeyDao.deleteById(id);
    }

    @Override
    public Page<PromApiKey> findAll(Pageable pageable) {
        return promApiKeyDao.findAll(pageable);
    }


}
