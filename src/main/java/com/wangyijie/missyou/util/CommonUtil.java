package com.wangyijie.missyou.util;

import com.wangyijie.missyou.bo.PageCounter;

import java.util.Calendar;
import java.util.Date;

public class CommonUtil {
    public static PageCounter convertToPageParameter(Integer start, Integer count) {
        int pageNum = start / count;
        return new PageCounter(pageNum, count);
    }

    public static Boolean isInTimeLine(Date date, Date start, Date end) {
        Long time = date.getTime();
        Long startTime = start.getTime();
        Long endTime = end.getTime();

        return time > startTime && time < endTime;
    }

    public static Calendar addSomeSeconds(Calendar calendar, int seconds) {
        calendar.add(Calendar.SECOND, seconds);
        return calendar;
    }
}
