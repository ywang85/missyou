package com.wangyijie.missyou.service;

import com.wangyijie.missyou.model.Theme;
import com.wangyijie.missyou.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {
    @Autowired
    private ThemeRepository themeRepository;

    @Query("select t from Theme t where t.name in (:names)")
    public List<Theme> findByNames(@Param("name") List<String> names) {
        return themeRepository.findByNames(names);
    }

    public Optional<Theme> findByName(String name) {
        return themeRepository.findByName(name);
    }
}
