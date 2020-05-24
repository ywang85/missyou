package com.wangyijie.missyou.service;

import com.wangyijie.missyou.model.Category;
import com.wangyijie.missyou.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public Map<Integer, List<Category>> getAll() {
        List<Category> root = repository.findAllByIsRootOrderByIndexAsc(true);
        List<Category> subs = repository.findAllByIsRootOrderByIndexAsc(false);
        Map<Integer, List<Category>> categories = new HashMap<>();
        categories.put(1, root);
        categories.put(2, subs);
        return categories;
    }
}
