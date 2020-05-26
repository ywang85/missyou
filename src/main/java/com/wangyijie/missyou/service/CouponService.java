package com.wangyijie.missyou.service;

import com.wangyijie.missyou.model.Coupon;
import com.wangyijie.missyou.repository.ActivityRepository;
import com.wangyijie.missyou.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public List<Coupon> getByCategory(Long cid) {
        Date now = new Date();
//        System.out.println("时间:" + now);
//        System.out.println(new Timestamp(System.currentTimeMillis()).getTime());
        return couponRepository.findByCategory(cid, now);
    }

    public List<Coupon> getWholeStoreCoupons() {
        Date now = new Date();
        return couponRepository.findByWholeStore(true, now);
    }
}
