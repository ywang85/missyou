package com.wangyijie.missyou.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 存放在sku中specs字段中
public class Spec {
    private Long keyId;
    private String key;
    private Long valueId;
    private String value;
}
