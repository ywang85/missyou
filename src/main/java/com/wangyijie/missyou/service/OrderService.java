package com.wangyijie.missyou.service;

import com.wangyijie.missyou.core.money.IMoneyDiscount;
import com.wangyijie.missyou.dto.OrderDTO;
import com.wangyijie.missyou.dto.SkuInfoDTO;
import com.wangyijie.missyou.exception.http.NotFoundException;
import com.wangyijie.missyou.exception.http.ParameterException;
import com.wangyijie.missyou.logic.CouponChecker;
import com.wangyijie.missyou.model.Coupon;
import com.wangyijie.missyou.model.Sku;
import com.wangyijie.missyou.model.UserCoupon;
import com.wangyijie.missyou.repository.CouponRepository;
import com.wangyijie.missyou.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private SkuService skuService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private IMoneyDiscount iMoneyDiscount;

    public void isOk(Long uid, OrderDTO orderDTO) {
        if (orderDTO.getFinalTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParameterException(50011);
        }
        List<Long> skuIdList = orderDTO.getSkuInfoList().stream().map(SkuInfoDTO::getId).collect(Collectors.toList());
        List<Sku> skuList = skuService.getSkuListByIds(skuIdList);

        Long couponId = orderDTO.getCouponId();

        CouponChecker couponChecker;
        if (couponId != null) {
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException(40004));
            UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndCouponId(uid, couponId);
            if (userCoupon == null) {
                throw new NotFoundException(50006);
            }
            couponChecker = new CouponChecker(coupon, iMoneyDiscount);
        }
    }
}
