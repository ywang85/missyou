package com.wangyijie.missyou.logic;

import com.wangyijie.missyou.bo.SkuOrderBO;
import com.wangyijie.missyou.dto.OrderDTO;
import com.wangyijie.missyou.dto.SkuInfoDTO;
import com.wangyijie.missyou.exception.http.ParameterException;
import com.wangyijie.missyou.model.OrderSku;
import com.wangyijie.missyou.model.Sku;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderChecker {
    private OrderDTO orderDTO;
    private List<Sku> serverSkuList;
    private CouponChecker couponChecker;
    private Integer maxSkuLimit;
    @Getter
    private List<OrderSku> orderSkuList = new ArrayList<>();

    public OrderChecker(OrderDTO orderDTO, List<Sku> serverSkuList, CouponChecker couponChecker, Integer maxSkuLimit) {
        this.orderDTO = orderDTO;
        this.serverSkuList = serverSkuList;
        this.couponChecker = couponChecker;
        this.maxSkuLimit = maxSkuLimit;
    }

    // 获取订单主要图片
    public String getLeaderImg() {
        return serverSkuList.get(0).getImg();
    }

    public String getLeaderTitle() {
        return serverSkuList.get(0).getTitle();
    }

    public Integer getTotalCount() {
        return orderDTO.getSkuInfoList().stream()
                .map(SkuInfoDTO::getCount)
                .reduce(Integer::sum)
                .orElse(0);
    }

    // orderTotalPrice, serverTotalPrice
    // 下架
    // 售罄商品
    // 超出库存
    // 超出最大数量
    // 优惠券校验
    public void isOk() {
        BigDecimal serverTotalPrice = new BigDecimal("0");
        List<SkuOrderBO> skuOrderBOList = new ArrayList<>();

        skuNotOnSale(orderDTO.getSkuInfoList().size(), serverSkuList.size());
        for (int i = 0; i < serverSkuList.size(); i++) {
            Sku sku = serverSkuList.get(i); // 服务端
            SkuInfoDTO skuInfoDTO = orderDTO.getSkuInfoList().get(i); // 前端
            containsSoldOutSku(sku);
            beyondSkuStock(sku, skuInfoDTO);
            beyondMaxSkuLimit(skuInfoDTO);

            serverTotalPrice = serverTotalPrice.add(calculateSkuOrderPrice(sku, skuInfoDTO));
            skuOrderBOList.add(new SkuOrderBO(sku, skuInfoDTO));
            orderSkuList.add(new OrderSku(skuInfoDTO, sku));
        }
        totalPriceIsOk(orderDTO.getTotalPrice(), serverTotalPrice);

        if (couponChecker != null) {
            couponChecker.isOk();
            couponChecker.canBeUsed(skuOrderBOList, serverTotalPrice);
            couponChecker.finalTotalPriceIsOk(orderDTO.getFinalTotalPrice(), serverTotalPrice);
        }
    }

    private void totalPriceIsOk(BigDecimal orderTotalPrice, BigDecimal serverTotalPrice) {
        if (orderTotalPrice.compareTo(serverTotalPrice) != 0) {
            throw new ParameterException(50005);
        }
    }

    private BigDecimal calculateSkuOrderPrice(Sku sku, SkuInfoDTO skuInfoDTO) {
        if (skuInfoDTO.getCount() <= 0) {
            throw new ParameterException(50007);
        }
        return sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
    }

    private void skuNotOnSale(int count1, int count2) {
        if (count1 != count2) {
            throw new ParameterException(50002);
        }
    }

    private void containsSoldOutSku(Sku sku) {
        if (sku.getStock() == 0) {
            throw new ParameterException(50001);
        }
    }

    private void beyondSkuStock(Sku sku, SkuInfoDTO skuInfoDTO) {
        if (sku.getStock() < skuInfoDTO.getCount()) {
            throw new ParameterException(50003);
        }
    }

    private void beyondMaxSkuLimit(SkuInfoDTO skuInfoDTO) {
        if (skuInfoDTO.getCount() > maxSkuLimit) {
            throw new ParameterException(50004);
        }
    }
}
