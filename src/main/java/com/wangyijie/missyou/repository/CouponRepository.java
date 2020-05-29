package com.wangyijie.missyou.repository;

import com.wangyijie.missyou.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    /**
     * SELECT * FROM coupon
     * JOIN coupon_category
     * ON coupon.id = coupon_category.coupon_id
     * JOIN category
     * ON coupon_category.category_id = category.id
     * WHERE category.id = 1
     */
    @Query("select c from Coupon c join c.categoryList ca join Activity a on a.id = c.activityId " +
            "where ca.id = :cid and a.endTime > :now and a.startTime < :now")
    List<Coupon> findByCategory(Long cid, Date now);

    @Query("select c from Coupon c join Activity a on c.activityId = a.id " +
            "where c.wholeStore = :isWholeStore and a.startTime < :now and a.endTime > :now")
    List<Coupon> findByWholeStore(Boolean isWholeStore, Date now);

    @Query("select c from Coupon c join UserCoupon uc on uc.couponId = c.id\n" +
            "join User u on u.id = uc.userId\n" +
            "where uc.status = 1 and u.id = :uid and c.startTime < :now and c.endTime > :now\n" +
            "and uc.orderId is null ")
    List<Coupon> findMyAvailable(Long uid, Date now);

    @Query("select c from Coupon c join UserCoupon uc on uc.couponId = c.id\n" +
            "join User u on u.id = uc.userId\n" +
            "where uc.status = 2 and u.id = :uid and c.startTime < :now and c.endTime > :now\n" +
            "and uc.orderId is not null ")
    List<Coupon> findMyUsed(Long uid, Date now);

    @Query("select c from Coupon c join UserCoupon uc on uc.couponId = c.id\n" +
            "join User u on u.id = uc.userId\n" +
            "where uc.status <> 2 and u.id = :uid and c.endTime < :now\n" +
            "and uc.orderId is null ")
    List<Coupon> findMyExpired(Long uid, Date now);
}
