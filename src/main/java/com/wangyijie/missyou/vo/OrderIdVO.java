package com.wangyijie.missyou.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderIdVO {
    private Long id;

    public OrderIdVO(Long id) {
        this.id = id;
    }
}
