package com.wangyijie.missyou.repository;

import com.wangyijie.missyou.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    UserCoupon findFirstByUserIdAndCouponIdAndStatus(Long uid, Long couponId, int status);

    @Modifying
    @Query("update UserCoupon uc \n" +
            "set uc.status = 2, uc.orderId = :oid \n" +
            "where uc.userId = :uid \n" +
            "and uc.couponId = :couponId\n" +
            "and uc.status = 1\n" +
            "and uc.orderId is null")
    int writeOff(Long couponId, Long oid, Long uid);

    UserCoupon findFirstByUserIdAndCouponId(Long uid, Long couponId);

    @Modifying
    @Query("update UserCoupon uc set uc.status = 1, uc.orderId = null " +
            "where uc.couponId = :couponId and uc.userId = :uid" +
            " and uc.orderId is not null and uc.status = 2")
    int returnBack(Long couponId, Long uid);
}
