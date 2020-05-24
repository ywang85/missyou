package com.wangyijie.missyou.util;

import com.wangyijie.missyou.bo.PageCounter;

public class CommonUtil {
    public static PageCounter convertToPageParameter(Integer start, Integer count) {
        int pageNum = start / count;
        return new PageCounter(pageNum, count);
    }
}
