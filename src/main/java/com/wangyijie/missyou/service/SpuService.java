package com.wangyijie.missyou.service;

import com.wangyijie.missyou.model.Spu;
import com.wangyijie.missyou.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuService {
    @Autowired
    private SpuRepository repository;

    public Spu getSpu(Long id) {
        return repository.findOneById(id);
    }

    public Page<Spu> getLatestPagingSpu(Integer pageNum, Integer size) {
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by("createTime").descending());
        return repository.findAll(pageable);
    }

    public Page<Spu> getByCategory(Long cid, Boolean isRoot, Integer pageNum, Integer size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        if (isRoot) {
            return repository.findByRootCategoryId(cid, pageable);
        } else {
            return repository.findByCategoryId(cid, pageable);
        }
    }
}
