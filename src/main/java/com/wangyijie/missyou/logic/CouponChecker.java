package com.wangyijie.missyou.logic;

import com.wangyijie.missyou.bo.SkuOrderBO;
import com.wangyijie.missyou.core.enumeration.CouponType;
import com.wangyijie.missyou.core.money.IMoneyDiscount;
import com.wangyijie.missyou.exception.http.ForbiddenException;
import com.wangyijie.missyou.exception.http.ParameterException;
import com.wangyijie.missyou.model.Category;
import com.wangyijie.missyou.model.Coupon;
import com.wangyijie.missyou.util.CommonUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CouponChecker {
    private Coupon coupon;

    private IMoneyDiscount iMoneyDiscount;

    public CouponChecker(Coupon coupon, IMoneyDiscount iMoneyDiscount) {
        this.coupon = coupon;
        this.iMoneyDiscount = iMoneyDiscount;
    }

    // 检查优惠券是否过期
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

    // sku价格
    // sku数量
    // sku分类id
    // coupon category
    public void canBeUsed(List<SkuOrderBO> skuOrderBOList, BigDecimal serverTotalPrice) {
        BigDecimal orderCategoryPrice;
        // 券没有分类的限制
        if (coupon.getWholeStore()) {
            orderCategoryPrice = serverTotalPrice;
        } else {
            List<Long> cidList = coupon.getCategoryList().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            orderCategoryPrice = getSumByCategoryList(skuOrderBOList, cidList);
        }
        couponCanBeUsed(orderCategoryPrice);
    }

    private void couponCanBeUsed(BigDecimal orderCategoryPrice) {
        switch (CouponType.toType(coupon.getType())) {
            case FULL_OFF:
            case FULL_MINUS:
                int compare = coupon.getFullMoney().compareTo(orderCategoryPrice);
                // 优惠券价格不能大于订单总价
                if (compare > 0) {
                    throw new ParameterException(40008);
                }
                break;
            case NO_THRESHOLD_MINUS:
                break;
            default:
                throw new ParameterException(40009);
        }
    }

    private BigDecimal getSumByCategoryList(List<SkuOrderBO> skuOrderBOList, List<Long> cidList) {
//        return cidList.stream()
//                .map(cid -> getSumByCategory(skuOrderBOList, cid))
//                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        BigDecimal sum = BigDecimal.ZERO;
        for (Long cid : cidList) {
            sum = sum.add(getSumByCategory(skuOrderBOList, cid));
        }
        return sum;
    }

    // 每一个品类的商品价格总和
    private BigDecimal getSumByCategory(List<SkuOrderBO> skuOrderBOList, Long cid) {
//        return skuOrderBOList.stream()
//                .filter(sku -> sku.getCategoryId().equals(cid))
//                .map(SkuOrderBO::getTotalPrice)
//                .reduce(BigDecimal::add)
//                .orElse(BigDecimal.ZERO);
        BigDecimal sum = BigDecimal.ZERO;
        for (SkuOrderBO skuOrderBO : skuOrderBOList) {
            Long categoryId = skuOrderBO.getCategoryId();
            if (categoryId.equals(cid)) {
                sum = sum.add(skuOrderBO.getTotalPrice());
            }
        }
        return sum;
    }
}
