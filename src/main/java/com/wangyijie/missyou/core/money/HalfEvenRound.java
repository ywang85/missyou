package com.wangyijie.missyou.core.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
//@Component
public class HalfEvenRound implements IMoneyDiscount {
    @Override
    public BigDecimal discount(BigDecimal original, BigDecimal discount) {
        BigDecimal actualMoney = original.multiply(discount);
        return actualMoney.setScale(2, RoundingMode.HALF_EVEN);
    }
}
