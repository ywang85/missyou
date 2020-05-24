package com.wangyijie.missyou.core.enumeration;

import lombok.Getter;

@Getter
public enum LoginType {
    USER_WX(0, "微信登录"),
    USER_EMAIL(1, "邮箱登录");

    private Integer code;
    private String description;

    LoginType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
