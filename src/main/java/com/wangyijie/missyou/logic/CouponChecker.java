package com.wangyijie.missyou.logic;

import com.wangyijie.missyou.core.enumeration.CouponType;
import com.wangyijie.missyou.core.money.IMoneyDiscount;
import com.wangyijie.missyou.exception.http.ForbiddenException;
import com.wangyijie.missyou.exception.http.ParameterException;
import com.wangyijie.missyou.model.Coupon;
import com.wangyijie.missyou.model.UserCoupon;
import com.wangyijie.missyou.util.CommonUtil;

import java.math.BigDecimal;
import java.util.Date;

public class CouponChecker {
    private Coupon coupon;
    private UserCoupon userCoupon;

    private IMoneyDiscount iMoneyDiscount;

    public CouponChecker(Coupon coupon, UserCoupon userCoupon, IMoneyDiscount iMoneyDiscount) {
        this.coupon = coupon;
        this.userCoupon = userCoupon;
//        iMoneyDiscount =
    }

    public void isOk() {
        Date now = new Date();
        Boolean isInTimeline = CommonUtil.isInTimeLine(now, coupon.getStartTime(), coupon.getEndTime());
        if (!isInTimeline) {
            throw new ForbiddenException(40007);
        }
    }

    public void finalTotalPriceIsOk(BigDecimal orderFinalTotalPrice, BigDecimal serverTotalPrice) {
        BigDecimal serverFinalTotalPrice;
        switch (CouponType.toType(coupon.getType())) {
            case FULL_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(coupon.getMinus());
                break;
            case FULL_OFF:
                serverFinalTotalPrice = iMoneyDiscount.discount(serverTotalPrice, coupon.getRate());
                break;
            case NO_THRESHOLD_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(coupon.getMinus());
                if (serverFinalTotalPrice.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ForbiddenException(50008);
                }
                break;
            default:
                throw new ParameterException(40009);
        }
        int compare = serverFinalTotalPrice.compareTo(orderFinalTotalPrice);
        if (compare != 0) {
            throw new ForbiddenException(50008);
        }
    }

    public void canBeUsed() {

    }
}
