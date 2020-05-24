package com.wangyijie.missyou.service;

import com.wangyijie.missyou.model.Banner;
import com.wangyijie.missyou.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerRepository repository;

    public Banner getByName(String name) {
        return repository.findOneByName(name);
    }
}
