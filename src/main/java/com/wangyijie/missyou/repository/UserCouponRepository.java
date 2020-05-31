package com.wangyijie.missyou.repository;

import com.wangyijie.missyou.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    UserCoupon findFirstByUserIdAndCouponId(Long uid, Long couponId);

    @Modifying
    @Query("")
    int writeOff(Long couponId, Long oid, Long uid);
}
