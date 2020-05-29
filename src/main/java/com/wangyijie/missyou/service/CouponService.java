package com.wangyijie.missyou.service;

import com.wangyijie.missyou.core.enumeration.CouponStatus;
import com.wangyijie.missyou.exception.http.NotFoundException;
import com.wangyijie.missyou.exception.http.ParameterException;
import com.wangyijie.missyou.model.Activity;
import com.wangyijie.missyou.model.Coupon;
import com.wangyijie.missyou.model.UserCoupon;
import com.wangyijie.missyou.repository.ActivityRepository;
import com.wangyijie.missyou.repository.CouponRepository;
import com.wangyijie.missyou.repository.UserCouponRepository;
import com.wangyijie.missyou.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

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

    public void collectOneCoupon(Long uid, Long couponId) {
        // 判断有没有这张优惠券
        couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException(40003));
        // 根据优惠券找到活动
        Activity activity = activityRepository.findByCouponListId(couponId);
        if (activity == null) {
            throw new NotFoundException(40010);
        }
        // 判断活动是否过期
        Date now = new Date();
        Boolean isIn = CommonUtil.isInTimeLine(now, activity.getStartTime(), activity.getEndTime());
        if (!isIn) {
            throw new ParameterException(40005);
        }
        // 通过用户id和优惠券id查询该用户是否领过该优惠券
        UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndCouponId(uid, couponId);
        if (userCoupon != null) {
            throw new ParameterException(40006);
        }
        UserCoupon userCouponNew = new UserCoupon();
        userCouponNew.setUserId(uid);
        userCouponNew.setCouponId(couponId);
        userCouponNew.setStatus(CouponStatus.AVAILABLE.getValue());
        userCouponNew.setCreateTime(now);
        userCouponRepository.save(userCouponNew);
    }

    public List<Coupon> getMyAvailableCoupons(Long uid) {
        Date now = new Date();
        return couponRepository.findMyAvailable(uid, now);
    }

    public List<Coupon> getMyUsedCoupons(Long uid) {
        Date now = new Date();
        return couponRepository.findMyUsed(uid, now);
    }

    public List<Coupon> getMyExpiredCoupons(Long uid) {
        Date now = new Date();
        return couponRepository.findMyExpired(uid, now);
    }
}
