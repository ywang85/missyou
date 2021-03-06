package com.wangyijie.missyou.vo;

import com.wangyijie.missyou.model.Activity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ActivityCouponVo extends ActivityPureVO {
    private List<CouponPureVO> coupons;

    public ActivityCouponVo(Activity activity) {
        super(activity);
        coupons = activity.getCouponList().stream()
                .map(CouponPureVO::new)
//                .map(c-> {return new CouponPureVO(c);})
                .collect(Collectors.toList());
    }


}
