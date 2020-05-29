package com.wangyijie.missyou.vo;


import com.wangyijie.missyou.model.Category;
import com.wangyijie.missyou.model.Coupon;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class CouponCategoryVO extends CouponPureVO {
    private List<CategoryPureVO> categoryPureVOS = new ArrayList<>();

    public CouponCategoryVO(Coupon coupon) {
        super(coupon);
        List<Category> categoryList = coupon.getCategoryList();
        for (Category category : categoryList) {
            CategoryPureVO categoryPureVO = new CategoryPureVO(category);
            categoryPureVOS.add(categoryPureVO);
        }
    }
}
