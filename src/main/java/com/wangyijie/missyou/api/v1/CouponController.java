package com.wangyijie.missyou.api.v1;

import com.wangyijie.missyou.core.LocalUser;
import com.wangyijie.missyou.core.UnifyResponse;
import com.wangyijie.missyou.core.enumeration.CouponStatus;
import com.wangyijie.missyou.core.interceptors.ScopeLevel;
import com.wangyijie.missyou.exception.http.ParameterException;
import com.wangyijie.missyou.model.Coupon;
import com.wangyijie.missyou.model.User;
import com.wangyijie.missyou.service.CouponService;
import com.wangyijie.missyou.vo.CouponCategoryVO;
import com.wangyijie.missyou.vo.CouponPureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("coupon")
@RestController
public class CouponController {
    @Autowired
    private CouponService couponService;

    @GetMapping("/by/category/{cid}")
    // 优惠券与2级分类挂钩
    public List<CouponPureVO> getCouponListByCategory(@PathVariable Long cid) {
        List<Coupon> coupons = couponService.getByCategory(cid);
        if (coupons.isEmpty()) {
            return Collections.emptyList();
        }
        return CouponPureVO.getList(coupons);
    }

    @GetMapping("/whole_store")
    public List<CouponPureVO> getWholeStoreCouponList() {
        List<Coupon> coupons = couponService.getWholeStoreCoupons();
        if (coupons.isEmpty()) {
            return Collections.emptyList();
        }
        return CouponPureVO.getList(coupons);
    }

    @ScopeLevel
    @PostMapping("/collect/{id}")
    public void collectCoupon(@PathVariable Long id) {
        Long uid = LocalUser.getUser().getId();
        couponService.collectOneCoupon(uid, id);
        UnifyResponse.createSuccess(0);
    }

    @ScopeLevel
    @GetMapping("/myself/by/status/{status}")
    public List<CouponPureVO> getMyCouponByStatus(@PathVariable Integer status) {
        Long uid = LocalUser.getUser().getId();
        List<Coupon> couponList;

        switch (CouponStatus.toType(status)) {
            case AVAILABLE:
                couponList = couponService.getMyAvailableCoupons(uid);
                break;
            case USED:
                couponList = couponService.getMyUsedCoupons(uid);
                break;
            case EXPIRED:
                couponList = couponService.getMyExpiredCoupons(uid);
                break;
            default:
                throw new ParameterException(40001);
        }
        return CouponPureVO.getList(couponList);
    }

    @ScopeLevel
    @GetMapping("/myself/available/with_category")
    public List<CouponCategoryVO> getUserCouponWithCategory() {
        User user = LocalUser.getUser();
        List<Coupon> coupons = couponService.getMyAvailableCoupons(user.getId());
        if (coupons.isEmpty()) {
            return Collections.emptyList();
        }
        return coupons.stream().map(CouponCategoryVO::new).collect(Collectors.toList());
    }
}
