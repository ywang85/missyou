package com.wangyijie.missyou.bo;

import com.wangyijie.missyou.dto.SkuInfoDTO;
import com.wangyijie.missyou.model.Sku;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class SkuOrderBO {
    private BigDecimal actualPrice;
    private Integer count;
    private Long categoryId;

    public SkuOrderBO(Sku sku, SkuInfoDTO skuInfoDTO) {
        actualPrice = sku.getActualPrice();
        count = skuInfoDTO.getCount();
        categoryId = sku.getCategoryId();
    }

    public BigDecimal getTotalPrice() {
        return actualPrice.multiply(new BigDecimal(count));
    }
}
