package com.wangyijie.missyou.repository;

import com.wangyijie.missyou.model.Spu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpuRepository extends JpaRepository<Spu, Long> {
    Spu findOneById(Long id);

    Page<Spu> findByCategoryId(Long cid, Pageable pageable);

    Page<Spu> findByRootCategoryId(Long cid, Pageable pageable);
}
