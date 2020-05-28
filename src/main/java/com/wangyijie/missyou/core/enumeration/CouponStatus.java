package com.wangyijie.missyou.core.enumeration;

import lombok.Getter;

@Getter
public enum CouponStatus {
    AVAILABLE(1, "未过期"),
    USED(2, "已使用"),
    EXPIRED(3, "已过期");
    private Integer value;

    CouponStatus(Integer value, String description) {
        this.value = value;
    }
}
