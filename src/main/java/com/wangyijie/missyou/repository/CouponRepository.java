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
}
