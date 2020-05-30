package com.wangyijie.missyou.model;

import com.wangyijie.missyou.dto.SkuInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class OrderSku {
    private Long id;
    private Long spuId;
    private BigDecimal finalPrice;
    private BigDecimal singlePrice;
    private List<String> specValues;
    private Integer count;
    private String img;
    private String title;

    public OrderSku(SkuInfoDTO skuInfoDTO, Sku sku) {
        id = sku.getId();
        spuId = sku.getSpuId();
        finalPrice = sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
        singlePrice = sku.getActualPrice();
        count = skuInfoDTO.getCount();
        img = sku.getImg();
        title = sku.getTitle();
        specValues = sku.getSpecValueList();
    }
}
