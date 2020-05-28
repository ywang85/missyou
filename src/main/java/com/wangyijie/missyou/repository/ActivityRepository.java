package com.wangyijie.missyou.repository;

import com.wangyijie.missyou.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByName(String name);

    Activity findByCouponListId(Long couponId);
}
