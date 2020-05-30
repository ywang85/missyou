package com.wangyijie.missyou.core.money;

import java.math.BigDecimal;

public interface IMoneyDiscount {
    BigDecimal discount(BigDecimal original, BigDecimal discount);
}
