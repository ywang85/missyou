package com.wangyijie.missyou.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageCounter {
    private Integer page;
    private Integer count;

    public PageCounter(Integer page, Integer count) {
        this.page = page;
        this.count = count;
    }
}
