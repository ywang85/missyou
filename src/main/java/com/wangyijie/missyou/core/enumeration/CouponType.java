package com.wangyijie.missyou.core.enumeration;

import lombok.Getter;

@Getter
public enum CouponType {
    FULL_MINUS(1, "满减券"),
    FULL_OFF(2, "满减折扣券"),
    NO_THRESHOLD_MINUS(3, "无门槛减除券");

    private int value;

    CouponType(int value, String description) {
        this.value = value;
    }

    public static CouponType toType(int value) {
        for (CouponType couponType : values()) {
            if (couponType.value == value) {
                return couponType;
            }
        }
        return null;
    }
}
