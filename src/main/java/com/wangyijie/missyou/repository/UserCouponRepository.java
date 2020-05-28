package com.wangyijie.missyou.repository;

import com.wangyijie.missyou.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    UserCoupon findFirstByUserIdAndCouponId(Long uid, Long couponId);
}
