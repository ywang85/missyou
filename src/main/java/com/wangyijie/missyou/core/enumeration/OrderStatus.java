package com.wangyijie.missyou.core.enumeration;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ALL(0, "全部"),
    UNPAID(1, "待支付"),
    PAID(2, "已支付"),
    DELIVERED(3, "已发货"),
    FINISHED(4, "已完成"),
    CANCELED(5, "已取消"),

    PAID_BUT_OUT_OF(21, "已支付，但无货"),
    DEAL_OUT_OF(22, "已处理缺货但已支付但情况");

    private int value;
    OrderStatus(int value, String text) {
        this.value = value;
    }

    public static OrderStatus toType(int value) {
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.value == value) {
                return orderStatus;
            }
        }
        return null;
    }
}
